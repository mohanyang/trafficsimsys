package traffic.simulation.statistics;

import traffic.event.EventListener;
import traffic.map.entity.Road;

public interface IStat extends EventListener {

	public void start();

	public void stop();

	public int currentVehiclesOnRoad(Road r);

	public double averageVehiclesOnRoad(Road r);
	
	public int accidentsOnRoad(Road r);

}
