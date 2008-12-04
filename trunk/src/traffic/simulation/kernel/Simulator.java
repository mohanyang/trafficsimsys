package traffic.simulation.kernel;

import java.util.Iterator;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.basic.Scheduler;
import traffic.console.Console;
import traffic.console.Event;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;
import traffic.map.handler.LoadHandler;
import traffic.simulation.vehicle.*;

public class Simulator {
	private Map map = null;
	private Console console = null;
	private Thread KThread = null;

	public Simulator() {
		Lib.seedRandom(0);
		Config.load("traffic.ini");
		map = new LoadHandler().run();
		System.out.println("Vehicle list");
		for (Iterator<Point> p = map.getPointList(); p.hasNext();)
			for (Iterator<Road> r = p.next().getRoadList(); r.hasNext();)
				for (Iterator<Vehicle> v = r.next().getVehicleList(); v
						.hasNext();)
					System.out.println(v.next());
		System.out.println();
		console = (Console) Lib.constructObject(Config
				.getString("traffic.console"));
		console.write(new Event(map, Event.CREATE));
		start();
	}

	public void start() {
		if (KThread != null)
			return;
		KThread = new Thread(new Runnable() {
			public void run() {
				for (Iterator<Point> pp = map.getPointList(); pp.hasNext();) {
					Point p = pp.next();
					for (Iterator<Road> rr = p.getRoadList(); rr.hasNext();) {
						Road r = rr.next();
						synchronized (r.getVehicleLinkedList()) {
							if (r.getStartPoint().equals(p))
								for (Iterator<Vehicle> itr = r.getVehicleList(); itr
										.hasNext();) {
									Vehicle v = itr.next();
									System.out.println(v);
	//								double x1 = v.getRoad().getStartPoint().getXAxis(), 
	//										y1 = v.getRoad().getStartPoint().getYAxis(), 
	//										x2 = v.getRoad().getEndPoint().getXAxis(), 
	//										y2 = v.getRoad().getEndPoint().getYAxis(), 
	//										x = v.getPoint().getXAxis(), 
	//										y = v.getPoint().getYAxis(), 
	//										l = v.getSpeed();
	//								double tanv = (y2 - y1) / (x2 - x1), 
	//										cosv = 1 / Math.sqrt(1 + tanv * tanv), 
	//										sinv = Math.sqrt(1 - cosv * cosv);
	//								double sy = Math.signum(y2 - y1), 
	//										sx = Math.signum(x2 - x1);
	//								x += l * cosv * sx;
	//								y += l * sinv * sy;
	//								v.setPoint(new Point(x, y));
									// TODO use interface here
									new BasicVehicleController().react(v);
									System.out.println(v);
								}
						}
					}
				}
				console.write(new Event(map, Event.MOVE));
				Scheduler.getInstance().scheduler(KThread, 34);
			}
		});
		KThread.run();
	}

	public void stop() {
		KThread = null;
	}
}
