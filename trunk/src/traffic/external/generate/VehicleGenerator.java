package traffic.external.generate;

import java.util.Iterator;
import java.util.LinkedList;

import traffic.basic.Lib;
import traffic.console.graphic.ImageLoader;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;
import traffic.map.entity.VehicleInf;

// @author liangda li

public class VehicleGenerator implements GenerateController {
	public VehicleGenerator() {
	}

	public static synchronized VehicleGenerator getInstance() {
		if (instance == null) {
			instance = new VehicleGenerator();
			typenum = ImageLoader.getInstance().count;
		}
		return instance;
	}

	public int generate() {
		if(road==null){
			road = SearchBornPoint(bornpoint);
		}
		if (road == null) {
			return -1;
		}
		Vehicle v = Map.getInstance().newVehicle(
				new VehicleInf(type, maxspeed, initspeed));
		v.setRoad(road, 0);
		v.setPosition(v.getLength() / 2);
		v.setSpeed(initspeed);
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

	private static VehicleGenerator instance = null;
	private static int defaultmaxspeed = 100;
	private int maxspeed = 10;
	private int initspeed = 10;
	private int type = 0;
	private int bornpoint = 0;
	private Road road=null;
	private static int typenum = 4;
	private static int pointnum = 40;
	private static int paranum = 4;
}