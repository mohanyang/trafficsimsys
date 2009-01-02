package traffic.simulation.statistics;

import java.util.HashMap;
import java.util.Map;

import traffic.basic.Lib;
import traffic.basic.Scheduler;
import traffic.event.Event;
import traffic.map.entity.Road;

public class Stat implements IStat {

	@Override
	public void eventOccured(Event event) {
		if (event.getType() == Event.ENTER_ROAD) {
			Road road = (Road) event.getObj();
			getInfo(road).current++;
		} else if (event.getType() == Event.LEAVE_ROAD) {
			Road road = (Road) event.getObj();
			getInfo(road).current--;
		}
	}

	@Override
	public double averageVehiclesOnRoad(Road r) {
		return getInfo(r).average;
	}

	@Override
	public int currentVehiclesOnRoad(Road r) {
		return getInfo(r).current;
	}

	@Override
	public void start() {
		Lib.assertTrue(!running);
		running = true;
		Scheduler.getInstance().schedule(updater, UPDATE_TIME);
	}

	@Override
	public void stop() {
		Lib.assertTrue(running);
		running = false;
		infos.clear();
	}

	@Override
	public int accidentsOnRoad(Road r) {
		// TODO unimplemented
		return 0;
	}

	protected StatInfo getInfo(Road r) {
		StatInfo info = infos.get(r);
		if (info == null) {
			info = new StatInfo();
			synchronized (infos) {
				infos.put(r, info);
			}
		}
		return info;
	}

	private boolean running = false;
	private final Map<Road, StatInfo> infos = new HashMap<Road, StatInfo>();

	private Runnable updater = new Runnable() {

		@Override
		public void run() {
			if (running) {
				synchronized (infos) {
					for (StatInfo info : infos.values()) {
						info.update();
					}
				}
				Scheduler.getInstance().schedule(updater, UPDATE_TIME);
			}
		}
	};

	private static final long UPDATE_TIME = 1000;

	protected class StatInfo {
		public static final double alpha = 0.1;

		public int current = 0;
		public double average = 0;

		public void update() {
			average = alpha * current + (1 - alpha) * average;
		}
	}

}
