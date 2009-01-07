package traffic.simulation.vehicle;

/**
 * 
 * @author huangsx
 */

import java.util.LinkedList;

import traffic.basic.Lib;
import traffic.event.Event;
import traffic.event.EventDispatcher;
import traffic.log.Log;
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
	
	protected double getNewSpeed() {
		double temp = assoc.getRoad().closestDistance(assoc);
		Log.getInstance().writeln("closest distance " + temp);
		if (temp < assoc.getSpeed() || Lib.isEqual(assoc.getSpeed(), 0))
			return temp;
		return assoc.getSpeed();
	}
	
	protected boolean isAtIntersection() {
		return Lib.isEqual(assoc.getPosition(), assoc.getRoad().getLength());
	}
	
	protected LinkedList<RoadEntranceInfo> getEntryList(){
		return assoc.getRoad().getIntersectionList(assoc.getLane());	
	}
	
	protected void performChange(RoadEntranceInfo target) {
		Log.getInstance().writeln(
				"+++" + assoc + " changing road to " + target.getRoad()
						+ " lane " + target.getLane());
		Lib.assertTrue(target.getRoad() != assoc.getRoad()
				|| target.getLane() != assoc.getLane());
		dispatchEvent(new Event(this, Event.LEAVE_ROAD, assoc.getRoad()));
		dispatchEvent(new Event(this, Event.ENTER_ROAD, target
				.getRoad()));
		assoc.setRoad(target.getRoad(), target.getLane());
		assoc.setSpeed(Lib.random(15) + 5);
	}
	
	@Override
	public void react() {
		if (assoc == null)
			return;
		Vehicle v = assoc;
		Road curr = v.getRoad();
		if (curr.canMove(v.getLane())) {
			v.setSpeed(getNewSpeed());
			v.proceed();

			int count;
			Log.getInstance().writeln(
					"position " + v.getPosition() + " "
							+ v.getRoad().getLength());
			if (isAtIntersection()) {
				LinkedList<RoadEntranceInfo> adj = getEntryList();
				if (adj.size() == 0) {
					Log.getInstance().writeln("+++" + v + " dying");
					v.removeFromCurrent();
					stop();
				} else {
					Log.getInstance().writeln("adj list size=" + adj.size());
					count = Lib.random(adj.size());
					Log.getInstance().writeln("count=" + count);
					RoadEntranceInfo target = adj.get(count);
					performChange(target);
				}
			}
		} else {
			Log.getInstance().writeln("+++" + v + " dying");
			v.removeFromCurrent();
			stop();
		}
	}

}
