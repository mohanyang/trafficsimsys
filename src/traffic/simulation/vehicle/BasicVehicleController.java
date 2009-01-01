package traffic.simulation.vehicle;

/**
 * 
 * @author huangsx
 */

import java.util.LinkedList;

import traffic.basic.Lib;
import traffic.event.Event;
import traffic.event.EventDispatcher;
import traffic.map.entity.Road;
import traffic.map.entity.RoadEntranceInfo;
import traffic.map.entity.Vehicle;

public class BasicVehicleController extends EventDispatcher implements
		IVehicleControl {

	private Vehicle assoc;

	@Override
	public void setVehicle(Vehicle v) {
		assoc = v;
	}
	
	@Override
	public void start() {
		Lib.assertTrue(assoc != null);
		dispatchEvent(new Event(this, Event.ENTER_ROAD, assoc.getRoad()));
	}
	
	@Override
	public void stop() {
		dispatchEvent(new Event(this, Event.LEAVE_ROAD, assoc.getRoad()));
	}

	@Override
	public void react() {
		if (assoc == null)
			return;
		Vehicle v = assoc;
		Road curr = v.getRoad();
		if (curr.canMove(v.getLane())) {
			// check closest distance
			double temp = curr.closestDistance(v);
			if (temp < 0)
				v.setSpeed(0);
			else if (temp < v.getSpeed() || Lib.isEqual(v.getSpeed(), 0))
				v.setSpeed(temp);

			// else
			// v.setSpeed(v.getSpeed()+1);
			v.proceed();

			int count = v.getNextPoint(19).getDegree();
			if (Lib.isEqual(v.getPosition() + 19, v.getRoad()
							.getLength())) {
				LinkedList<RoadEntranceInfo> adj=v.getRoad().getIntersectionList(v.getLane());
				if (adj.size()==0){
					
				}
				else {
					count = Lib.random(adj.size()) - 1;
					RoadEntranceInfo target=adj.get(count);
					System.out.println("+++" + v + " changing road");
					Lib.assertNotReached();
					dispatchEvent(new Event(this, Event.LEAVE_ROAD, v.getRoad()));
					dispatchEvent(new Event(this, Event.ENTER_ROAD, target.getRoad()));
					v.setRoad(target.getRoad(), target.getLane());
					v.setSpeed(Lib.random(5) + 5);
				}
			}
		} else {
			System.out.println("+++" + v + " dying");
			dispatchEvent(new Event(this, Event.LEAVE_ROAD, v.getRoad()));
		}
	}

}
