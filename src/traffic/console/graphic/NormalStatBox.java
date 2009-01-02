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

	public NormalStatBox() {
		super(width, height);
		graph = createGraphics();
		// bg = ImageLoader.loadImageByName("infobg.gif");
		bg = ImageLoader.loadImageByName("infobg.png");
		Lib.assertTrue(bg != null);
		Lib.alphaImg(bg, 0x70);
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
		// trans.setToScale(0.25, 0.25);
		trans.setToScale(0.35, 0.35);
		BufferedImageOp op = new AffineTransformOp(trans,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		graph.drawImage(bg, op, 0, 0);
		graph.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
		graph.setColor(Color.BLACK);
		graph.drawString("Vehicles  : " + stat.currentVehiclesOnRoad(road)
				+ ".", 15, 20);
		graph.drawString(
				"Average   : "
						+ String.format("%.2f", stat
								.averageVehiclesOnRoad(road)) + ".", 15, 32);
		graph.drawString("Accidents : " + stat.accidentsOnRoad(road) + ".", 15,
				44);
	}
}
