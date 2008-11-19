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

	protected Point(double x, double y) {
		xAxis = x;
		yAxis = y;
		hashCode = Long.rotateLeft(Lib.doubleToInt(x), 32) | Lib.doubleToInt(y);
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
}
