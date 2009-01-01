package traffic.console.graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
import traffic.simulation.kernel.Simulator;
import traffic.simulation.statistics.IStat;

public class MapDisplayPanel extends JPanel implements MouseWheelListener,
		MouseMotionListener {
	static public final long serialVersionUID = 2L;
	static final int MAXWIDTH = 5000, MAXHEIGHT = 5000;
	private Map map = null;
	BufferedImage[] img = null;
	BufferedImage bg = null;
	BufferedImage transBG = null;
	BufferedImage disBG = null;
	AffineTransform trans = null;
	double scale = 1.0;

	private int mouseX, mouseY;

	private static final Color roadColor = Color.GRAY;
	private static final Color highLightRoadColor = Color.WHITE;

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

		bg = new BufferedImage(MAXWIDTH, MAXHEIGHT, BufferedImage.TYPE_INT_RGB);
		transBG = new BufferedImage(MAXWIDTH, MAXHEIGHT,
				BufferedImage.TYPE_INT_RGB);
		disBG = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
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

	private void drawRoad(Graphics2D g, Road r, Color color) {
		g.setColor(color);
		g.setStroke(new BasicStroke(r.getLane() * Road.laneWidth));
		Point p1 = r.getPositionOnRoad(r.getLane() * 13, 0, false);
		Point p2 = r.getPositionOnRoad(r.getLength() - r.getLane() * 13, 0,
				false);
		g.draw(new Line2D.Double(p1.getXAxis(), p1.getYAxis(), p2.getXAxis(),
				p2.getYAxis()));

	}

	private void drawMapOnGraphics(Graphics2D graphics) {
		Graphics2D bf = (Graphics2D) bg.getGraphics();
		bf.setColor(Color.BLACK);
		bf.fillRect(0, 0, MAXWIDTH, MAXHEIGHT);
		for (Iterator<Point> PointItr = map.getPointList(); PointItr.hasNext();) {
			Point p = PointItr.next();
			for (Iterator<Road> RoadItr = p.getRoadList(); RoadItr.hasNext();) {
				Road r = RoadItr.next();
				if (r.getStartPoint().equals(p)) {
					drawRoad(bf, r, roadColor);
				}
			}
		}

		// draw the selected road
		Road selectedRoad = map.getRoad(mouseX, mouseY);
		if (selectedRoad != null) {
			drawRoad(bf, selectedRoad, highLightRoadColor);
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
					drawVehicleOnRoad(bf, r);
				}
			}
		}

		// draw statistic information
		IStat stat = Simulator.getInstance().getStat();
		if (selectedRoad != null && stat != null) {
			String msg0 = "vehicles: "
					+ stat.currentVehiclesOnRoad(selectedRoad);
			String msg1 = "average : "
					+ String.format("%.2f", stat
							.averageVehiclesOnRoad(selectedRoad));
			bf.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
			bf.setColor(Color.RED);
			bf.drawString(msg0, mouseX + 10, mouseY - 12);
			bf.drawString(msg1, mouseX + 10, mouseY);
		}
		// trans.setToScale(0.5, 0.5);
		trans.setToScale(scale, scale);
		BufferedImageOp op = new AffineTransformOp(trans,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		op.filter(bg, transBG);
		disBG.setData(transBG.getSubimage(0, 0, 800, 600).getRaster());

		graphics.drawImage(disBG, null, 0, 0);
	}

	private void drawVehicleOnRoad(Graphics2D graphics, Road r) {
		r.acquireLock();
		for (Iterator<Vehicle> itr = r.getVehicleList(); itr.hasNext();) {
			Vehicle v = itr.next();
			Point s = v.getRoad().getPositionOnRoad(0, v.getLane());
			Point e = v.getRoad().getPositionOnRoad(v.getRoad().getLength(),
					v.getLane());
			double tanv = (e.getYAxis() - s.getYAxis())
					/ (e.getXAxis() - s.getXAxis());
			double theta;
			Point px = new Point(v.getPoint().getXAxis(), v.getPoint()
					.getYAxis());
			v.getRoad().moveLine(new Point(s.getXAxis(), s.getYAxis()), px,
					-Road.laneWidth / 2);
			if (tanv == Double.POSITIVE_INFINITY) {
				theta = 0;
			} else if (tanv == Double.NEGATIVE_INFINITY) {
				theta = 180;
			} else {
				theta = Math.toDegrees(Math.atan(tanv)) + 270;
			}
			System.out.println(r + "\n" + theta);
			theta += 180;
			trans.setToRotation(Math.toRadians(theta));
			BufferedImageOp op = new AffineTransformOp(trans,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			((Graphics2D) graphics).drawImage(img[v.getVehicleInf()
					.getImageID()], op, (int) px.getXAxis(), (int) px
					.getYAxis());
		}
		r.releaseLock();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int num = -e.getWheelRotation();
		scale *= Math.pow(1.05, num);
	}
}
