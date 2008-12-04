package traffic.map.entity;

import traffic.basic.*;
import traffic.map.entity.Map;

/**
 * @author Isaac
 * 
 */
public class Vehicle {
	protected Road road;
	protected double speed;
	protected double currentPosition;
	private VehicleInf inf;

	private int id = 0;
	private static int count = 0;
	private int lane = 0;

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
		return "Vehicle " + id + " on " + road + " at speed of " + speed
				+ " current at " + getPoint();
	}
	
	public double getPosition(){
		return currentPosition;
	}
	
	public void setPosition(double p){
		currentPosition=p;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public void setRoad(Road r) {
		if (road != null) {
			Lib.assertTrue(road.isHeldByCurrentThread());
			road.removeVehicle(this);
		}
		r.acquireLock();
		r.addVehicle(this);
		currentPosition=0;
		r.releaseLock();
	}

	public Point getPoint() {
		double retx=(road.endPoint.xAxis-road.startPoint.xAxis)*currentPosition/road.length
				+road.startPoint.xAxis;
		double rety=(road.endPoint.yAxis-road.startPoint.yAxis)*currentPosition/road.length
				+road.startPoint.yAxis;
		return Map.getInstance().getPoint(new Point(retx, rety));
	}
	
	public Point getNextPoint() {
		double retx=(road.endPoint.xAxis-road.startPoint.xAxis)
				*(currentPosition+speed)/road.length
				+road.startPoint.xAxis;
		double rety=(road.endPoint.yAxis-road.startPoint.yAxis)
				*(currentPosition+speed)/road.length
				+road.startPoint.yAxis;
		return Map.getInstance().getPoint(new Point(retx, rety));
	}

	public void setPoint(Point p) {
		double rate;
		if (Math.abs(road.startPoint.xAxis-road.endPoint.xAxis)<
				Math.abs(road.startPoint.yAxis-road.endPoint.yAxis))
			rate=(p.yAxis-road.startPoint.yAxis)/(road.endPoint.yAxis-road.startPoint.yAxis);
		else
			rate=(p.xAxis-road.startPoint.xAxis)/(road.endPoint.xAxis-road.startPoint.xAxis);
		currentPosition=road.length*rate;
	}
	
	public int getLane(){
		return lane;
	}

	public double getSpeed() {
		return speed;
	}
	
	public void proceed(){
		currentPosition+=speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public VehicleInf getVehicleInf() {
		return inf;
	}
}
