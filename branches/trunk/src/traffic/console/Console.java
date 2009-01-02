package traffic.console;

import traffic.event.EventListener;

public interface Console extends EventListener {

	public void setInputHandlers(Runnable inputHandler);

}
