package traffic.map.handler;

import java.util.Iterator;

import org.junit.Test;

import traffic.basic.Config;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;

/**
 * @author Isaac
 * 
 */
public class LoadHandlerTest {

	@Test
	public void testRun() {
		Config.load("traffic.ini");
		Map m = new LoadHandler().load();
		System.out.println("map");
		for (Iterator<Point> itr = m.getPointList(); itr.hasNext();) {
			Point p = itr.next();
			System.out.println(p);
			for (Iterator<Road> itrr = p.getRoadList(); itrr.hasNext();)
				System.out.println(itrr.next());
		}
	}
}
