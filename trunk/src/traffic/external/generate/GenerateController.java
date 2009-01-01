package traffic.external.generate;

import java.util.Iterator;

import traffic.basic.Lib;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;
import traffic.map.entity.VehicleInf;

public class GenerateController {
	public GenerateController(){
	}
	
	public static synchronized GenerateController getInstance(){
		if(instance==null){
			instance=new GenerateController();
		}
		return instance;
	}
	
	public int generateVehicle(){
		Road r=SearchBornPoint(bornpoint);
		if(r==null){
			return -1;
		}
		r.acquireLock();
		Vehicle v = Map.getInstance().newVehicle(new VehicleInf(type,maxspeed,initspeed));
		v.setRoad(r,0);
		v.setPosition(0);
		v.setSpeed(initspeed);
		r.releaseLock();
		return 0;
	}
	
	private Road SearchBornPoint(int index){
		int i=0;
		Point p=null;
		for (Iterator<Point> itr =Map.getInstance().getPointList(); itr.hasNext();) {
			p = itr.next();
			if(i==index){
				break;
			}
			i++;
		}
		if(p==null){
			return null;
		}
		i=0;
		int degree=Map.getInstance().getPoint(p).getDegree();
		int tmpindex=Randomnum(0,degree);
		for (Iterator<Road> itrr = p.getRoadList(); itrr.hasNext();){
			Road r = itrr.next();
			if(i==tmpindex){
				return r;
			}
			i++;
		}
		return null;
	}
	
	public int setbornpoint(int bpoint){
		if(bpoint>=pointnum){
			bpoint=pointnum-1;
			return -1;
		}
		bornpoint=bpoint;
		return 0;
	}
	
	public void setinitspeed(int ispeed){
		initspeed=ispeed;
	}
	
	public void setmaxspeed(int mspeed){
		maxspeed=mspeed;
	}
	
	public int settype(int t){
		if(t>=typenum){
			type=typenum-1;
			return -1;
		}
		type=t;
		return 0;
	}
	
	public int Randomnum(int low, int high){
		return (int)(low+(high-low)*Lib.random());
	}
	 
	private static GenerateController instance=null;
	private int maxspeed=10;
	private int initspeed=10;
	private int type=0;
	private int bornpoint=0;
	private static int typenum=4;
	private static int pointnum=40;
}
