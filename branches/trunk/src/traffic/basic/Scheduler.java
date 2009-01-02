package traffic.basic;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
	static Scheduler instance = null;
	Timer timer;

	private Scheduler() {
		timer = new Timer();
	}

	public static Scheduler getInstance() {
		if (instance == null)
			instance = new Scheduler();
		return instance;
	}

	public void schedule(final Runnable task, long time) {
		timer.schedule(new TimerTask() {
			public void run() {
				task.run();
			}
		}, time);
	}

	public static long RECEIVE = 100, SEND = 100;
}
