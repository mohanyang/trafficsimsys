package traffic.console.graphic;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import traffic.map.entity.Vehicle;

public class VehicleDisplayPanel extends JPanel {
	public static final long serialVersionUID = 3L;

	public static final int items = 3;
	private static final String[] text = { "position:", "speed:", "lane:" };
	private static final int position = 0, speed = 1, lane = 2;

	private Vehicle vehicle = null;

	private JTextField[] fields = new JTextField[items];

	public VehicleDisplayPanel() {
		setLayout(new GridLayout(1, items * 2));
		for (int i = 0; i < items; ++i) {
			add(new JLabel(text[i]));
			add(fields[i] = new JTextField());
		}
	}

	public void setVehicle(Vehicle v) {
		vehicle = v;
	}

	public void paint() {
		if (vehicle == null) {
			for (int i = 0; i < items; ++i)
				fields[i].setText("N/A");
		} else {
			fields[position].setText((int) vehicle.getPoint().getXAxis() + " "
					+ (int) vehicle.getPoint().getYAxis());
			fields[speed].setText(String.format("%.2f", vehicle.getSpeed()));
			fields[lane].setText("" + vehicle.getLane());
		}
	}
}
