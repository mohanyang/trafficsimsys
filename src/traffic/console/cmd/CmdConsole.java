package traffic.console.cmd;

import java.util.concurrent.ArrayBlockingQueue;

import traffic.basic.Scheduler;
import traffic.console.Console;
import traffic.console.Event;

public class CmdConsole implements Console {

	public CmdConsole() {
		receiveInterrupt = new Runnable() {
			public void run() {
				receiveInterrupt();
			}
		};
	}

	@Override
	public void setInterruptHandlers(Runnable receiveInterruptHandler,
			Runnable sendInterruptHandler) {
	}

	@Override
	public Event read() {
		return null;
	}

	@Override
	public void write(Event e) {
		receiveQueue.add(e);
		receiveInterrupt.run();
	}

	private void receiveInterrupt() {
		if (receiveQueue.isEmpty()) {
			scheduleReceiveInterrupt();
		} else {
			System.out.println(receiveQueue.poll());
		}
	}

	private void scheduleReceiveInterrupt() {
		Scheduler.getInstance().schedule(receiveInterrupt, Scheduler.RECEIVE);
	}

	private Runnable receiveInterrupt;
	private ArrayBlockingQueue<Event> receiveQueue = new ArrayBlockingQueue<Event>(
			10);
}
