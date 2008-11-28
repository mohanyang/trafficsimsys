package traffic.console.graphic;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.UIManager;

import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;

public class MapDisplayPanel extends JPanel {
	static public final long serialVersionUID = 2L;
	private Map map = null;
	BufferedImage img = null;
	AffineTransform trans = null;

	public MapDisplayPanel() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		try {
			img = ImageIO.read(new File("./image/1.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		trans = new AffineTransform();
		this.setSize(800, 600);
	}

	public void paint(Map map) {
		Graphics graphics = getGraphics();
		if (graphics != null) {
			this.map = map;
			drawMapOnGraphics((Graphics2D) graphics);
		}
	}

	private void drawMapOnGraphics(Graphics2D graphics) {
		for (Iterator<Point> PointItr = map.getPointList(); PointItr.hasNext();) {
			Point p = PointItr.next();
			for (Iterator<Road> RoadItr = p.getRoadList(); RoadItr.hasNext();) {
				Road r = RoadItr.next();
				if (r.getStartPoint().equals(p)) {
					Point pe = r.getEndPoint();
					graphics.setStroke(new BasicStroke(r.getLane() * 26));
					graphics.draw(new Line2D.Double(p.getXAxis(), p.getYAxis(),
							pe.getXAxis(), pe.getYAxis()));
					drawVehicleOnRoad(graphics, r);
				}
			}
		}
	}

	private void drawVehicleOnRoad(Graphics2D graphics, Road r) {
		for (Iterator<Vehicle> itr = r.getVehicleList(); itr.hasNext();) {
			Vehicle v = itr.next();
			Point s = v.getRoad().getStartPoint(), e = v.getRoad()
					.getEndPoint();
			int direct = v.getSpeed() > 0 ? 1 : -1;
			trans.setToRotation(direct * (e.getXAxis() - s.getXAxis()), e
					.getYAxis()
					- s.getYAxis(), v.getPoint().getXAxis(), v.getPoint()
					.getYAxis());
			BufferedImageOp op = new AffineTransformOp(trans,
					AffineTransformOp.TYPE_BICUBIC);
			((Graphics2D) graphics).drawImage(img, op, (int) v.getPoint()
					.getXAxis(), (int) v.getPoint().getYAxis() - 16);
		}
	}
}
