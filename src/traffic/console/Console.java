package traffic.console;

/**
 * @author Isaac
 * 
 */
public interface Console {
	public void setInterruptHandlers(Runnable receiveInterruptHandler,
			Runnable sendInterruptHandler);

	public Event read();

	public void write(Event e);
}
