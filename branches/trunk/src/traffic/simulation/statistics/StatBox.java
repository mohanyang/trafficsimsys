package traffic.simulation.statistics;

import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import traffic.basic.Config;
import traffic.basic.Lib;
import traffic.map.entity.Road;

public abstract class StatBox extends BufferedImage {

	private static StatBox instance = null;
	private Lock lock = new ReentrantLock();

	protected StatBox(int width, int height) {
		super(width, height, TYPE_4BYTE_ABGR);
	}

	public static StatBox getInstance() {
		if (instance == null) {
			instance = (StatBox) Lib.constructObject(Config.getString(
					"traffic.statistics.StatBox",
					"traffic.simulation.statistics.SimpleStatBox"));
			Lib.assertTrue(instance != null);
		}
		return instance;
	}

	public void acquireLock() {
		lock.lock();
	}

	public void releaseLock() {
		lock.unlock();
	}
	
	public abstract int getWidth();
	
	public abstract int getHeight();

	public abstract void prepare(IStat stat, Road road);
}
