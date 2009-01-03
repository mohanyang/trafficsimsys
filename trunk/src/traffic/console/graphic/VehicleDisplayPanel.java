package traffic.console.graphic;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import traffic.basic.Lib;
import traffic.map.entity.Vehicle;

public class VehicleDisplayPanel extends JPanel {
	public static final long serialVersionUID = 3L;

	public static final int items = 3;
	private static final String[] text = { "position:", "speed:", "lane:" };
	private static final int position = 0, speed = 1, lane = 2;

	private Vehicle vehicle = null;

	private JLabel[] fields = new JLabel[items];
	private JPanel subPanel = null;
	private ImgPanel imgPanel = null;
	private BufferedImage vImg = null;

	public VehicleDisplayPanel() {
		imgPanel = new ImgPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("System Information"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 6.0;
		gridbag.setConstraints(imgPanel, c);
		add(imgPanel);
		subPanel = new JPanel();
		subPanel.setLayout(new GridLayout(items, 2));
		subPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder("Vehicle Information"), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		for (int i = 0; i < items; ++i) {
			subPanel.add(new JLabel(text[i]));
			subPanel.add(fields[i] = new JLabel("N/A"));
		}
		subPanel.validate();
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 2.0;
		gridbag.setConstraints(subPanel, c);
		add(subPanel);
		validate();
		// setBorder(new LineBorder(Color.BLACK));
		setSize(200, 450);
	}

	public void setVehicle(Vehicle v) {
		vehicle = v;
		if (vehicle != null) {
			try {
				vImg = ImageLoader.loadImage(v.getVehicleInf().getImageID());
			} catch (IOException e) {
				vImg = null;
			}
		}
	}

	public void paint() {
		imgPanel.repaint();
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

	private class ImgPanel extends JPanel {
		static public final long serialVersionUID = 133L;
		private static final int width = 180, height = 180;
		private BufferedImage defaultImg = null;

		public ImgPanel() {
			defaultImg = ImageLoader.loadImageByName("start.gif");
			Lib.assertTrue(defaultImg != null);
			setSize(width, height);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (vehicle == null || vImg == null) {
				g.drawImage(defaultImg, (int) (getWidth() / 2 - defaultImg
						.getWidth() / 2), (int) (getHeight() / 2 - defaultImg
						.getHeight() / 2), null);
			} else {
				g.drawImage(vImg, (int) (getWidth() / 2 - vImg.getWidth() / 2),
						(int) (getHeight() / 2 - vImg.getHeight() / 2), null);
			}
		}
	}
}
