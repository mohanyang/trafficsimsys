package traffic.map.entity;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Isaac
 * 
 */
public class Map {
	private HashMap<Long, Point> pointMap = null;

	public Map() {
		pointMap = new HashMap<Long, Point>();
	}

	public Point newPoint(double x, double y) {
		Point p = new Point(x, y);
		Point o = pointMap.get(p.hash());
		if (o == null) {
			pointMap.put(p.hash(), p);
			return p;
		} else {
			return o;
		}
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
		return pointMap.values().iterator();
	}

	public Iterator<Vehicle> getVehicleOnRoad(Road r) {
		return r.getVehicleList();
	}
}
