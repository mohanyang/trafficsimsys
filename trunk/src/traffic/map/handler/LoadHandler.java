package traffic.map.handler;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.external.system.road.RoadInfSystem;
import traffic.external.system.road.RoadIterator;
import traffic.map.entity.Map;

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
		return map;
	}
}
