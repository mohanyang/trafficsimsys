package traffic.external.generate;

import java.util.Hashtable;

import traffic.basic.Lib;

//@author liangda li

public class MyFactory {
	private Hashtable<String, GenerateController> gentable=new Hashtable<String, GenerateController>();

	private static MyFactory instance = null;

	public static synchronized MyFactory getInstance() {
		if (instance == null) {
			instance = new MyFactory();
		}
		return instance;
	}

	public GenerateController getGenerator(String name) {
		if (gentable.get(name) == null) {
			try {
				GenerateController tmp = (VehicleGenerator) Lib
						.constructObject("traffic.external.generate."+name);
				gentable.put(name, tmp);
				return tmp;
			} catch (Exception e) {
				System.err.print("VehicleGenerator construct error\n");
			}
		}
		return gentable.get(name);
	}

}
