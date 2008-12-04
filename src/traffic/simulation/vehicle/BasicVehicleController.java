package traffic.simulation.vehicle;

import traffic.map.entity.*;
import traffic.basic.Lib;
import java.util.Iterator;

public class BasicVehicleController implements IVehicleControl {

	@Override
	public void react(Vehicle v) {
		Road curr=v.getRoad();
		if (curr.canMove(v.getLane())){
			// check closest distance
			double temp=curr.closestDistance(v);
			if (temp<v.getSpeed())
				v.setSpeed(temp);
			v.proceed();
			
			Iterator<Road> intitr=v.getPoint().getRoadList();
			int count=v.getPoint().getDegree();
		}
		else {
			// stop policy
		}
	}

}
