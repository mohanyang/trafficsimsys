package traffic.event;

import java.util.EventObject;

public class Event extends EventObject {

	private static final long serialVersionUID = 1L;

	private Object o;
	private int type;

	@Deprecated
	public Event(Object o, int type) {
		super(0);
		this.o = o;
		this.type = type;
	}

	public Event(Object source, int type, Object obj) {
		super(source);
		this.type = type;
		this.o = obj;
	}

	@Override
	public Object getSource() {
		return super.getSource();
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

	public static final int CREATE = 0, MOVE = 1, ENTER_ROAD = 2,
			LEAVE_ROAD = 3;
}
