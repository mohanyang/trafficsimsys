package traffic.map.entity;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Isaac
 * 
 */
public class Map {
	private LinkedList<Point> pointList = null;

	public Map() {
		pointList = new LinkedList<Point>();
	}

	public Point newPoint(double x, double y) {
		for (Iterator<Point> itr = pointList.iterator(); itr.hasNext();) {
			Point next = itr.next();
			if (next.isEqual(x, y))
				return next;
		}
		Point p = new Point(x, y);
		pointList.add(p);
		return p;
	}

	public Road newRoad(double x1, double y1, double x2, double y2, int l) {
		Point s = newPoint(x1, y1), e = newPoint(x2, y2);
		for (Iterator<Road> itr = s.getRoadList(); itr.hasNext();) {
			Road next = itr.next();
			if (next.endPoint.equals(e)) {
				next.setLane(l);
				return next;
			}
		}
		Road road = new Road(s, e, l);
		s.addRoad(road);
		e.addRoad(road);
		return road;
	}

	public Vehicle newVehicle(VehicleInf inf) {
		return new Vehicle(inf);
	}

	public Iterator<Point> getPointList() {
		return pointList.iterator();
	}

	public Iterator<Vehicle> getVehicleOnRoad(Road r) {
		return r.getVehicleList();
	}
}
