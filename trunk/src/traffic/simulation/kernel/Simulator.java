package traffic.simulation.kernel;

import java.util.Iterator;

import traffic.basic.Config;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.handler.LoadHandler;

public class Simulator {
	private Map map = null;

	public Simulator() {
		Config.load("traffic.ini");
		map = new LoadHandler().run();
		System.out.println("map");
		for (Iterator<Point> itr = map.getPointList(); itr.hasNext();) {
			Point p = itr.next();
			System.out.println(p);
			for (Iterator<Road> itrr = p.getRoadList(); itrr.hasNext();)
				System.out.println(itrr.next());
		}
	}
}
