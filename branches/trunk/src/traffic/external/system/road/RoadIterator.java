package traffic.external.system.road;

import java.util.Iterator;

import traffic.map.entity.Road;

/**
 * @author Isaac
 * 
 */
public abstract class RoadIterator implements Iterator<Road> {

	@Override
	public abstract boolean hasNext();

	@Override
	public abstract Road next();

	@Override
	public void remove() {
	}

}
