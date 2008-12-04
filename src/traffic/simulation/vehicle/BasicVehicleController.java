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
			
			int count=v.getPoint().getDegree();
			if (count>0 && v.getPoint().equals(v.getRoad().getEndPoint())){
				Iterator<Road> intitr=v.getPoint().getRoadList();
				Road nextRoad=intitr.next();
				count=Lib.random(count);
				while (count>0){
					Lib.assertTrue(intitr.hasNext());
					nextRoad=intitr.next();				
					--count;
				}
				System.out.println("+++" + v + " changing road");
				v.setRoad(nextRoad);
				v.setSpeed(Lib.random(10));
			}
		}
		else {
			// stop policy
		}
	}

}
