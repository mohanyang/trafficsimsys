package traffic.map.entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.*;

import traffic.map.entity.Vehicle;
import traffic.basic.*;

/**
 * @author Isaac
 * 
 */
public class Road {
	
	public static final int laneWidth = 26;
	
	protected Point startPoint;
	protected Point endPoint;
	protected double length;

	protected byte[] laneInfo;
	protected boolean[] laneMove;

	private LinkedList<Vehicle> vehicleList = new LinkedList<Vehicle>();
	private LinkedList<Vehicle> removeList = new LinkedList<Vehicle>();
	private ReentrantLock lock = new ReentrantLock();

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
	 * @author huangsx	 * 
	 * @return true if the lock is held by current thread
	 */
	public boolean isHeldByCurrentThread() {
		return lock.isHeldByCurrentThread();
	}

	/**
	 * get the length of the current road
	 * @author huangsx
	 * @return
	 */
	public double getLength() {
		return length;
	}

	/**
	 * constructs a new road
	 * @author huangsx
	 * @param s source point
	 * @param e destination point
	 * @param l a byte array representing lane direction
	 */
	public Road(Point s, Point e, byte[] l) {
		System.out.println("=== lane info ===");
		for (int i=0; i<l.length; ++i)
			System.out.println(" " + l[i]);
		startPoint = s;
		endPoint = e;
		laneInfo = new byte[l.length];
		System.arraycopy(l, 0, laneInfo, 0, laneInfo.length);
		laneMove = new boolean[l.length];
		for (int i = 0; i < l.length; ++i)
			laneMove[i] = true;
		length = Point.distance(s, e);
		System.out.print(this);
		System.out.println();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Road))
			return false;
		return isEqual(((Road) obj).startPoint, ((Road) obj).endPoint);
	}

	@Override
	public String toString() {
		String ret="road from " + startPoint.toString() + " to "
				+ endPoint.toString();
		for (int i=0; i<laneInfo.length; ++i)
			ret+=" " + laneInfo[i];
		return ret;
	}

	public boolean isEqual(Point s, Point e) {
		return (s.equals(startPoint) && e.equals(endPoint))
				|| (s.equals(endPoint) && e.equals(startPoint));
	}

	protected void addVehicle(Vehicle v, int lane) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		if (v.getRoad() == null && !vehicleList.contains(v)) {
			Lib.assertTrue(vehicleList.add(v));
		} else if (!v.getRoad().equals(this) && !vehicleList.contains(v)) {
			Lib.assertTrue(vehicleList.add(v));
		}
	}

	/**
	 * remove the vehicle from the current road. must acquire 
	 * the synchronization lock in advance.
	 * the removed vehicle would be added into a removal queue,
	 * rather than deleted directly, in order to provide concurrent
	 * iterating.
	 * @author huangsx
	 * @param v the vehicle to be removed
	 * @return the number of vehicles remaining on the road
	 */
	protected int removeVehicle(Vehicle v) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		if (v.getRoad().equals(this) && vehicleList.contains(v)) {
			removeList.add(v);
		}
		return vehicleList.size() - removeList.size();
	}

	/**
	 * perform actual removal. remove the vehicles in the removal queue.
	 * must acquire concurrent visit lock first.
	 * @author huangsx
	 */
	public void performRemoval() {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		while (!removeList.isEmpty()) {
			Vehicle v = removeList.pollFirst();
			vehicleList.remove(v);
		}
		removeList.clear();
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * move the line from start and point to their right by d.
	 * the resulting starting and destination point is stored
	 * in start and end, i.e., the original parameter would be
	 * modified. BE SURE TO CREATE A NEW ONE when necessary.
	 * @param start starting point of the line
	 * @param end ending point of the line
	 * @param d the distance to be moved
	 */
	public void moveLine(Point start, Point end, double d) {
		double distance = Point.distance(start, end);
		double theta = Math
				.asin((end.getYAxis() - start.getYAxis()) / distance)
				+ Math.PI / 2;
		start.xAxis += d * Math.cos(theta);
		start.yAxis += d * Math.sin(theta);
		end.xAxis += d * Math.cos(theta);
		end.yAxis += d * Math.sin(theta);
	}

	/**
	 * get the coordinate (point) distance from the starting point
	 * of the lane-th lane of the current road.
	 * @author huangsx
	 * @param distance
	 * @param lane
	 * @return the point of the corresponding position
	 */
	public Point getPositionOnRoad(double distance, int lane) {
		return getPositionOnRoad(distance, lane, true);
	}

	/**
	 * get the coordinate (point) distance from the starting point
	 * of the lane-th lane of the current road.
	 * @author huangsx
	 * @param distance
	 * @param lane
	 * @param moveLine whether perform moveLine. false if only consider the
	 * central axis of the road
	 * @return the point of the corresponding position
	 */
	public Point getPositionOnRoad(double distance, int lane, boolean moveLine) {
		Point p1 = new Point(startPoint.xAxis, startPoint.yAxis), p2 = new Point(
				endPoint.xAxis, endPoint.yAxis);
		if (moveLine)
			moveLine(p1, p2, laneInfo.length * 26 / 2 - lane * 26 - 13);
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
	 * whether the cars in the current lane can move (e.g. blocked, signalled, etc.)
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
	 * return the closest distance on the road from the current vehicle
	 * (in front of the vehicle)
	 * @author huangsx
	 * @param p the current vehicle
	 * @return the closest distance from the other objects to p,
	 * or the distance to the next intersection if it is closer.
	 */
	public double closestDistance(Vehicle p) {
		return closestDistance(p.getPosition(), p.getLane(), p);
	}

	/**
	 * 
	 * @param position
	 * @param lane
	 * @param pv the current vehicle
	 * @return the closest distance from the other objects to p,
	 * or the distance to the next intersection if it is closer.
	 */
	public double closestDistance(double position, int lane, Vehicle pv) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		// TODO should calculate the length of the car
		double ret = length-position;
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
}
