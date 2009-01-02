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

	protected Vehicle assoc;

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
			System.out.println("closest distance " + temp);
			if (temp < v.getSpeed() || Lib.isEqual(v.getSpeed(), 0))
				v.setSpeed(temp);

			v.proceed();

			int count;
			System.out.println("position " + v.getPosition() + " " + v.getRoad().getLength());
			if (Lib.isEqual(v.getPosition(), v.getRoad()	.getLength())) {
				LinkedList<RoadEntranceInfo> adj=v.getRoad().getIntersectionList(v.getLane());
				if (adj.size()==0){
					System.out.println("+++" + v + " dying");
					v.removeFromCurrent();
					stop();
				}
				else {
					int abc=adj.size();
					System.out.println("adj list size=" + adj.size());
					count = Lib.random(adj.size());
					System.out.println("count=" + count);
					RoadEntranceInfo target=adj.get(count);
					System.out.println("+++" + v + " changing road to " + target.getRoad() 
							+ " lane " + target.getLane());
					Lib.assertTrue(target.getRoad()!=v.getRoad() || target.getLane()!=v.getLane());
					dispatchEvent(new Event(this, Event.LEAVE_ROAD, v.getRoad()));
					dispatchEvent(new Event(this, Event.ENTER_ROAD, target.getRoad()));
					v.setRoad(target.getRoad(), target.getLane());
					v.setSpeed(Lib.random(15) + 5);
				}
			}
		} else {
			System.out.println("+++" + v + " dying");
			v.removeFromCurrent();
			stop();
		}
	}

}
