package traffic.map.entity;

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
			currentPosition=dp;
	}
	
	public void setPosition(double p){
		if (p>=0 && p<=road.getLength())
			currentPosition=p;
	}
	
	public Point getCurrentPoint(){
		double retx=(road.endPoint.xAxis-road.startPoint.xAxis)*currentPosition/road.length
				+road.startPoint.xAxis;
		double rety=(road.endPoint.yAxis-road.startPoint.yAxis)*currentPosition/road.length
				+road.startPoint.yAxis;
		return Map.getInstance().getPoint(new Point(retx, rety));
	}
	
	public Point getNextPoint(double p){
		double retx=(road.endPoint.xAxis-road.startPoint.xAxis)*(currentPosition+p)
				/road.length+road.startPoint.xAxis;
		double rety=(road.endPoint.yAxis-road.startPoint.yAxis)*(currentPosition+p)
				/road.length+road.startPoint.yAxis;
		return Map.getInstance().getPoint(new Point(retx, rety));
		
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
