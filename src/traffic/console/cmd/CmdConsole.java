package traffic.console.cmd;

import traffic.basic.Scheduler;
import traffic.console.Console;
import traffic.console.Event;

public class CmdConsole implements Console {

	Scheduler scheduler = null;

	public CmdConsole(Scheduler s) {
		scheduler = s;
		receiveInterrupt = new Runnable() {
			public void run() {
				receiveInterrupt();
			}
		};
		sendInterrupt = new Runnable() {
			public void run() {
				sendInterrupt();
			}
		};
	}

	@Override
	public void setInterruptHandlers(Runnable receiveInterruptHandler,
			Runnable sendInterruptHandler) {
		this.receiveInterruptHandler = receiveInterruptHandler;
		this.sendInterruptHandler = sendInterruptHandler;
	}

	@Override
	public Event read() {
		if (incomingKey != -1) {
			incomingKey = -1;
			scheduleReceiveInterrupt();
		}
		return null;
	}

	@Override
	public void write(Event e) {
		if (outgoingKey == -1)
			scheduleSendInterrupt();

		outgoingKey = value & 0xFF;
	}

	private void receiveInterrupt() {
		if (receiveInterruptHandler != null) {
			receiveInterruptHandler.run();
		}
	}

	private void sendInterrupt() {
		if (sendInterruptHandler != null) {
			sendInterruptHandler.run();
		}
	}

	private void scheduleReceiveInterrupt() {
		scheduler.schedulder(receiveInterrupt, Scheduler.RECEIVE);
	}

	private void scheduleSendInterrupt() {
		scheduler.schedulder(sendInterrupt, Scheduler.SEND);
	}

	private Runnable receiveInterrupt;
	private Runnable sendInterrupt;

	private Runnable receiveInterruptHandler = null;
	private Runnable sendInterruptHandler = null;
	private int incomingKey = -1;
	private int outgoingKey = -1;
}
