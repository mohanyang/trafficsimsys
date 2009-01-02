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
	private static final Color bgColor = Color.decode("#5BDB57");
	private static final Color roadColor = Color.decode("#7F7F7F");
	private static final Color borderColor = Color.BLUE;
	private static final Color dotLineColor = Color.WHITE;
	private static final Color textColor = Color.decode("#F87858");

	private static final Color highLightRoadColor = Color.YELLOW;
	private static final BasicStroke borderStroke = new BasicStroke(0.1f);
	private static final BasicStroke dotLineStroke = new BasicStroke(0.1f,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, new float[] {
					6.0f, 4.0f }, 0f);

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
			for (int i=0; i<4; ++i)
				img[i]=ImageLoader.loadImage(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
		trans = new AffineTransform();
		this.setSize(imgWidth, imgHeight);

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
		Point p1 = null, p2 = null, t1 = null, t2 = null;
		if (r.n1 == -1) {
			int n1 = 0, n2 = 0, n;
			double lamda0 = r.getLamda(), lamda1 = 0.0;
			p1 = r.getStartPoint();
			p2 = r.getEndPoint();
			n = p1.getDegree();
			if (n != 1) {
				for (Iterator<Road> itr = p1.getRoadList(); itr.hasNext();) {
					Road tmp = itr.next();
					lamda1 = tmp.getLamda();
					if (lamda0 != lamda1) {
						n1 = tmp.getLane();
						break;
					}
				}
			}
			n = p2.getDegree();
			if (n != 1) {
				for (Iterator<Road> itr = p2.getRoadList(); itr.hasNext();) {
					Road tmp = itr.next();
					lamda1 = tmp.getLamda();
					if (lamda0 != lamda1) {
						n2 = tmp.getLane();
						break;
					}
				}
			}
			r.n1 = n1;
			r.n2 = n2;
		}

		g.setColor(color);
		g.setStroke(new BasicStroke(
				(float) (r.getLane() * Road.laneWidth * scale)));
		p1 = r.getPositionOnRoad(r.getLane() * Road.laneWidth / 2, 0, false);
		p2 = r.getPositionOnRoad(r.getLength() - r.getLane() * Road.laneWidth
				/ 2, 0, false);
		g.draw(new Line2D.Double(transImgX(p1.getXAxis()), transImgY(p1
				.getYAxis()), transImgX(p2.getXAxis()),
				transImgY(p2.getYAxis())));

		if (r.getDirection(0) == 0) {
			p1 = r.getPositionOnRoad(r.n2 * Road.laneWidth / 2, 0, false)
					.clone();
			p2 = r.getPositionOnRoad(r.getLength() - r.n1 * Road.laneWidth / 2,
					0, false).clone();
		} else {
			p1 = r.getPositionOnRoad(r.n1 * Road.laneWidth / 2, 0, false)
					.clone();
			p2 = r.getPositionOnRoad(r.getLength() - r.n2 * Road.laneWidth / 2,
					0, false).clone();
		}

		g.setStroke(borderStroke);
		g.setColor(borderColor);
		t1 = p1.clone();
		t2 = p2.clone();
		r.moveLine(t1, t2, t2, -r.getLane() * Road.laneWidth / 2);
		g.draw(new Line2D.Double(transImgX(t1.getXAxis()), transImgY(t1
				.getYAxis()), transImgX(t2.getXAxis()),
				transImgY(t2.getYAxis())));

		for (int i = 0; i < r.getLane(); ++i) {
			g.setStroke(dotLineStroke);
			g.setColor(dotLineColor);
			t1 = p1.clone();
			t2 = p2.clone();
			r.moveLine(t1, t2, t2, r.getLane() * Road.laneWidth / 2 - i
					* Road.laneWidth - 13);
			g.draw(new Line2D.Double(transImgX(t1.getXAxis()), transImgY(t1
					.getYAxis()), transImgX(t2.getXAxis()), transImgY(t2
					.getYAxis())));

			g.setStroke(borderStroke);
			g.setColor(borderColor);
			t1 = p1.clone();
			t2 = p2.clone();
			r.moveLine(t1, t2, t2, r.getLane() * Road.laneWidth / 2 - i
					* Road.laneWidth);
			g.draw(new Line2D.Double(transImgX(t1.getXAxis()), transImgY(t1
					.getYAxis()), transImgX(t2.getXAxis()), transImgY(t2
					.getYAxis())));
		}
	}

	private void drawMapOnGraphics(Graphics2D graphics) {
		Graphics2D bf = (Graphics2D) bg.getGraphics();
		bf.setColor(bgColor);
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
			bf.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
			bf.setColor(textColor);
			bf.drawString(msg0, mouseX + 10, mouseY - 12);
			bf.drawString(msg1, mouseX + 10, mouseY);
		}

		graphics.drawImage(bg, null, 0, 0);
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
			double theta = 0;
			Point px = new Point(v.getPoint().getXAxis(), v.getPoint()
					.getYAxis());
			v.getRoad().moveLine(new Point(s.getXAxis(), s.getYAxis()), px, px,
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
