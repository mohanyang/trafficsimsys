package traffic.external.generate;

import java.util.Iterator;
import java.util.LinkedList;

import traffic.basic.Lib;
import traffic.console.graphic.ImageLoader;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.RoadInfo;
import traffic.map.entity.Vehicle;
import traffic.map.entity.VehicleInf;

// @author liangda li

public class VehicleGenerator implements GenerateController {
	public VehicleGenerator() {
		typenum = ImageLoader.getInstance().count;
	}

	public int generate() {
		if (road == null) {
			road = SearchBornPoint(bornpoint);
			if(info==null){
				info=new RoadInfo(road, 0, 0);
			}
		}
		if (road == null) {
			return -1;
		}
		Vehicle v = Map.getInstance().newVehicle(
				new VehicleInf(type, maxspeed, initspeed));
		v.setRoad(road, info.getCurrentLane());
		v.setPosition(info.getCurrentPosition());
		v.setSpeed(initspeed);
		road.acquireLock();
		road.performInsertion();
		road.releaseLock();
		road = null;
		info=null;
		return 0;
	}
	
	public int getTypeCount(){
		return typenum;
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
/*		Road r= Map.getInstance().getRoad(p.getXAxis(), p.getYAxis());
		if (r != null) {
			RoadInfo info = r.getInfoByPoint(new Point(p.getXAxis(), p.getYAxis()));
			VehicleGenerator vg = (VehicleGenerator)MyFactory.getInstance().getGenerator("VehicleGenerator");

			vg.settype(Lib.random(vg.getTypeCount()));
			((VehicleGenerator)MyFactory.getInstance().getGenerator("VehicleGenerator")).setroad(
					r, info);
			((VehicleGenerator)MyFactory.getInstance().getGenerator("VehicleGenerator"))
					.generate();
		}
		return null;*/
		int degree = Map.getInstance().getPoint(p).getDegree();
		int tmpindex = Randomnum(0, degree);
		for (Iterator<RoadInfo> itrr = p.gettmpRoadList(); itrr.hasNext();) {
			RoadInfo r = itrr.next();
			if (i == tmpindex) {
				this.info=r;
				return r.getCurrentRoad();
			}
			i++;
		}
		return null;
	}

	public int setroad(Road r, RoadInfo info) {
		if (r == null) {
			return -1;
		}
		road = r;
		this.info = info;
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

	public int setinitspeed(int ispeed) {
		if (ispeed > defaultmaxspeed) {
			return -1;
		}
		initspeed = ispeed;
		return 0;
	}

	public int setmaxspeed(int mspeed) {
		if (mspeed > defaultmaxspeed) {
			return -1;
		}
		maxspeed = mspeed;
		return 0;
	}

	public int settype(int t) {
		if (t >= typenum) {
			type = typenum - 1;
			return -1;
		}
		type = t;
		return 0;
	}

	public int setparameter(Object o, int index) {
		switch (index) {
		case 0:
			if (o instanceof Integer) {
				return setmaxspeed((Integer) o);
			}
			break;
		case 1:
			if (o instanceof Integer) {
				return setinitspeed((Integer) o);
			}
			break;
		case 2:
			if (o instanceof Integer) {
				return settype((Integer) o);
			}
			break;
		case 3:
			if (o instanceof Integer) {
				return setbornpoint((Integer) o);
			}
			break;
		default:
			System.err.print("error parameter\n");
			return -1;
		}
		System.err.print("error parameter type\n");
		return -1;
	}

	public int setallparameter(LinkedList list) {
		int size = list.size();
		if (size != paranum) {
			System.err.print("wrong parameter number\n");
			return -1;
		}
		for (int i = 0; i < size; i++) {
			if (setparameter(list.removeFirst(), i) < 0) {
				return -1;
			}
		}
		return 0;
	}

	public Object getparameter(int index) {
		switch (index) {
		case 0:
			return maxspeed;
		case 1:
			return initspeed;
		case 2:
			return type;
		case 3:
			return bornpoint;
		default:
			System.err.print("error parameter\n");
			return -1;
		}
	}

	public int Randomnum(int low, int high) {
		return (int) (low + (high - low) * Lib.random());
	}


	private static int defaultmaxspeed = Vehicle.maxForwardSpeed;
	private int maxspeed = 10;
	private int initspeed = 10;
	private int type = 0;
	private int bornpoint = 0;
	private RoadInfo info = null;
	private Road road = null;
	private int typenum = 4;
	private int pointnum = 40;
	private int paranum = 4;
}