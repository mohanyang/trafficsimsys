package traffic.map.entity;

import java.util.LinkedList;

public class Point {
	private double xAxis = 0;
	private double yAxis = 0;
	private int degree = 0;
	private LinkedList<Road> roadList = new LinkedList<Road>();

	protected Point(int x, int y) {
		xAxis = x;
		yAxis = y;
	}

	public int hashCode() {
		return xAxis << 16 | yAxis;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		return xAxis == ((Point) obj).xAxis && yAxis == ((Point) obj).yAxis;
	}

	public int getDegree() {
		return degree;
	}

	public void addRoad(Road road) {
		roadList.add(road);
		++degree;
	}

	public int removeRoad(Road road) {
		if (roadList.contains(road)) {
			roadList.remove(road);
			--degree;
		}
		return degree;
	}

	public int getXAxis() {
		return xAxis;
	}

	public void setXAxis(int axis) {
		xAxis = axis;
	}

	public int getYAxis() {
		return yAxis;
	}

	public void setYAxis(int axis) {
		yAxis = axis;
	}

	public String toString() {
		return "[point (" + xAxis + ", " + yAxis + ")" + ":" + degree + "]";
	}
}
