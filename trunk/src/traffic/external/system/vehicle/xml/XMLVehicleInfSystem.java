package traffic.external.system.vehicle.xml;

import traffic.external.system.vehicle.VehicleInfIterator;
import traffic.external.system.vehicle.VehicleInfSystem;

/**
 * @author Isaac
 * 
 */
public class XMLVehicleInfSystem implements VehicleInfSystem {
	private XMLVehicleInfParser parser = null;

	@Override
	public VehicleInfIterator getVehicleInf() {
		return parser.iterator();
	}

	@Override
	public void init() {
		parser = new XMLVehicleInfParser("vehicle.xml");
	}
}
