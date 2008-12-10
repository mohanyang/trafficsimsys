package traffic.map.handler;

import java.util.Iterator;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.external.system.road.RoadInfSystem;
import traffic.external.system.road.RoadIterator;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;
import traffic.map.entity.VehicleInf;

/**
 * @author Isaac
 * 
 */
public class LoadHandler {
	public Map run() {
		Map map = new Map();
		RoadInfSystem roadSys = (RoadInfSystem) Lib.constructObject(Config
				.getString("traffic.external.system.road.RoadInfSystem"));
		roadSys.init();
		for (RoadIterator itr = roadSys.getRoad(); itr.hasNext();)
			map.newRoad(itr.next());

		// testing part
		for (Iterator<Point> itr = map.getPointList(); itr.hasNext();) {
			Point p = itr.next();
			double rnd = Lib.random();
			if (rnd >0.25) {
				for (Iterator<Road> itrr = p.getRoadList(); itrr.hasNext();)
					if (Lib.random() >0.75) {
						Road r = itrr.next();
						r.acquireLock();
						Vehicle v = map
								.newVehicle(new VehicleInf(Lib.random(4),10,5));
						v.setPosition(0);
						v.setSpeed(Lib.random(5)+5);
						v.setRoad(r);
						r.releaseLock();
						break;
					}
			}
		}
		return map;
	}
}