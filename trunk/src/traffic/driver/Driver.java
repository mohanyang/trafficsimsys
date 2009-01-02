package traffic.driver;

import traffic.simulation.kernel.Simulator;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Simulator simulator = Simulator.getInstance();
			simulator.initialize();
			simulator.start();
		} catch (Exception e) {
			System.err.println("Error in simulator");
		}
	}
}
