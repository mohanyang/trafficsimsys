package traffic.console;

public interface Console {
	public void setInterruptHandlers(Runnable receiveInterruptHandler,
			Runnable sendInterruptHandler);

	public Object read();

	public void write(Object o);
}
