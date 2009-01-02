package traffic.simulation.statistics;

import java.awt.Color;
import java.awt.Graphics2D;

import traffic.map.entity.Road;

public class SimpleStatBox extends StatBox {

	private static final int width = 100;
	private static final int height = 20;
	private Graphics2D graph = null;

	public SimpleStatBox() {
		super(width, height);
		graph = createGraphics();
	}

	@Override
	public void prepare(IStat stat, Road road) {
		graph.setColor(Color.WHITE);
		graph.fillRect(0, 0, width, height);
		graph.setColor(Color.RED);
		graph.drawString(
				"Vehicles : " + stat.currentVehiclesOnRoad(road) + ".", 1, 10);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}
}
