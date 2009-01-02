package traffic.map.handler;

import java.util.Iterator;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.external.generate.*;
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

		int pointnum=map.getPointNum();
		for(int i=0;i<3;i++){
			MyFactory.getInstance().getVehicleGenerator().getInstance().setbornpoint(i);
			for(int j=0;j<1;j++){
				MyFactory.getInstance().getVehicleGenerator().getInstance().setmaxspeed(20);
				MyFactory.getInstance().getVehicleGenerator().getInstance().settype(Lib.random(4));
				MyFactory.getInstance().getVehicleGenerator().getInstance().setinitspeed(Lib.random(5)+5);
				MyFactory.getInstance().getVehicleGenerator().getInstance().generate();
			}
		}
		
		return map;
	}
}