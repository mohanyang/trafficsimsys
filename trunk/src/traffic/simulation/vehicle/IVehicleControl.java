package traffic.simulation.vehicle;

import traffic.event.EventListener;
import traffic.map.entity.Vehicle;

public interface IVehicleControl {
	public void react();

	public void setVehicle(Vehicle v);

	public void addEventListener(EventListener listener);
}
