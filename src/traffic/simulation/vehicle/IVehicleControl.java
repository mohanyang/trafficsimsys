package traffic.simulation.vehicle;

/**
 * 
 * @author huangsx
 */

import traffic.event.EventListener;
import traffic.map.entity.Vehicle;

public interface IVehicleControl {
	
	public void start();
	
	public void stop();
	
	public void react();

	public void setVehicle(Vehicle v);

	public void addEventListener(EventListener listener);
}
