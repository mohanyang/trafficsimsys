package traffic.map.handler;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.external.generate.MyFactory;
import traffic.external.generate.VehicleGenerator;
import traffic.external.system.road.RoadInfSystem;
import traffic.external.system.road.RoadIterator;
import traffic.map.entity.Map;

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

		int pointnum = map.getPointNum();
		MyFactory fact = MyFactory.getInstance();
		VehicleGenerator vg = fact.getVehicleGenerator().getInstance();
		for (int i = 0; i < pointnum; i++) {
			vg.setbornpoint(i);
			for (int j = 0; j < 1; j++) {
				vg.setmaxspeed(20);
				vg.settype(Lib.random(4));
				vg.setinitspeed(Lib.random(5) + 5);
				vg.generate();
			}
		}

		return map;
	}
}