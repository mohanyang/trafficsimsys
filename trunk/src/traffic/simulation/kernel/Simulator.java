package traffic.simulation.kernel;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.console.Console;
import traffic.console.Event;
import traffic.map.entity.Map;
import traffic.map.handler.LoadHandler;

public class Simulator {
	private Map map = null;
	private Console console = null;
	private Thread KThread = null;

	public Simulator() {
		Config.load("traffic.ini");
		map = new LoadHandler().run();
		console = (Console) Lib.constructObject(Config
				.getString("traffic.console"));
		console.write(new Event(map, Event.CREATE));
	}

	public void start() {
		if (KThread != null)
			return;
		KThread = new Thread(new Runnable() {
			public void run() {
			}
		});
	}

	public void stop() {
		KThread = null;
	}
}
