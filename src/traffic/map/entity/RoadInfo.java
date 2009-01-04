package traffic.map.entity;

/**
 * A RoadInfo class represents the road information
 * of a specific vehicle, including its road, lane
 * and position on the road.
 * 
 * @author huangsx
 *
 */

public class RoadInfo {
	
	private Road road;
	private int currentLane;
	private double currentPosition;
	
	public RoadInfo(Road r, int lane, double p){
		road=r;
		currentLane=lane;
		currentPosition=p;
	}
	
	public int getCurrentLane(){
		return currentLane;
	}
	
	public double getCurrentPosition(){
		return currentPosition;
	}
	
	public Road getCurrentRoad(){
		return road;
	}
	
	public void setRoad(Road r){
		road=r;
	}
	
	public void setLane(int i){
		if (i<road.getLane())
			currentLane=i;
	}
	
	public void moveBy(double dp){
		if (currentPosition+dp<=road.getLength())
			currentPosition+=dp;
		else
			currentPosition=road.getLength();
	}
	
	public void setPosition(double p){
		if (p<0)
			currentPosition=p;
		else if (p>road.getLength())
			currentPosition=road.getLength();
		else 
			currentPosition=p;
	}
	
	public Point getCurrentPoint(){
		return road.getPositionOnRoad(currentPosition, currentLane);
	}
	
	public Point getNextPoint(double p){
		return road.getPositionOnRoad(currentPosition+p, currentLane);
	}
	
	public void setPoint(Point p){
		double rate;
		if (Math.abs(road.startPoint.xAxis-road.endPoint.xAxis)<
				Math.abs(road.startPoint.yAxis-road.endPoint.yAxis))
			rate=(p.yAxis-road.startPoint.yAxis)/(road.endPoint.yAxis-road.startPoint.yAxis);
		else
			rate=(p.xAxis-road.startPoint.xAxis)/(road.endPoint.xAxis-road.startPoint.xAxis);
		currentPosition=road.length*rate;
	}
}
