package traffic.console.graphic;

import traffic.basic.Lib;
import traffic.console.Console;
import traffic.event.Event;
import traffic.map.entity.Map;

public class GraphicConsole implements Console {
	MapFrame f = null;
	Runnable inputHandler = null;
	Event event = null;

	public GraphicConsole() {
		f = new MapFrame();
		f.addEventListener(this);
	}

	@Override
	public void setInputHandler(Runnable inputHandler) {
		this.inputHandler = inputHandler;
	}

	@Override
	public Event read() {
		try {
			return event;
		} finally {
			event = null;
		}
	}

	@Override
	public void eventOccured(Event e) {
		switch (e.getType()) {
		case Event.CREATE:
			f.handleCreate((Map) e.getObj());
			f.setVisible(true);
			break;
		case Event.MOVE:
			f.handleMove((Map) e.getObj());
			break;
		case Event.MOUSE_INPUT:
			event = e;
			inputHandler.run();
			break;
		default:
			Lib.assertNotReached("illegal event type");
		}
	}
}
