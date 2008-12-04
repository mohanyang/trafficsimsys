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
			if (temp<v.getSpeed() || Lib.isEqual(v.getSpeed(), 0))
				v.setSpeed(temp);
//			else
//				v.setSpeed(v.getSpeed()+1);
			v.proceed();
			
			int count=v.getPoint().getDegree();
			if (count>0 && Lib.isEqual(v.getPosition()+v.getSpeed(), v.getRoad().getLength())){
				Iterator<Road> intitr=v.getPoint().getRoadList();
				Road nextRoad=intitr.next();
				count=Lib.random(count)-1;
				while (count>0){
					Lib.assertTrue(intitr.hasNext());
					nextRoad=intitr.next();				
					if (nextRoad!=v.getRoad())
						--count;
				}
				System.out.println("+++" + v + " changing road");
				if (nextRoad!=v.getRoad()) {
					v.setRoad(nextRoad);
					v.setSpeed(Lib.random(5)+5);
				}
			}
		}
		else {
			// stop policy
		}
	}

}
