package traffic.simulation.kernel;

import java.util.HashMap;
import java.util.Iterator;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.basic.Scheduler;
import traffic.console.Console;
import traffic.event.Event;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;
import traffic.map.handler.LoadHandler;
import traffic.simulation.vehicle.BasicVehicleController;
import traffic.simulation.vehicle.IVehicleControl;

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
	}

	private IVehicleControl getController(Vehicle v) {
		IVehicleControl controller = v.getController();
		if (controller == null) {
			// TODO use something like factory to create controller.
			controller = new BasicVehicleController();
			controller.setVehicle(v);
			v.setController(controller);
		}
		return controller;
	}

	public void start() {
		if (KThread != null)
			return;
		KThread = new Thread(new Runnable() {
			public void run() {
				synchronized (lock) {
					System.out.println("+++starting+++");
					for (Iterator<Point> pp = map.getPointList(); pp.hasNext();) {
						Point p = pp.next();
						for (Iterator<Road> rr = p.getRoadList(); rr.hasNext();) {
							Road r = rr.next();
							r.acquireLock();
							if (r.getStartPoint().equals(p))
								for (Iterator<Vehicle> itr = r.getVehicleList(); itr
										.hasNext();) {
									Vehicle v = itr.next();
									System.out.println(v);
									getController(v).react();
									System.out.println(v);
								}
							r.performRemoval();
							r.releaseLock();
						}
					}
					console.write(new Event(map, Event.MOVE));
					Scheduler.getInstance().schedule(KThread, 100);
					System.out.println("===finished===");
				}
			}
		});
		KThread.run();
	}

	private byte lock[] = new byte[0];

	public void stop() {
		KThread = null;
	}
}
