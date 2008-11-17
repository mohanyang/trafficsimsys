package traffic.basic;

public class Lib {
	private final double epsilon = 1e-7;

	public static boolean isEqual(double a, double b) {
		return Math.abs(a - b) < epsilon;
	}
}
