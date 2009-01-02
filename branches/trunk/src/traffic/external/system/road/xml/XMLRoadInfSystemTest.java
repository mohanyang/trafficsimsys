package traffic.external.system.road.xml;

import org.junit.Test;

import traffic.external.system.road.RoadIterator;

/**
 * @author Isaac
 * 
 */
public class XMLRoadInfSystemTest {

	@Test
	public void testGetRoad() {
		XMLRoadInfSystem sys = new XMLRoadInfSystem();
		sys.init();
		for (RoadIterator itr = sys.getRoad(); itr.hasNext();)
			System.out.println(itr.next());
	}
}
