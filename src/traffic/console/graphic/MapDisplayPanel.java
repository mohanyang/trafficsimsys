package traffic.console.graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.event.MouseInputListener;

import traffic.basic.Lib;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;
import traffic.simulation.kernel.Simulator;
import traffic.simulation.statistics.IStat;

public class MapDisplayPanel extends JPanel implements MouseListener,
		MouseMotionListener, MouseWheelListener {
	public static final long serialVersionUID = 2L;
	public static final int MAXWIDTH = 5000, MAXHEIGHT = 5000;
	private int imgWidth = 800, imgHeight = 600;
	private int moveThreshold = 50, moveStep = 50;
	private Map map = null;
	BufferedImage[] img = null;
	BufferedImage bg = null;
	AffineTransform trans = null;
	double scale = 1.0;

	private int mouseX, mouseY;
	private boolean mouseInPanel = false;

	private int startX = 0, startY = 0;
	private static final Color roadColor = Color.GRAY;
	private static final Color highLightRoadColor = Color.WHITE;

	private double transImgX(double mapX) {
		return (mapX - startX) * scale;
	}

	private double transImgY(double mapY) {
		return (mapY - startY) * scale;
	}

	private double transMapX(double imgX) {
		return imgX / scale + startX;
	}

	private double transMapY(double imgY) {
		return imgY / scale + startY;
	}

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
		this.setSize(imgWidth, imgHeight);

		// isScale = Config.getBoolean("traffic.scale", false);
		// if (isScale) {
		// bg = new BufferedImage(MAXWIDTH, MAXHEIGHT,
		// BufferedImage.TYPE_INT_RGB);
		// bg.setAccelerationPriority(1);
		// } else {
		// bg = new BufferedImage(imgWidth, imgHeight,
		// BufferedImage.TYPE_INT_RGB);
		// }

		bg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addMouseListener(this);
	}

	public void paint(Map map) {
		Graphics graphics = getGraphics();
		if (graphics != null) {
			this.map = map;
			moveScreen();
			drawMapOnGraphics((Graphics2D) graphics);
		}
	}

	private void drawRoad(Graphics2D g, Road r, Color color) {
		g.setColor(color);
		g.setStroke(new BasicStroke(
				(float) (r.getLane() * Road.laneWidth * scale)));
		Point p1 = r.getPositionOnRoad(r.getLane() * Road.laneWidth / 2, 0,
				false);
		Point p2 = r.getPositionOnRoad(r.getLength() - r.getLane()
				* Road.laneWidth / 2, 0, false);
		double x1 = transImgX(p1.getXAxis());
		double y1 = transImgY(p1.getYAxis());
		double x2 = transImgX(p2.getXAxis());
		double y2 = transImgY(p2.getYAxis());
		g.draw(new Line2D.Double(x1, y1, x2, y2));
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
		Road selectedRoad = map.getRoad(transMapX(mouseX), transMapY(mouseY));
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

		// if (isScale) {
		// trans.setToScale(scale, scale);
		// BufferedImageOp op = new AffineTransformOp(trans,
		// AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		// int xx = (int) (imgWidth / scale) + 1, yy = (int) (imgHeight / scale)
		// + 1;
		// graphics.drawImage(bg.getSubimage(0, 0, xx, yy), op, 0, 0);
		// } else {
		graphics.drawImage(bg, null, 0, 0);
		// }

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
			// trans.setToScale(scale, scale);
			AffineTransform tmp = new AffineTransform();
			tmp.setToScale(scale, scale);
			trans.concatenate(tmp);
			BufferedImageOp op = new AffineTransformOp(trans,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			graphics.drawImage(img[v.getVehicleInf().getImageID()], op,
					(int) transImgX(px.getXAxis()), (int) transImgY(px
							.getYAxis()));
		}
		r.releaseLock();
	}

	private void moveScreen() {
		if (mouseInPanel && mouseX - moveThreshold < 0) {
			startX = Math.max(0, startX - moveStep);
		}
		if (mouseInPanel && imgWidth < mouseX + moveThreshold) {
			startX = Math.min(MAXWIDTH - imgWidth, startX + moveStep);
		}
		if (mouseInPanel && mouseY - moveThreshold < 0) {
			startY = Math.max(0, startY - moveStep);
		}
		if (mouseInPanel && mouseY + moveThreshold > imgHeight) {
			startY = Math.min(MAXHEIGHT - imgHeight, startY + moveStep);
		}
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// nothing
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		mouseInPanel = true;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseInPanel = false;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// nothing
	}

}
