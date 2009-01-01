package traffic.map.handler;

import java.util.Iterator;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.external.generate.*;
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
	public Map load() {
		Map map = new Map();
		RoadInfSystem roadSys = (RoadInfSystem) Lib.constructObject(Config
				.getString("traffic.external.system.road.RoadInfSystem"));
		roadSys.init();
		for (RoadIterator itr = roadSys.getRoad(); itr.hasNext();)
			map.newRoad(itr.next());

		int pointnum=map.getPointNum();
		for(int i=0;i<pointnum;i++){
			MyFactory.getInstance().getVehicleGenerator().getInstance().setbornpoint(i);
			for(int j=0;j<4;j++){
				MyFactory.getInstance().getVehicleGenerator().getInstance().setmaxspeed(20);
				MyFactory.getInstance().getVehicleGenerator().getInstance().settype(Lib.random(4));
				MyFactory.getInstance().getVehicleGenerator().getInstance().setinitspeed(Lib.random(5)+5);
				MyFactory.getInstance().getVehicleGenerator().getInstance().generate();
			}
		}
		
		// testing part
/*		for (Iterator<Point> itr = map.getPointList(); itr.hasNext();) {
			Point p = itr.next();
			double rnd = Lib.random();
			if (rnd < 0.7) {
				for (Iterator<Road> itrr = p.getRoadList(); itrr.hasNext();)
					if (Lib.random() > 0.5) {
						Road r = itrr.next();
						r.acquireLock();
						Vehicle v = map
								.newVehicle(new VehicleInf(Lib.random(4),10,5));
						v.setRoad(r, 0);
						v.setPosition(0);
						v.setSpeed(Lib.random(5)+5);
						r.releaseLock();
						break;
					}
			}
		}*/
		return map;
	}
}