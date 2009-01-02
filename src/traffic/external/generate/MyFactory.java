package traffic.external.generate;

import traffic.basic.Lib;

//@author liangda li

public class MyFactory {
	private VehicleGenerator vehiclegen;
	private BarrierGenerator barriergen;

	private static MyFactory instance = null;

	public static synchronized MyFactory getInstance() {
		if (instance == null) {
			instance = new MyFactory();
		}
		return instance;
	}

	public VehicleGenerator getVehicleGenerator() {
		if (vehiclegen == null) {
			try {
				vehiclegen = (VehicleGenerator) Lib
						.constructObject("traffic.external.generate.VehicleGenerator");
			} catch (Exception e) {
				System.err.print("VehicleGenerator construct error\n");
			}
		}
		return vehiclegen;
	}

	public BarrierGenerator getBarrierGenerator() {
		if (barriergen == null) {
			try {
				barriergen = (BarrierGenerator) Lib
						.constructObject("traffic.external.generate.BarrierGenerator");
			} catch (Exception e) {
				System.err.print("BarrierGenerator construct error\n");
			}
		}
		return barriergen;
	}
}
