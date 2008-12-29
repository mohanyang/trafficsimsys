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
	protected Point startPoint;
	protected Point endPoint;
	protected double length;
	
	protected byte[] laneInfo;
	protected boolean[] laneMove;
	
	private LinkedList<Vehicle> vehicleList = new LinkedList<Vehicle>();
	private LinkedList<Vehicle> removeList = new LinkedList<Vehicle>();
	private ReentrantLock lock=new ReentrantLock();
	
	public void acquireLock(){
		lock.lock();
	}
	
	public void releaseLock(){
		Lib.assertTrue(lock.isHeldByCurrentThread());
		lock.unlock();
	}
	
	public boolean isHeldByCurrentThread(){
		return lock.isHeldByCurrentThread();
	}
	
	public double getLength(){
		return length;
	}
	
	public Road(Point s, Point e, byte[] l) {
		startPoint = s;
		endPoint = e;
		laneInfo=new byte[l.length];
		System.arraycopy(l, 0, laneInfo, 0, laneInfo.length);
		laneMove=new boolean[l.length];
		for (int i=0; i<l.length; ++i)
			laneMove[i]=true;
		length=Point.distance(s, e);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Road))
			return false;
		return isEqual(((Road) obj).startPoint, ((Road) obj).endPoint);
	}

	@Override
	public String toString() {
		return "road from " + startPoint.toString() + " to "
				+ endPoint.toString();
	}

	public boolean isEqual(Point s, Point e) {
		return (s.equals(startPoint) && e.equals(endPoint))
				|| (s.equals(endPoint) && e.equals(startPoint));
	}

	protected void addVehicle(Vehicle v, int lane) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		if (v.getRoad() == null && !vehicleList.contains(v)) {
			// TODO
			Lib.assertTrue(vehicleList.add(v));
		} else if (!v.getRoad().equals(this) && !vehicleList.contains(v)) {
			// TODO
			Lib.assertTrue(vehicleList.add(v));
		}
	}

	protected int removeVehicle(Vehicle v) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		if (v.getRoad().equals(this) && vehicleList.contains(v)) {
//			vehicleList.remove(v);
			removeList.add(v);
		}
		return vehicleList.size()-removeList.size();
	}
	
	public void performRemoval(){
		Lib.assertTrue(lock.isHeldByCurrentThread());
		while (!removeList.isEmpty()){
			Vehicle v=removeList.pollFirst();
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
	
	private void moveLine(Point start, Point end, double d){
		double distance=Point.distance(start, end);
		double theta=Math.asin((end.getYAxis()-start.getYAxis())/distance)+Math.PI/2;
		start.xAxis+=d*Math.cos(theta);
		start.yAxis+=d*Math.sin(theta);
		end.xAxis+=d*Math.cos(theta);
		end.yAxis+=d*Math.sin(theta);
	}
	
	public Point getPositionOnRoad(double distance, int lane){
		return getPositionOnRoad(distance, lane, true);
	}
	
	public Point getPositionOnRoad(double distance, int lane, boolean moveLine){
		Point p1=new Point(startPoint.xAxis, startPoint.yAxis), 
				p2=new Point(endPoint.xAxis, endPoint.yAxis);
		if (moveLine)
			moveLine(p1, p2, laneInfo.length*26/2-lane*26-13);
		if (laneInfo[lane]!=0) {
			Point temp=p1;
			p1=p2;
			p2=temp;
		}
		if (distance<0)
			distance=0;
		else if (distance>length)
			distance=length;
		double retx=(p2.xAxis-p1.xAxis)*(distance)/length+p1.xAxis;
		double rety=(p2.yAxis-p1.yAxis)*(distance)/length+p1.yAxis;
		return Map.getInstance().getPoint(new Point(retx, rety));
	}
	
	public boolean canMove(int lane){
		return laneMove[lane];
	}
	
	public void setMove(int lane, boolean f){
		laneMove[lane]=f;
	}
	
	public byte getDirection(int lane){
		return laneInfo[lane];
	}
	
	public double closestDistance(Vehicle p){
		Lib.assertTrue(lock.isHeldByCurrentThread());
		// TODO should calculate the length of the car
		double ret=length-p.getPosition();
		Vehicle curr=vehicleList.iterator().next();
		for (Iterator<Vehicle> itr=vehicleList.iterator(); itr.hasNext();
			curr=itr.next()){
			if (curr!=p && curr.getLane()==p.getLane() && curr.getPosition()>=p.getPosition()) {
				ret=ret<(-p.getPosition()+curr.getPosition())?
						ret:(-p.getPosition()+curr.getPosition());
			}
		}
		// TODO vehicle length
		return ret-19;
	}
	
	public int getLane() {
		return laneInfo.length;
	}
	
	public Iterator<Vehicle> getVehicleList() {
		return vehicleList.iterator();
	}
}
