package traffic.map.entity;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Isaac
 * 
 */
public class Map {
	private HashMap<Long, Point> pointMap = null;

	private static Map _instance;

	public static Map getInstance() {
		return _instance;
	}

	public Map() {
		_instance = this;
		pointMap = new HashMap<Long, Point>();
	}

	public Point newPoint(Point p) {
		Point o = pointMap.get(p.hash());
		if (o == null) {
			pointMap.put(p.hash(), p);
			return p;
		} else {
			return o;
		}
	}

	public Point getPoint(Point p) {
		Point o = pointMap.get(p.hash());
		if (o == null) {
			return p;
		} else {
			return o;
		}
	}

	public Point newPoint(double x, double y) {
		return newPoint(new Point(x, y));
	}

	public Road newRoad(Point ps, Point pe, int l) {
		Point s = newPoint(ps), e = newPoint(pe);
		// for (Iterator<Road> itr = s.getRoadList(); itr.hasNext();) {
		// Road next = itr.next();
		// if (next.endPoint.equals(e)) {
		// next.setLane(l);
		// return next;
		// }
		// }
		Road road = new Road(s, e, new byte[l]);
		s.addRoad(road);
		e.addRoad(road);
		return road;
	}

	public Road newRoad(Road r) {
		return newRoad(r.getStartPoint(), r.getEndPoint(), r.getLane());
	}

	public Road newRoad(double x1, double y1, double x2, double y2, int l) {
		return newRoad(new Point(x1, y1), new Point(x2, y2), l);
	}

	public Vehicle newVehicle(VehicleInf inf) {
		return new Vehicle(inf);
	}

	public Iterator<Point> getPointList() {
		return pointMap.values().iterator();
	}

	public Iterator<Vehicle> getVehicleOnRoad(Road r) {
		return r.getVehicleList();
	}

	public Road getRoad(double x, double y) {
		Point p = new Point(x, y);
		for (Iterator<Point> pIter = getPointList(); pIter.hasNext();) {
			Point point = pIter.next();
			for (Iterator<Road> rIter = point.getRoadList(); rIter.hasNext();) {
				Road road = rIter.next();
				if (Point.dotProduct(Point.diff(p, road.startPoint), Point
						.diff(road.endPoint, road.startPoint)) >= 0
						&& Point.dotProduct(Point.diff(p, road.endPoint), Point
								.diff(road.startPoint, road.endPoint)) >= 0) {
					double h = Math.abs(Point.crossProduct(Point.diff(p,
							road.endPoint), Point.diff(p, road.startPoint)))
							* 2 / road.length;
					if (h * 2 <= road.getLane() * Road.laneWidth)
						return road;
				}
			}
		}
		return null;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("map\n");
		for (Iterator<Point> itr = getPointList(); itr.hasNext();) {
			Point p = itr.next();
			buf.append(p + "\n");
			for (Iterator<Road> itrr = p.getRoadList(); itrr.hasNext();)
				buf.append(itrr.next() + "\n");
		}
		return buf.toString();
	}
}
