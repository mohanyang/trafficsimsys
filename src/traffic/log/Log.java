package traffic.log;

import traffic.basic.Config;
import traffic.basic.Lib;

public abstract class Log {

	private static Log instance = null;

	public static synchronized Log getInstance() {
		if (instance == null) {
			String className = Config.getString("traffic.logClassName",
					"traffic.log.SimpleLogger");
			instance = (Log) Lib.constructObject(className);
			Lib.assertTrue(instance != null);
		}
		return instance;
	}

	public void flush() {
	}
	
	public void close() {
	}

	public void writeln(String msg) {
		write(msg);
		write("\n");
	}

	public void writeln() {
		write("\n");
	}

	public abstract void write(String msg);

}
