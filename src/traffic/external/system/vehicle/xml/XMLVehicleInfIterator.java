package traffic.external.system.vehicle.xml;

import org.w3c.dom.Node;

import traffic.external.system.vehicle.VehicleInfIterator;
import traffic.map.entity.VehicleInf;

/**
 * @author Isaac
 * 
 */
public class XMLVehicleInfIterator extends VehicleInfIterator {

	private Node node = null;

	public XMLVehicleInfIterator(Node n) {
		node = n;
	}

	@Override
	public boolean hasNext() {
		return node != null;
	}

	@Override
	public VehicleInf next() {
		return null;
	}
}
