package traffic.map.entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import java.util.TreeSet;

import traffic.basic.Lib;
import traffic.log.Log;

/**
 * @author Isaac
 * 
 */
public class Road {

	public static final int laneWidth = 26;

	public int n1 = -1, n2 = -1;
	protected Point startPoint;
	protected Point endPoint;
	protected double length;

	protected byte[] laneInfo;
	protected boolean[] laneMove;

	private LinkedList<Vehicle> vehicleList = new LinkedList<Vehicle>();
	private LinkedList<Vehicle> removeList = new LinkedList<Vehicle>();
	private LinkedList<Vehicle> insertList = new LinkedList<Vehicle>();
	private ReentrantLock lock = new ReentrantLock();

	private TreeSet<Double> intersectionList = new TreeSet<Double>();

	public static Point intersect(Road r1, Road r2) {
		double s1 = Point.crossProduct(Point.diff(r2.endPoint, r1.startPoint),
				Point.diff(r2.startPoint, r1.startPoint));
		double s2 = Point.crossProduct(Point.diff(r2.endPoint, r2.startPoint),
				Point.diff(r1.endPoint, r2.startPoint));
		double s1p = Point.crossProduct(Point.diff(r2.endPoint, r1.startPoint),
				Point.diff(r1.endPoint, r1.startPoint));
		double s2p = Point.crossProduct(Point.diff(r1.endPoint, r1.startPoint),
				Point.diff(r2.startPoint, r1.startPoint));
		if (s1 * s2 < 0 || s1p * s2p < 0)
			return null;
		if (Math.abs(s1 + s2) > Math.abs(s1p + s2p)) {
			if (Math.abs(s1 + s2) < 1e-4 || s1 / (s1 + s2) < 0
					|| s1 / (s1 + s2) > 1)
				return null;
			else
				return Point.ratioSegment(r1.startPoint, r1.endPoint, s1
						/ (s1 + s2));
		} else {
			if (Math.abs(s1p + s2p) < 1e-4 || s2p / (s1p + s2p) < 0
					|| s2p / (s1p + s2p) > 1)
				return null;
			else
				return Point.ratioSegment(r2.startPoint, r2.endPoint, s2p
						/ (s1p + s2p));
		}
	}

	/**
	 * acquire the lock used to protect concurrent visit.
	 * 
	 * @author huangsx
	 */
	public void acquireLock() {
		lock.lock();
	}

	/**
	 * release the synchronization lock.
	 * 
	 * @author huangsx
	 */
	public void releaseLock() {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		lock.unlock();
	}

	/**
	 * decide whether the lock is held by the current thread
	 * 
	 * @author huangsx *
	 * @return true if the lock is held by current thread
	 */
	public boolean isHeldByCurrentThread() {
		return lock.isHeldByCurrentThread();
	}

	/**
	 * get the length of the current road
	 * 
	 * @author huangsx
	 * @return
	 */
	public double getLength() {
		return length;
	}

