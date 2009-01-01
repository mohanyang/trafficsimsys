package traffic.simulation.kernel;

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
import traffic.simulation.statistics.IStat;
import traffic.simulation.vehicle.BasicVehicleController;
import traffic.simulation.vehicle.IVehicleControl;

public class Simulator {
	private Map map = null;
	private Console console = null;
	private Runnable simuTask = null;
	private IStat stat = null;

	public Simulator() {
	}

	public void initialize() {
		Config.load("traffic.ini");
		Lib.seedRandom(Config.getInteger("traffic.randomSeed", 0));
		map = new LoadHandler().load();
		System.out.println("Vehicle list");
		for (Iterator<Point> p = map.getPointList(); p.hasNext();)
			for (Iterator<Road> r = p.next().getRoadList(); r.hasNext();)
				for (Iterator<Vehicle> v = r.next().getVehicleList(); v
						.hasNext();)
					System.out.println(v.next());
		System.out.println();
		console = (Console) Lib.constructObject(Config
				.getString("traffic.console"));
		Lib.assertTrue(console != null);
		console.eventOccured(new Event(map, Event.CREATE));
		stat = (IStat) Lib.constructObject(Config.getString(
				"traffic.statistics", "traffic.simulation.statistics.Stat"));
		Lib.assertTrue(stat != null);
	}

	private IVehicleControl getController(Vehicle v) {
		IVehicleControl controller = v.getController();
		if (controller == null) {
			// TODO use something like factory to create controller.
			controller = new BasicVehicleController();
			controller.setVehicle(v);
			v.setController(controller);
			controller.addEventListener(stat);
		}
		return controller;
	}

	public IStat getStat() {
		return stat;
	}

	public void start() {
		if (simuTask != null)
			return;
		simuTask = new Runnable() {
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
					console.eventOccured(new Event(map, Event.MOVE));
					if (simuTask != null) {
						Scheduler.getInstance().schedule(simuTask, 100);
					} else {
						// simulation finished
						stop();
					}
					System.out.println("===finished===");
				}
			}
		};
		simuTask.run();
	}

	private byte lock[] = new byte[0];

	public void stop() {
		simuTask = null;
		stat.stop();
	}
}
