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
	protected double getNewSpeed(){
		double newSpeed=assoc.getSpeed()-1+Lib.random(5);
		if (newSpeed<0)
			newSpeed=0;
		Vehicle closestV=assoc.getRoad().getClosestVehicle(assoc.getPosition(), assoc.getLane(), assoc);
		if (closestV!=null){
			Log.getInstance().writeln("closest vehicle=" + (closestV.getPosition()-assoc.getPosition()));
			Log.getInstance().writeln("closestv length=" + closestV.getLength());
			Log.getInstance().writeln("currentv length=" + assoc.getLength());
			if (newSpeed>closestV.getPosition()-assoc.getPosition()
					-(closestV.getLength()+assoc.getLength())/2)
				newSpeed=closestV.getPosition()-assoc.getPosition()
						-(closestV.getLength()+assoc.getLength())/2;
			if (newSpeed<0)
				newSpeed=0;
		}
		
		double temp = assoc.getRoad().closestIntersection(assoc.getPosition(), assoc.getLane());
		Log.getInstance().writeln("closest intersection=" + temp);
		if (temp<newSpeed)
			newSpeed=temp;
		return newSpeed;
	}
	
	@Override
	protected boolean isAtIntersection() {
		return Lib.isEqual(
				assoc.getRoad().closestIntersection(assoc.getPosition(), assoc.getLane()), 0);
	}
	
	@Override
	protected LinkedList<RoadEntranceInfo> getEntryList() {
		LinkedList<RoadEntranceInfo> ret
			=assoc.getRoad().getIntersectionList(assoc.getPosition(), assoc.getLane());
		if (ret.size()>1)
			for (Iterator<RoadEntranceInfo> itr=ret.iterator(); itr.hasNext();){
				RoadEntranceInfo ri=itr.next();
				if (ri.getRoad()==assoc.getRoad() 
						&& ri.getRoad().getDirection(ri.getLane())!=assoc.getDirection()){
					Log.getInstance().writeln("removing target: " + ri.getRoad() + " "
							+ ri.getLane());
					itr.remove();
				}
			}
		return ret;
	}
	
	@Override
	protected void performChange(RoadEntranceInfo target) {
		if (target.getClosestDistance()<assoc.getLength())
			return;
		Log.getInstance().writeln(
				"+++" + assoc + " changing road to " + target.getRoad()
						+ " lane " + target.getLane());
		dispatchEvent(new Event(this, Event.LEAVE_ROAD, assoc.getRoad()));
		dispatchEvent(new Event(this, Event.ENTER_ROAD, target
				.getRoad()));
		assoc.setRoad(target.getRoad(), target.getLane(), target.getPosition());
		assoc.setSpeed(Lib.random(5) + 15);
	}
}
