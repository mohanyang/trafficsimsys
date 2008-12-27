package traffic.event;

/**
 * @author Isaac
 * 
 */
public class Event {
	private Object o;
	private int type;

	public Event(Object o, int type) {
		this.o = o;
		this.type = type;
	}

	public Object getObj() {
		return o;
	}

	public int getType() {
		return type;
	}

	public String toString() {
		return "Event-" + type + " " + o.toString();
	}

	public static final int CREATE = 0, MOVE = 1;
}
