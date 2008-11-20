package traffic.external.system.road.xml;

import traffic.external.system.road.RoadInfSystem;
import traffic.external.system.road.RoadIterator;

/**
 * @author Isaac
 * 
 */
public class XMLRoadInfSystem implements RoadInfSystem {
	private XMLRoadParser parser = null;

	@Override
	public RoadIterator getRoad() {
		return parser.iterator();
	}

	@Override
	public void init() {
		parser = new XMLRoadParser("road.xml");
	}
}
