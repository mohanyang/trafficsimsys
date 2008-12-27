package traffic.console.graphic;

import traffic.console.Console;
import traffic.event.Event;
import traffic.map.entity.Map;

public class GraphicConsole implements Console {
	MapFrame f = null;

	public GraphicConsole() {
		f = new MapFrame();
	}

	@Override
	public Event read() {
		return null;
	}

	@Override
	public void setInterruptHandlers(Runnable receiveInterruptHandler,
			Runnable sendInterruptHandler) {
	}

	@Override
	public void write(Event e) {
		if (e.getObj() instanceof Map) {
			switch (e.getType()) {
			case Event.CREATE:
				f.handleCreate((Map) e.getObj());
				f.setSize(1024, 768);
				f.pack();
				f.setVisible(true);
				break;
			case Event.MOVE:
				f.handleMove((Map) e.getObj());
				f.setSize(1024, 768);
				break;
			}
		}
	}
}
