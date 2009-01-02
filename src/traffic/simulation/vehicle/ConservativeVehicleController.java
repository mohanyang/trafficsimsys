package traffic.simulation.vehicle;

import java.util.LinkedList;

import traffic.basic.Lib;
import traffic.event.Event;
import traffic.event.EventListener;
import traffic.log.Log;
import traffic.map.entity.Road;
import traffic.map.entity.RoadEntranceInfo;
import traffic.map.entity.Vehicle;

public class ConservativeVehicleController extends BasicVehicleController 
	implements IVehicleControl {
	
	@Override
	public void react() {
		if (assoc == null)
			return;
		Vehicle v = assoc;
		Road curr = v.getRoad();
		if (curr.canMove(v.getLane())) {
			// check closest distance
			double temp = curr.closestDistance(v);
			Log.getInstance().writeln("closest distance " + temp);
			if (temp-v.getLength() < v.getSpeed()) {
				if (v.getPosition()+temp<curr.getLength()-1e-3)
					v.setSpeed(0);
				else
					v.setSpeed(5);
			}
			else if (Lib.isEqual(v.getSpeed(), 0))
				v.setSpeed(temp/2);
			else
				v.setSpeed(10+Lib.random(10));

			v.proceed();

			int count;
			Log.getInstance().writeln("position " + v.getPosition() + " " + v.getRoad().getLength());
			if (Lib.isEqual(v.getPosition(), v.getRoad()	.getLength())) {
				LinkedList<RoadEntranceInfo> adj=v.getRoad().getIntersectionList(v.getLane());
				if (adj.size()==0){
					Log.getInstance().writeln("+++" + v + " dying");
					v.removeFromCurrent();
					stop();
				}
				else {
					int abc=adj.size();
					Log.getInstance().writeln("adj list size=" + adj.size());
					count = Lib.random(adj.size());
					Log.getInstance().writeln("count=" + count);
					RoadEntranceInfo target=adj.get(count);
					Log.getInstance().writeln("+++" + v + " changing road to " + target.getRoad() 
							+ " lane " + target.getLane());
					Lib.assertTrue(target.getRoad()!=v.getRoad() || target.getLane()!=v.getLane());
					dispatchEvent(new Event(this, Event.LEAVE_ROAD, v.getRoad()));
					dispatchEvent(new Event(this, Event.ENTER_ROAD, target.getRoad()));
					v.setRoad(target.getRoad(), target.getLane());
					v.setSpeed(Lib.random(15) + 5);
				}
			}
		} else {
			Log.getInstance().writeln("+++" + v + " dying");
			v.removeFromCurrent();
			stop();
		}
	}

}
