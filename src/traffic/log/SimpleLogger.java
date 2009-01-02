package traffic.log;

public class SimpleLogger extends Log {

	@Override
	public void write(String msg) {
		System.out.print(msg);
	}

}
