package traffic.simulation.vehicle;

import traffic.map.entity.*;

public class BasicVehicleController implements IVehicleControl {

	@Override
	public void react(Vehicle v) {
		Road curr=v.getRoad();
		if (curr.canMove(v.getLane())){
			
		}
		else {
			// stop policy
		}
	}

}
