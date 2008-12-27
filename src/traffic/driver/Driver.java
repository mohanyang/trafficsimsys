package traffic.driver;

import traffic.simulation.kernel.Simulator;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Simulator simulator = new Simulator();
			simulator.initialize();
			simulator.start();
		} catch (Exception e) {
			System.out.println("Error in simulator");
		}
	}
}
