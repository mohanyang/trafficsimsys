package traffic.external.system.vehicle;

import java.util.Iterator;

import traffic.map.entity.VehicleInf;

/**
 * @author Isaac
 * 
 */
public abstract class VehicleInfIterator implements Iterator<VehicleInf> {

	@Override
	public abstract boolean hasNext();

	@Override
	public abstract VehicleInf next();

	@Override
	public void remove() {
	}

}
