package traffic.basic;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
	Timer timer;

	public Scheduler() {
		timer = new Timer();
	}

	public void schedulder(final Runnable handler, long time) {
		timer.schedule(new TimerTask() {
			public void run() {
				handler.run();
			}
		}, time);
	}

	public static long RECEIVE = 100, SEND = 100;
}
