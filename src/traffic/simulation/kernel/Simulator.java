package traffic.simulation.kernel;

import java.util.Iterator;
import java.util.TimerTask;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.basic.Scheduler;
import traffic.console.Console;
import traffic.console.graphic.SplashWindow;
import traffic.event.Event;
import traffic.log.Log;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;
import traffic.map.handler.LoadHandler;
import traffic.simulation.statistics.IStat;
import traffic.simulation.vehicle.ConservativeVehicleController;
import traffic.simulation.vehicle.IVehicleControl;

public class Simulator {
	private Map map = null;
	private Console console = null;
	private Runnable simuTask = null;
	private IStat stat = null;
	private SplashWindow sWin = null;

	private static Simulator instance = null;

	public Simulator() {
	}

	public static synchronized Simulator getInstance() {
		if (instance == null) {
			instance = new Simulator();
		}
		return instance;
	}

	public void initialize() {
		sWin = new SplashWindow();
		sWin.setRatio(0.2);
		Config.load("traffic.ini");
		Lib.seedRandom(Config.getInteger("traffic.randomSeed", (int) System
				.currentTimeMillis()));
		sWin.setRatio(0.4);
		map = new LoadHandler().load();
		sWin.setRatio(0.6);
		Log.getInstance().writeln("Point list");
		for (Iterator<Point> p = map.getPointList(); p.hasNext();)
			System.out.println(p.next().toString());
		Log.getInstance().writeln("Vehicle list");
		sWin.setRatio(0.8);
		for (Iterator<Point> p = map.getPointList(); p.hasNext();)
			for (Iterator<Road> r = p.next().getRoadList(); r.hasNext();)
				for (Iterator<Vehicle> v = r.next().getVehicleList(); v
						.hasNext();)
					Log.getInstance().writeln(v.next().toString());
		Log.getInstance().writeln();
		sWin.setRatio(1);
		console = (Console) Lib.constructObject(Config
				.getString("traffic.console"));
		Lib.assertTrue(console != null);
		console.eventOccured(new Event(map, Event.CREATE));
		stat = (IStat) Lib.constructObject(Config.getString(
				"traffic.statistics", "traffic.simulation.statistics.Stat"));
		Lib.assertTrue(stat != null);
		stat.start();
	}

	private IVehicleControl getController(Vehicle v) {
		IVehicleControl controller = v.getController();
		if (controller == null) {
			// TODO use something like factory to create controller.
			controller = new ConservativeVehicleController();
			controller.setVehicle(v);
			v.setController(controller);
			controller.addEventListener(stat);
			controller.start();
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
					Log.getInstance().writeln("+++starting+++");
					for (Iterator<Point> pp = map.getPointList(); pp.hasNext();) {
						Point p = pp.next();
						for (Iterator<Road> rr = p.getRoadList(); rr.hasNext();) {
							Road r = rr.next();
							r.acquireLock();
							if (r.getStartPoint().equals(p))
								for (Iterator<Vehicle> itr = r.getVehicleList(); itr
										.hasNext();) {
									Vehicle v = itr.next();
									Log.getInstance().writeln(v.toString());
									getController(v).react();
									Log.getInstance().writeln(v.toString());
								}
							r.flushQueue();
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
					Log.getInstance().writeln("===finished===");
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
