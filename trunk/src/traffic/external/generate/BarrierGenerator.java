package traffic.external.generate;

import java.util.Iterator;
import java.util.LinkedList;

import traffic.basic.Lib;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.RoadInfo;
import traffic.map.entity.Vehicle;
import traffic.map.entity.VehicleInf;
import traffic.simulation.vehicle.BarrierObject;

public class BarrierGenerator implements GenerateController{
	public BarrierGenerator(){
	}
	
	public int generate() {
		if(road==null){
			road = SearchBornPoint(bornpoint);
			info=new RoadInfo(road, 0, 0);
		}
		if (road == null) {
			return -1;
		}
		Vehicle v = Map.getInstance().newVehicle(
				new VehicleInf(type, 0, 0));
		v.setRoad(road, info.getCurrentLane());
		v.setPosition(info.getCurrentPosition());
		v.setSpeed(0);
		BarrierObject o=new BarrierObject();
		o.setVehicle(v);
		v.setController(o);
		road.acquireLock();
		road.performInsertion();
		road.releaseLock();
		road=null;
		return 0;
	}

	private Road SearchBornPoint(int index) {
		int i = 0;
		Point p = null;
		for (Iterator<Point> itr = Map.getInstance().getPointList(); itr
				.hasNext();) {
			p = itr.next();
			if (i == index) {
				break;
			}
			i++;
		}
		if (p == null) {
			return null;
		}
		i = 0;
		int degree = Map.getInstance().getPoint(p).getDegree();
		int tmpindex = Randomnum(0, degree);
		for (Iterator<Road> itrr = p.getRoadList(); itrr.hasNext();) {
			Road r = itrr.next();
			if (i == tmpindex) {
				return r;
			}
			i++;
		}
		return null;
	}
	
	public int setroad(Road r){
		if(r==null){
			return -1;
		}
		road=r;
		return 0;
	}
	
	public int setbornpoint(int bpoint) {
		if (bpoint >= pointnum) {
			bpoint = pointnum - 1;
			return -1;
		}
		bornpoint = bpoint;
		return 0;
	}
	
	public Object getparameter(int index){
		switch(index){
		case 0:
			return bornpoint;
		default:
			System.out.print("error parameter\n");
			return -1;
		}
	}
	
	public int setparameter(Object o, int index){
		switch(index){
		case 0:
			if (o instanceof Integer) {
				return setbornpoint((Integer) o);
			}
			break;
		default:
			System.out.print("error parameter\n");
			return -1;
		}
		return -1;
	}
	
	public int setallparameter(LinkedList list){
		int size=list.size();
		if(size!=paranum){
			System.out.print("wrong parameter number\n");
			return -1;
		}
		for(int i=0;i<size;i++){
			if(setparameter(list.removeFirst(),i)<0){
				return -1;
			}
		}		
		return 0;
	}

	public int Randomnum(int low, int high) {
		return (int) (low + (high - low) * Lib.random());
	}
	
	 
	private RoadInfo info = null;
	
	private static int paranum=1;
	private static int typenum = 4;
	private static int pointnum = 40;
	
	private Road road=null;
	private int bornpoint = 0;
	private int type=4;
}
