package traffic.map.entity;

/**
 * @author Isaac
 * 
 */
public class Vehicle {
	protected Road road;
	protected double speed;
	protected Point current;
	private VehicleInf inf;

	private int id = 0;
	private static int count = 0;

	protected Vehicle(VehicleInf inf) {
		id = count++;
		this.inf = inf;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vehicle))
			return false;
		return ((Vehicle) obj).id == id;
	}

	@Override
	public String toString() {
		return "Vehicle " + id + " on " + road + " at speed of " + speed
				+ " current at " + current;
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road r) {
		if (road != null)
			road.removeVehicle(this);
		r.addVehicle(this);
	}

	public Point getPoint() {
		return current;
	}

	public void setPoint(Point p) {
		current = p;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public VehicleInf getVehicleInf() {
		return inf;
	}
}
