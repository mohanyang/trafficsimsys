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
	protected int lane;
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
	
	public Road(Point s, Point e, int l) {
		startPoint = s;
		endPoint = e;
		lane = l;
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

	protected void addVehicle(Vehicle v) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		if (v.road == null && !vehicleList.contains(v)) {
			v.road = this;
			Lib.assertTrue(vehicleList.add(v));
		} else if (!v.road.equals(this) && !vehicleList.contains(v)) {
			v.road = this;
			Lib.assertTrue(vehicleList.add(v));
		}
	}

	protected int removeVehicle(Vehicle v) {
		Lib.assertTrue(lock.isHeldByCurrentThread());
		if (v.road.equals(this) && vehicleList.contains(v)) {
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
	
	public boolean canMove(int lane){
		return true;
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
		return ret;
	}
	
	public int getLane() {
		return lane;
	}

	public void setLane(int lane) {
		this.lane = lane;
	}
	
	public Iterator<Vehicle> getVehicleList() {
		return vehicleList.iterator();
	}
}
