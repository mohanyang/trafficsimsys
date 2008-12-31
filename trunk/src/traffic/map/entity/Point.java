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
	
	public static double distance(Point p1, Point p2){
		return Math.sqrt((p1.xAxis-p2.xAxis)*(p1.xAxis-p2.xAxis)
				+(p1.yAxis-p2.yAxis)*(p1.yAxis-p2.yAxis));
	}

	public Point(double x, double y) {
		xAxis = x;
		yAxis = y;
		hashCode = Long.rotateLeft(Lib.doubleToInt(x), 32) | Lib.doubleToInt(y);
	}
	
	public Point(Point p){
		xAxis=p.xAxis;
		yAxis=p.yAxis;
		hashCode=p.hashCode;
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

	public double getXAxis() {
		return xAxis;
	}

	public double getYAxis() {
		return yAxis;
	}

	public int getDegree() {
		return roadList.size();
	}

	public Iterator<Road> getRoadList() {
		return roadList.iterator();
	}
	
	public LinkedList<RoadEntranceInfo> getIntersectionList(){
		return getIntersectionList(true);
	}
	
	public LinkedList<RoadEntranceInfo> getIntersectionList(boolean direction){
		LinkedList<RoadEntranceInfo> ret=new LinkedList<RoadEntranceInfo>();
		for (Iterator<Road> itr=getRoadList(); itr.hasNext(); ){
			Road curr=itr.next();
			for (int i=0; i<curr.getLane(); ++i)
				if (curr.getPositionOnRoad(0, i)==this || !direction){
					ret.add(new RoadEntranceInfo(curr, i));
				}
		}
		return ret;
	}
}
