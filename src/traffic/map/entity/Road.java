package traffic.map.entity;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import traffic.map.entity.Vehicle;

/**
 * @author Isaac
 * 
 */
public class Road {
	protected Point startPoint;
	protected Point endPoint;
	protected double length;
	protected int lane;
	private List<Vehicle> vehicleList =
		Collections.synchronizedList(
		new LinkedList<Vehicle>());

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
		synchronized (vehicleList) {
			if (v.road == null && !vehicleList.contains(v)) {
				v.road = this;
				vehicleList.add(v);
			} else if (!v.road.equals(this) && !vehicleList.contains(v)) {
				v.road = this;
				vehicleList.add(v);
			}
		}
	}

	protected int removeVehicle(Vehicle v) {
		synchronized (vehicleList) {
			if (v.road.equals(this) && vehicleList.contains(v))
				vehicleList.remove(v);
			return vehicleList.size();
		}
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
		synchronized (vehicleList) {
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
			return ret;
		}
	}
	
	public int getLane() {
		return lane;
	}

	public void setLane(int lane) {
		this.lane = lane;
	}
	
	public List<Vehicle> getVehicleLinkedList(){
		synchronized (vehicleList) {
			return vehicleList;
		}
	}

	public Iterator<Vehicle> getVehicleList() {
		synchronized (vehicleList) {
			return vehicleList.iterator();
		}
	}
}
