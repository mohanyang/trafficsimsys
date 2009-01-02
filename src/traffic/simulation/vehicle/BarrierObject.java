package traffic.simulation.vehicle;

import java.util.LinkedList;

import traffic.basic.Lib;
import traffic.event.Event;
import traffic.event.EventListener;
import traffic.event.EventDispatcher;
import traffic.log.Log;
import traffic.map.entity.Road;
import traffic.map.entity.RoadEntranceInfo;
import traffic.map.entity.Vehicle;

public class BarrierObject extends EventDispatcher implements IVehicleControl {

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
		v.setSpeed(0);
	}

}
