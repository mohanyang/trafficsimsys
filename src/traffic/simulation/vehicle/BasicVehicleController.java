package traffic.simulation.vehicle;

import traffic.map.entity.*;
import traffic.basic.Lib;
import java.util.Iterator;

public class BasicVehicleController implements IVehicleControl {
	
	private Vehicle assoc;
	
	public void setVehicle(Vehicle v){
		assoc=v;
	}

	@Override
	public void react() {
		if (assoc==null)
			return;
		Vehicle v=assoc;
		Road curr=v.getRoad();
		if (curr.canMove(v.getLane())){
			// check closest distance
			double temp=curr.closestDistance(v);
			if (temp<0)
				v.setSpeed(0);
			else
			if (temp<v.getSpeed() || Lib.isEqual(v.getSpeed(), 0))
				v.setSpeed(temp);
			
//			else
//				v.setSpeed(v.getSpeed()+1);
			v.proceed();
			
			int count=v.getNextPoint(19).getDegree();
			if (count>0 && Lib.isEqual(v.getPosition()+19, v.getRoad().getLength())){
				Iterator<Road> intitr=v.getNextPoint(19).getRoadList();
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
					v.setRoad(nextRoad,0);
					v.setSpeed(Lib.random(5)+5);
				}
			}
		}
		else {
			// stop policy
		}
	}

}
