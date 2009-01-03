package traffic.map.entity;

import java.util.Iterator;
import java.util.LinkedList;

import traffic.basic.Lib;

/**
 * @author Isaac
 * 
 */
public class Point {
	protected double xAxis = 0;
	protected double yAxis = 0;
	private long hashCode = 0;
	private LinkedList<Road> roadList = new LinkedList<Road>();
	private LinkedList<RoadInfo> passRoadList = new LinkedList<RoadInfo>();

	public static double distance(Point p1, Point p2) {
		return Math.sqrt((p1.xAxis - p2.xAxis) * (p1.xAxis - p2.xAxis)
				+ (p1.yAxis - p2.yAxis) * (p1.yAxis - p2.yAxis));
	}

	public static Point diff(Point p1, Point p2) {
		return new Point(p1.xAxis - p2.xAxis, p1.yAxis - p2.yAxis);
	}

	public static double dotProduct(Point p1, Point p2) {
		return p1.xAxis * p2.xAxis + p1.yAxis * p2.yAxis;
	}

	public static double crossProduct(Point p1, Point p2) {
		return p1.xAxis * p2.yAxis - p2.xAxis * p1.yAxis;
	}
	
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @param d
	 * @return a point on the line p1-p2, distance d from p2
	 */
	public static Point distanceSegment(Point p1, Point p2, double d){
		return new Point(p2.getXAxis()-d*(p2.getXAxis()-p1.getXAxis())/distance(p1, p2),
				p2.getYAxis()-d*(p2.getYAxis()-p1.getYAxis())/distance(p1, p2));
	}
	
	public static Point ratioSegment(Point p1, Point p2, double lambda){
		return new Point((1-lambda)*p1.getXAxis()+lambda*p2.getXAxis(),
				(1-lambda)*p1.getYAxis()+lambda*p2.getYAxis());
	}

	public Point(double x, double y) {
		xAxis = x;
		yAxis = y;
		hashCode = Long.rotateLeft(Lib.doubleToInt(x), 32) | Lib.doubleToInt(y);
	}

	public Point(Point p) {
		xAxis = p.xAxis;
		yAxis = p.yAxis;
		hashCode = p.hashCode;
	}

	public long hash() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		return isEqual(((Point) obj).xAxis, ((Point) obj).yAxis);
	}

	@Override
	public String toString() {
		return "[point (" + xAxis + ", " + yAxis + ")" + ":" + getDegree()
				+ "]";
	}

	public Point clone() {
		return new Point(xAxis, yAxis);
	}

	public boolean isEqual(double x, double y) {
		return Lib.isEqual(x, xAxis) && Lib.isEqual(y, yAxis);
	}

	public void addRoad(Road road) {
		if (road.startPoint.equals(this) || road.endPoint.equals(this)) {
			roadList.add(road);
		}
	}

	public int removeRoad(Road road) {
		if (roadList.contains(road)) {
			roadList.remove(road);
		}
		return roadList.size();
	}
	
	public void addPassRoad(Road road, double position){
		passRoadList.add(new RoadInfo(road, 0, position));
	}
	
	public int removePassRoad(Road road){
		for (RoadInfo r: passRoadList)
			if (r.getCurrentRoad()==road)
				passRoadList.remove(r);
		return passRoadList.size();
	}

	public double getXAxis() {
		return xAxis;
	}

	public double getYAxis() {
		return yAxis;
	}

	public int getDegree() {
		return roadList.size()+passRoadList.size();
	}

	public Iterator<Road> getRoadList() {
		return roadList.iterator();
	}

	public LinkedList<RoadEntranceInfo> getIntersectionList() {
		return getIntersectionList(true);
	}

	public LinkedList<RoadEntranceInfo> getIntersectionList(boolean direction) {
		LinkedList<RoadEntranceInfo> ret = new LinkedList<RoadEntranceInfo>();
		for (Iterator<Road> itr = getRoadList(); itr.hasNext();) {
			Road curr = itr.next();
			for (int i = 0; i < curr.getLane(); ++i)
				if ((curr.endPoint == this && curr.laneInfo[i] == 0)
						|| (curr.startPoint == this && curr.laneInfo[i] == 1)
						|| !direction) {
					ret.add(new RoadEntranceInfo(curr, i));
				}
		}
		for (RoadInfo itr: passRoadList){
			Road curr=itr.getCurrentRoad();
			for (int i=0; i<curr.getLane(); ++i){
				if ((curr.endPoint.equals(this) && curr.laneInfo[i]==0)
						|| (curr.startPoint.equals(this) && curr.laneInfo[i]==1)
						|| !direction){
					ret.add(new RoadEntranceInfo(curr, i, itr.getCurrentPosition()));
				}
			}
		}
		return ret;
	}
}
