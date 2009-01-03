package traffic.console;

import traffic.event.Event;
import traffic.event.EventListener;

public interface Console extends EventListener {

	public void setInputHandler(Runnable inputHandler);
	
	public Event read();

}
