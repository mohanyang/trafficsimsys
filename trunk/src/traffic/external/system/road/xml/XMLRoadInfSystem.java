package traffic.external.system.road.xml;

import traffic.external.system.road.RoadInfSystem;
import traffic.external.system.road.RoadIterator;
import traffic.basic.Config;

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
		String filename = Config.getString("traffic.external.system.road.xmlFileName");
		parser = new XMLRoadParser(filename);
	}
}
