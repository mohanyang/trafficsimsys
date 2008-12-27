package traffic.map.entity;

import traffic.basic.*;
import traffic.map.entity.Map;

/**
 * @author Isaac
 * 
 */
public class Vehicle {
	protected double speed;
	protected RoadInfo rInfo;
	private VehicleInf inf;

	private int id = 0;
	private static int count = 0;

	protected Vehicle(VehicleInf inf) {
		id = count++;
		this.inf = inf;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vehicle))
			return false;
		return ((Vehicle) obj).id == id;
	}

	@Override
	public String toString() {
		return "Vehicle " + id + " on " + rInfo.getCurrentRoad() + " at speed of " + speed
				+ " current at " + getPoint();
	}
	
	public double getPosition(){
		return rInfo.getCurrentPosition();
	}
	
	public void setPosition(double p){
		rInfo.setPosition(p);
	}
	
	public Road getRoad() {
		return rInfo.getCurrentRoad();
	}
	
	public void setRoad(Road r, int lane) {
		if (rInfo.getCurrentRoad() != null) {
			Lib.assertTrue(rInfo.getCurrentRoad().isHeldByCurrentThread());
			rInfo.getCurrentRoad().removeVehicle(this);
		}
		r.acquireLock();
		r.addVehicle(this, lane);
		rInfo.setPosition(0);
		rInfo.setLane(lane);
		r.releaseLock();
	}

	public Point getPoint() {
		return rInfo.getCurrentPoint();
	}
	
	public void setPoint(Point p) {
		rInfo.setPoint(p);
	}
	
	public int getLane(){
		return rInfo.getCurrentLane();
	}

	public double getSpeed() {
		return speed;
	}
	
	public void proceed(){
		rInfo.moveBy(speed);
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public VehicleInf getVehicleInf() {
		return inf;
	}
}
