package traffic.console.graphic;

import traffic.console.Console;
import traffic.event.Event;
import traffic.map.entity.Map;

public class GraphicConsole implements Console {
	MapFrame f = null;
	Runnable inputHandler = null;

	public GraphicConsole() {
		f = new MapFrame();
	}

	@Override
	public void setInputHandler(Runnable inputHandler) {
		this.inputHandler = inputHandler; 
	}

	@Override
	public Event read() {
		return null;
	}

	@Override
	public void eventOccured(Event e) {
		if (e.getObj() instanceof Map) {
			switch (e.getType()) {
			case Event.CREATE:
				f.handleCreate((Map) e.getObj());
				f.setVisible(true);
				break;
			case Event.MOVE:
				f.handleMove((Map) e.getObj());
				break;
			}
		}
	}
}