	/**
	 * constructs a new road
	 * 
	 * @author huangsx
	 * @param s
	 *            source point
	 * @param e
	 *            destination point
	 * @param l
	 *            a byte array representing lane direction
	 */
	public Road(Point s, Point e, byte[] l) {
		Log.getInstance().writeln("=== lane info ===");
		for (int i = 0; i < l.length; ++i)
			Log.getInstance().write(" " + l[i]);
		Log.getInstance().writeln();
		startPoint = s;
		endPoint = e;
		laneInfo = new byte[l.length];
		System.arraycopy(l, 0, laneInfo, 0, laneInfo.length);
		laneMove = new boolean[l.length];
		for (int i = 0; i < l.length; ++i)
			laneMove[i] = true;
		length = Point.distance(s, e);
		intersectionList.add(new Double(0));
		intersectionList.add(new Double(length));
		Log.getInstance().writeln("constructing " + this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Road))
			return false;
		return isEqual(((Road) obj).startPoint, ((Road) obj).endPoint);
	}

	@Override
	public String toString() {
		String ret = "road from " + startPoint.toString() + " to "
				+ endPoint.toString();
		for (int i = 0; i < laneInfo.length; ++i)
			ret += " " + laneInfo[i];
		return ret;
	}

	public boolean isEqual(Point s, Point e) {
		return (s.equals(startPoint) && e.equals(endPoint))
				|| (s.equals(endPoint) && e.equals(startPoint));
	}

	protected void addVehicle(Vehicle v, int lane) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		// if (v.getRoad() == null && !vehicleList.contains(v)) {
		// Lib.assertTrue(vehicleList.add(v));
		// } else if (!v.getRoad().equals(this) && !vehicleList.contains(v)) {
		// Lib.assertTrue(vehicleList.add(v));
		// }
		// System.out.println("inserting vehicle: " + v);
		insertList.add(v);
	}

	/**
	 * remove the vehicle from the current road. must acquire the
	 * synchronization lock in advance. the removed vehicle would be added into
	 * a removal queue, rather than deleted directly, in order to provide
	 * concurrent iterating.
	 * 
	 * @author huangsx
	 * @param v
	 *            the vehicle to be removed
	 * @return the number of vehicles remaining on the road
	 */
	protected int removeVehicle(Vehicle v) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		Log.getInstance().writeln("removing vehicle: " + v);
		if (v.getRoad().equals(this) && vehicleList.contains(v)) {
			removeList.add(v);
		}
		return vehicleList.size() - removeList.size() + insertList.size();
	}

	/**
	 * perform actual removal. remove the vehicles in the removal queue. must
	 * acquire concurrent visit lock first.
	 * 
	 * @author huangsx
	 */
	public void performRemoval() {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		if (!removeList.isEmpty())
			Log.getInstance().writeln("removing the following vehicles:");
		else
			return;
		while (!removeList.isEmpty()) {
			Vehicle v = removeList.pollFirst();
			vehicleList.remove(v);
			Log.getInstance().writeln(v.toString());
		}
		Log.getInstance().writeln("remove finished");
		removeList.clear();
	}

	public void performInsertion() {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		if (!insertList.isEmpty())
			Log.getInstance().writeln("performing insertion:");
		else
			return;
		while (!insertList.isEmpty()) {
			Vehicle v = insertList.pollFirst();
			vehicleList.add(v);
			Log.getInstance().writeln(v.toString());
		}
		Log.getInstance().writeln("insertion finished.");
	}

	public void flushQueue() {
		performRemoval();
		performInsertion();
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * move the line from start and point to their right by d. the resulting
	 * starting and destination point is stored in start and end, i.e., the
	 * original parameter would be modified. BE SURE TO CREATE A NEW ONE when
	 * necessary.
	 * 
	 * @param start
	 *            starting point of the line
	 * @param end
	 *            ending point of the line
	 * @param d
	 *            the distance to be moved
	 */
	private static void moveLine(Point start, Point end, double d) {
		double distance = Point.distance(start, end);
		double theta = Math
				.asin((end.getYAxis() - start.getYAxis()) / distance)
				+ Math.PI / 2;
		start.xAxis += d * Math.cos(theta);
		start.yAxis += d * Math.sin(theta);
		end.xAxis += d * Math.cos(theta);
		end.yAxis += d * Math.sin(theta);
	}

	public static void moveLine(Point start, Point end, Point m, double d) {
		if (start.equals(m))
			moveLine(m, end, d);
		else
			moveLine(start, m, d);
	}

	/**
	 * get the coordinate (point) distance from the starting point of the
	 * lane-th lane of the current road.
	 * 
	 * @author huangsx
	 * @param distance
	 * @param lane
	 * @return the point of the corresponding position
	 */
	public Point getPositionOnRoad(double distance, int lane) {
		return getPositionOnRoad(distance, lane, true);
	}

	/**
	 * get the coordinate (point) distance from the starting point of the
	 * lane-th lane of the current road.
	 * 
	 * @author huangsx
	 * @param distance
	 * @param lane
	 * @param moveLine
	 *            whether perform moveLine. false if only consider the central
	 *            axis of the road
	 * @return the point of the corresponding position
	 */
	public Point getPositionOnRoad(double distance, int lane, boolean moveLine) {
		Point p1 = startPoint.clone(), p2 = endPoint.clone();
		if (moveLine)
			moveLine(p1, p2, laneInfo.length * laneWidth / 2 - lane * laneWidth
					- laneWidth / 2);
		if (laneInfo[lane] == 0) {
			Point temp = p1;
			p1 = p2;
			p2 = temp;
		}
		if (distance < 0)
			distance = 0;
		else if (distance > length)
			distance = length;
		double retx = (p2.xAxis - p1.xAxis) * (distance) / length + p1.xAxis;
		double rety = (p2.yAxis - p1.yAxis) * (distance) / length + p1.yAxis;
		return Map.getInstance().getPoint(new Point(retx, rety));
	}

	/**
	 * whether the cars in the current lane can move (e.g. blocked, signalled,
	 * etc.)
	 * 
	 * @author huangsx
	 * @param lane
	 * @return
	 */
	public boolean canMove(int lane) {
		return laneMove[lane];
	}

	public void setMove(int lane, boolean f) {
		laneMove[lane] = f;
	}

	public byte getDirection(int lane) {
		return laneInfo[lane];
	}

	/**
	 * return the closest distance on the road from the current vehicle (in
	 * front of the vehicle)
	 * 
	 * @author huangsx
	 * @param p
	 *            the current vehicle
	 * @return the closest distance from the other objects to p, or the distance
	 *         to the next intersection if it is closer.
	 */
	public double closestDistance(Vehicle p) {
		return closestDistance(p.getPosition(), p.getLane(), p);
	}

	/**
	 * 
	 * @param position
	 * @param lane
	 * @param pv
	 *            the current vehicle
	 * @return the closest distance from the other objects to p, or the distance
	 *         to the next intersection if it is closer.
	 */
	public double closestDistance(double position, int lane, Vehicle pv) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		// TODO should calculate the length of the car
		double ret = length - position;
		int dir;
		if (laneInfo[lane] == 0)
			dir = -1;
		else
			dir = 1;
		Vehicle curr = vehicleList.iterator().next();
		for (Iterator<Vehicle> itr = vehicleList.iterator(); itr.hasNext(); curr = itr
				.next()) {
			if (curr != pv && curr.getLane() == lane
					&& curr.getPosition() * dir >= position * dir) {
				ret = ret < (dir * (curr.getPosition() - position)) ? ret
						: (dir * (curr.getPosition() - position));
			}
		}
		// TODO vehicle length
		return ret;
	}

	public Vehicle closestVehicle(double position, int lane, Vehicle pv) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		double ret = Double.MAX_VALUE;
		Vehicle retV = null;
		int dir = (laneInfo[lane] == 0) ? -1 : 1;
		Vehicle curr = vehicleList.iterator().next();
		for (Iterator<Vehicle> itr = vehicleList.iterator(); itr.hasNext(); curr = itr
				.next()) {
			if (curr != pv && curr.getLane() == lane
					&& curr.getPosition() * dir >= position * dir) {
				if (ret > (dir * (curr.getPosition() - position))) {
					ret = (dir * (curr.getPosition() - position));
					retV = curr;
				}
			}
		}
		return retV;
	}

	public double closestIntersection(double position, int lane) {
		if (laneInfo[lane] == 0) {
			return intersectionList.floor(position);
		} else {
			return intersectionList.ceiling(position);
		}
	}

	public int getLane() {
		return laneInfo.length;
	}

	public Iterator<Vehicle> getVehicleList() {
		return vehicleList.iterator();
	}

	public LinkedList<RoadEntranceInfo> getIntersectionList(int lane) {
		if (laneInfo[lane] == 0)
			return startPoint.getIntersectionList();
		else
			return endPoint.getIntersectionList();
	}

	public double getLamda() {
		return (endPoint.yAxis - startPoint.yAxis)
				/ (endPoint.xAxis - startPoint.xAxis);
	}

	public RoadInfo getInfoByPoint(Point x) {
		Point sp = Point.diff(x, startPoint);
		Point se = Point.diff(endPoint, startPoint);
		double distance = Point.dotProduct(sp, se) / length;
		double delta = Point.crossProduct(se, sp) / length;
		delta += Road.laneWidth * laneInfo.length * 0.5;
		int lane = getLane() - 1 - (int) Math.floor(delta / Road.laneWidth);
		if (laneInfo[lane] == 0)
			distance = length - distance;
		if (distance < 0)
			Log.getInstance().writeln(
					toString() + " " + x.toString() + " " + distance);
		return new RoadInfo(this, lane, distance);
	}

	public LinkedList<Road> getRoadBySegment() {
		LinkedList<Road> ret = new LinkedList<Road>();
		Point former = startPoint;
		for (Double dc : intersectionList) {
			Point current = getPositionOnRoad(dc, 0, false);
			byte[] temp = new byte[laneInfo.length];
			System.arraycopy(laneInfo, 0, temp, 0, laneInfo.length);
			ret.add(new Road(former, current, temp));
			former = current;
		}
		return ret;
	}

	public void insertIntersection(double d) {
		Double fl = intersectionList.floor(d);
		if (Lib.isEqual(d, fl))
			return;
		fl = intersectionList.ceiling(d);
		if (Lib.isEqual(d, fl))
			return;
		intersectionList.add(d);
	}

	public void clearVehicle() {
		vehicleList = new LinkedList<Vehicle>();
	}
}
