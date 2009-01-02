package traffic.map.entity;

import traffic.basic.Lib;
import traffic.simulation.vehicle.IVehicleControl;

/**
 * @author Isaac
 * 
 */
public class Vehicle {
	protected double speed;
	protected RoadInfo rInfo;
	private VehicleInf inf;
	private IVehicleControl controller = null;

	private int id = 0;
	private static int count = 0;

	protected Vehicle(VehicleInf inf) {
		id = count++;
		this.inf = inf;
		rInfo=new RoadInfo(null, 0, 0);
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
		return "Vehicle " + id + " on " + rInfo.getCurrentRoad() +  " at lane " 
				+ rInfo.getCurrentLane()
				+ " at speed of " + speed
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
	
	public int getDirection(){
		return rInfo.getCurrentRoad().getDirection(rInfo.getCurrentLane());
	}
	
	public int getLength(){
		return inf.getLength();
	}
	
	public void setRoad(Road r, int lane) {
		if (rInfo.getCurrentRoad() != null) {
			Lib.assertTrue(rInfo.getCurrentRoad().isHeldByCurrentThread());
			rInfo.getCurrentRoad().removeVehicle(this);
		}
		r.acquireLock();
		r.addVehicle(this, lane);
		rInfo.setRoad(r);
		rInfo.setPosition(0);
		rInfo.setLane(lane);
		r.releaseLock();
	}
	
	public void removeFromCurrent(){
		Road temp=rInfo.getCurrentRoad();
		temp.acquireLock();
		temp.removeVehicle(this);
		temp.releaseLock();
		
	}

	public Point getPoint() {
		return rInfo.getCurrentPoint();
	}
	
	public Point getNextPoint(double p) {
		return rInfo.getNextPoint(p);
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
	
	public void setController(IVehicleControl c) {
		controller = c;
	}
	
	public IVehicleControl getController() {
		return controller;
	}
}
