package traffic.console.graphic;

import java.awt.BasicStroke;
import java.awt.Color;
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
	BufferedImage[] img = null;
	AffineTransform trans = null;

	public MapDisplayPanel() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		try {
			img = new BufferedImage[4];
			img[0] = ImageIO.read(new File("./image/1.gif"));
			img[1] = ImageIO.read(new File("./image/2.gif"));
			img[2] = ImageIO.read(new File("./image/3.gif"));
			img[3] = ImageIO.read(new File("./image/4.gif"));
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
			// Color c = graphics.getColor();
			// graphics.fillRect(0, 0, 800, 600);
			// graphics.setColor(Color.white);
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
				}
			}
		}
		// for (Iterator<Point> PointItr = map.getPointList();
		// PointItr.hasNext();) {
		// Point p = PointItr.next();
		// Color c = graphics.getColor();
		// graphics.setColor(Color.RED);
		// graphics.drawRect((int) p.getXAxis() - 5, (int) p.getYAxis() - 5,
		// 10, 10);
		// graphics.setColor(c);
		// }
		for (Iterator<Point> PointItr = map.getPointList(); PointItr.hasNext();) {
			Point p = PointItr.next();
			for (Iterator<Road> RoadItr = p.getRoadList(); RoadItr.hasNext();) {
				Road r = RoadItr.next();
				if (r.getStartPoint().equals(p)) {
					drawVehicleOnRoad(graphics, r);
				}
			}
		}
	}

	private void drawVehicleOnRoad(Graphics2D graphics, Road r) {
		r.acquireLock();
		for (Iterator<Vehicle> itr = r.getVehicleList(); itr.hasNext();) {
			Vehicle v = itr.next();
			Point s = v.getRoad().getStartPoint(), e = v.getRoad()
					.getEndPoint();
			double tanv = (e.getYAxis() - s.getYAxis()) / (e.getXAxis() - s.getXAxis()), 
					theta, 
					x = v.getPoint().getXAxis(), 
					y = v.getPoint().getYAxis();
			if (tanv == Double.POSITIVE_INFINITY) {
				theta = 0;
				x = x + 13;
			} else if (tanv == Double.NEGATIVE_INFINITY) {
				theta = 180;
				x = x + 13;
			} else {
				theta = Math.toDegrees(Math.atan(tanv)) + 270;
				y = y - 13;
			}
			System.out.println(r + "\n" + theta);
//			if (v.getSpeed() > 0)
				theta += 180;
			trans.setToRotation(Math.toRadians(theta));
			BufferedImageOp op = new AffineTransformOp(trans,
					AffineTransformOp.TYPE_BICUBIC);
			((Graphics2D) graphics).drawImage(img[v.getVehicleInf()
					.getImageID()], op, (int) x, (int) y);
		}
		r.releaseLock();
	}
}
