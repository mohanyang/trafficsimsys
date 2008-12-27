package traffic.event;

import java.util.LinkedList;
import java.util.List;

/**
 * Classes that dispatches events will extend this class or use an instance of
 * this class as a member.
 * 
 * @author xrchen
 * 
 */
public class EventDispatcher {

	private List<EventListener> listeners = new LinkedList<EventListener>();

	public void addEventListener(EventListener listener) {
		listeners.add(listener);
	}

	public void removeEventListener(EventListener listener) {
		listeners.remove(listener);
	}

	public void dispatchEvent(Event e) {
		for (EventListener listener : listeners)
			listener.eventOccured(e);
	}
}
