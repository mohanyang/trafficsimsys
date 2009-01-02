package traffic.console.graphic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import traffic.basic.Lib;
import traffic.map.entity.Road;
import traffic.simulation.statistics.IStat;
import traffic.simulation.statistics.StatBox;

public class NormalStatBox extends StatBox {

	private static final int width = 160;
	private static final int height = 75;
	private Graphics2D graph = null;
	private BufferedImage bg = null;
	private static final Color textColor = Color.RED;

	public NormalStatBox() {
		super(width, height);
		graph = createGraphics();
		bg = ImageLoader.loadImageByName("infobg.png");
		Lib.assertTrue(bg != null);
		Lib.alphaImage(bg, 0x70);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void prepare(IStat stat, Road road) {
		graph.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,
				0.0f));
		graph.fillRect(0, 0, width, height);
		graph.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER,
				1.0f));
		AffineTransform trans = new AffineTransform();
		trans.setToScale(0.35, 0.35);
		BufferedImageOp op = new AffineTransformOp(trans,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		graph.drawImage(bg, op, 0, 0);
		graph.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		graph.setColor(textColor);
		graph.drawString("Vehicles  : " + stat.currentVehiclesOnRoad(road), 12,
				20);
		graph.drawString("Average   : "
				+ String.format("%.2f", stat.averageVehiclesOnRoad(road)), 12,
				35);
		graph.drawString("Accidents : " + stat.accidentsOnRoad(road), 12, 50);
	}
}
