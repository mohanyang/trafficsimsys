package traffic.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import traffic.basic.Config;

public class BufferedFileLogger extends Log {

	private static final int BUFFER_SIZE = 262144;

	Writer writer = null;

	public BufferedFileLogger() {
		String filename = Config.getString("traffic.log.logFileName",
				"traffic.log");
		File file = new File(filename);
		try {
			writer = new BufferedWriter(new FileWriter(file), BUFFER_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(String msg) {
		try {
			writer.write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void flush() {
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
