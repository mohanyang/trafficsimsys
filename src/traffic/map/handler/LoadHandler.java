package traffic.map.handler;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.external.system.road.RoadInfSystem;
import traffic.external.system.road.RoadIterator;

/**
 * @author Isaac
 * 
 */
public class LoadHandler {
	public void run() {
		RoadInfSystem roadSys = (RoadInfSystem) Lib.constructObject(Config
				.getString("traffic.external.system.road.RoadInfSystem"));
		roadSys.init();
		for (RoadIterator itr = roadSys.getRoad(); itr.hasNext();)
			System.out.println(itr.next());
	}
}
