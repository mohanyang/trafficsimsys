package traffic.simulation.vehicle;

import java.util.Iterator;
import java.util.LinkedList;

import traffic.basic.Lib;
import traffic.event.Event;
import traffic.log.Log;
import traffic.map.entity.Road;
import traffic.map.entity.RoadEntranceInfo;
import traffic.map.entity.Vehicle;

public class NewBasicVehicleController extends BasicVehicleController {
	
	@Override
	public void react(){
		if (assoc == null)
			return;
		Vehicle v = assoc;
		Road curr = v.getRoad();
		if (curr.canMove(v.getLane())) {
			double newSpeed=v.getSpeed();
			Vehicle closestV=curr.getClosestVehicle(v.getPosition(), v.getLane(), v);
			if (closestV!=null){
				Log.getInstance().writeln("closest vehicle=" + (closestV.getPosition()-v.getPosition()));
				if (newSpeed>closestV.getPosition()-v.getPosition())
					newSpeed=closestV.getPosition()-v.getPosition();
			}
			
			double temp = curr.closestIntersection(v.getPosition(), v.getLane());
			Log.getInstance().writeln("closest intersection=" + temp);
			if (temp<newSpeed)
				newSpeed=temp;
			v.setSpeed(newSpeed);
			v.proceed();

			if (Lib.isEqual(temp, 0)) {
				LinkedList<RoadEntranceInfo> adj = v.getRoad().getIntersectionList(v.getPosition(), v.getLane());
				if (adj.size()>1)
					for (Iterator<RoadEntranceInfo> itr=adj.iterator(); itr.hasNext();){
						RoadEntranceInfo ri=itr.next();
						if (ri.getRoad()==curr && ri.getLane()!=v.getLane()){
							itr.remove();
							break;
						}
					}
				if (adj.size() == 0) {
					Log.getInstance().writeln("+++" + v + " dying");
					v.removeFromCurrent();
					stop();
				} else {
					int abc = adj.size();
					Log.getInstance().writeln("adj list size=" + adj.size());
					int count = Lib.random(adj.size());
					Log.getInstance().writeln("count=" + count);
					RoadEntranceInfo target = adj.get(count);
					Log.getInstance().writeln(
							"+++" + v + " changing road to " + target.getRoad()
									+ " lane " + target.getLane());
//					Lib.assertTrue(target.getRoad() != v.getRoad()
//							|| target.getLane() != v.getLane());
					dispatchEvent(new Event(this, Event.LEAVE_ROAD, v.getRoad()));
					dispatchEvent(new Event(this, Event.ENTER_ROAD, target
							.getRoad()));
					v.setRoad(target.getRoad(), target.getLane(), target.getPosition());
					v.setSpeed(Lib.random(5) + 15);
				}
			}
		} else {
			Log.getInstance().writeln("+++" + v + " dying");
			v.removeFromCurrent();
			stop();
		}		
	}

}
