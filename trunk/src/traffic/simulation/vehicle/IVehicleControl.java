package traffic.simulation.vehicle;

import traffic.map.entity.Vehicle;

public interface IVehicleControl {
	public void react();

	public void setVehicle(Vehicle v);
}
