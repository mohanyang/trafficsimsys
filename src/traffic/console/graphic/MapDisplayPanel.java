package traffic.console.graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
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
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import traffic.basic.Lib;
import traffic.event.Event;
import traffic.event.EventDispatcher;
import traffic.event.EventListener;
import traffic.event.MouseInput;
import traffic.log.Log;
import traffic.map.entity.Map;
import traffic.map.entity.Point;
import traffic.map.entity.Road;
import traffic.map.entity.Vehicle;
import traffic.simulation.kernel.Simulator;
import traffic.simulation.statistics.IStat;
import traffic.simulation.statistics.StatBox;

/**
 * @author Isaac
 * 
 */
public class MapDisplayPanel extends JPanel implements MouseListener,
		MouseMotionListener, MouseWheelListener {
	public static final long serialVersionUID = 2L;
	public static final int MAXWIDTH = 5000, MAXHEIGHT = 5000;
	private int imgWidth = 1000, imgHeight = 1000;
	private int moveThreshold = 20, moveStep = 50;
	private Map map = null;
	BufferedImage[] img = null;
	BufferedImage bg = null;
	BufferedImage grassBG = null;
	BufferedImage carBG = null;
	AffineTransform trans = null;
	double scale = 1.0;

	ZoomPanel zoomPanel = null;
	VehicleDisplayPanel vPanel = null;

	private int mouseX, mouseY;
	private boolean mouseInPanel = false;
	private JLabel statusLabel = null;
	private String oldStatus = null;

	private Road selectedRoad = null;
	private Vehicle selectedVehicle = null;
	private boolean clicked = false;
	private boolean isDrag = false;
	private int dragX = 0, dragY = 0;

	private EventDispatcher eventDispatcher = new EventDispatcher();

	private int startX = 0, startY = 0;
	private static final Color roadColor = Color.decode("#7F7F7F");
	private static final Color borderColor = Color.BLUE;
	private static final Color dotLineColor = Color.WHITE;

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
			img = new BufferedImage[ImageLoader.count];
			for (int i = 0; i < ImageLoader.count; ++i)
				img[i] = ImageLoader.loadImage(i);
			grassBG = ImageLoader.loadImageByName("bg.bmp");
			carBG = ImageLoader.loadImageByName("infobg.png");
			Lib.assertTrue(carBG != null);
			Lib.alphaImage(carBG, 0xC0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		trans = new AffineTransform();
		setBorder(BorderFactory.createLineBorder(Color.decode("#B8CFE5")));
		setSize(imgWidth, imgHeight);

		bg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		zoomPanel = new ZoomPanel(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addMouseListener(this);
	}

	public void setStatusLabel(JLabel status) {
		statusLabel = status;
	}

	public void paint(Map map) {
		imgWidth = getWidth();
		imgHeight = getHeight();
		Graphics graphics = getGraphics();
		if (graphics != null) {
			this.map = map;
			moveScreen();
			drawMapOnGraphics((Graphics2D) graphics);
		}
	}

	/**
	 * return a triangle, heading s<-e, with head at s
	 * 
	 * @param s
	 * @param e
	 * @return
	 */
	private Polygon getTriangle(Point s, Point e) {
		Point p2 = Point.distanceSegment(e, s, 10);
		Point p3 = Point.distanceSegment(e, s, 10);
		Road.moveLine(s.clone(), e.clone(), p2, 5);
		Road.moveLine(s.clone(), e.clone(), p3, -5);
		Polygon ret = new Polygon();
		ret.addPoint((int) transImgX(s.getXAxis()), (int) transImgY(s
				.getYAxis()));
		ret.addPoint((int) transImgX(p2.getXAxis()), (int) transImgY(p2
				.getYAxis()));
		ret.addPoint((int) transImgX(p3.getXAxis()), (int) transImgY(p3
				.getYAxis()));
		return ret;
	}

	private void drawRoad(Graphics2D g, Road rr, Color color) {
		for (Road r : rr.getRoadBySegment()) {
			Point p1 = null, p2 = null, t1 = null, t2 = null;
			int n1 = 0, n2 = 0;
			double lamda = rr.getLamda();
			LinkedList<Road> list = null;
			p1 = r.getStartPoint();
			p2 = r.getEndPoint();
			list = p1.getIntersectionRoadList();
			for (Iterator<Road> itr = list.iterator(); itr.hasNext();) {
				Road tmp = itr.next();
				if (Double.compare(tmp.getLamda(), lamda) != 0) {
					n1 = tmp.getLane();
					break;
				}
			}
			list = p2.getIntersectionRoadList();
			for (Iterator<Road> itr = list.iterator(); itr.hasNext();) {
				Road tmp = itr.next();
				if (Double.compare(tmp.getLamda(), lamda) != 0) {
					n2 = tmp.getLane();
					break;
				}
			}

			g.setColor(color);
			g.setStroke(new BasicStroke(
					(float) (r.getLane() * Road.laneWidth * scale)));
			p1 = r
					.getPositionOnRoad(r.getLane() * Road.laneWidth / 2, 0,
							false);
			p2 = r.getPositionOnRoad(r.getLength() - r.getLane()
					* Road.laneWidth / 2, 0, false);
			g.draw(new Line2D.Double(transImgX(p1.getXAxis()), transImgY(p1
					.getYAxis()), transImgX(p2.getXAxis()), transImgY(p2
					.getYAxis())));

			p1 = r.getPositionOnRoad(n1 * Road.laneWidth / 2, 0, false).clone();
			p2 = r.getPositionOnRoad(r.getLength() - n2 * Road.laneWidth / 2,
					0, false).clone();

			g.setStroke(borderStroke);
			g.setColor(borderColor);
			t1 = p1.clone();
			t2 = p2.clone();
			Road.moveLine(t1, t2, t2, -r.getLane() * Road.laneWidth / 2);
			g.draw(new Line2D.Double(transImgX(t1.getXAxis()), transImgY(t1
					.getYAxis()), transImgX(t2.getXAxis()), transImgY(t2
					.getYAxis())));

			if (scale > 0.25) {
				for (int i = 0; i < r.getLane(); ++i) {
					g.setStroke(borderStroke);
					g.setColor(dotLineColor);
					t1 = r.getPositionOnRoad(r.getLength() / 2, i);
					Polygon tri = null;
					tri = getTriangle(t1, r.getPositionOnRoad(0, i));
					g.drawPolygon(tri);
					g.fillPolygon(tri);

					g.setStroke(dotLineStroke);
					g.setColor(dotLineColor);
					t1 = p1.clone();
					t2 = p2.clone();
					Road.moveLine(t1, t2, t2, r.getLane() * Road.laneWidth / 2
							- i * Road.laneWidth - Road.laneWidth / 2);
					g.draw(new Line2D.Double(transImgX(t1.getXAxis()),
							transImgY(t1.getYAxis()), transImgX(t2.getXAxis()),
							transImgY(t2.getYAxis())));

					g.setStroke(borderStroke);
					g.setColor(borderColor);
					t1 = p1.clone();
					t2 = p2.clone();
					Road.moveLine(t1, t2, t2, r.getLane() * Road.laneWidth / 2
							- i * Road.laneWidth);
					g.draw(new Line2D.Double(transImgX(t1.getXAxis()),
							transImgY(t1.getYAxis()), transImgX(t2.getXAxis()),
							transImgY(t2.getYAxis())));
				}
			} else {
				g.setStroke(borderStroke);
				g.setColor(borderColor);
				t1 = p1.clone();
				t2 = p2.clone();
				Road.moveLine(t1, t2, t2, r.getLane() * Road.laneWidth / 2);
				g.draw(new Line2D.Double(transImgX(t1.getXAxis()), transImgY(t1
						.getYAxis()), transImgX(t2.getXAxis()), transImgY(t2
						.getYAxis())));
			}
		}
	}

	private void drawMapOnGraphics(Graphics2D graphics) {
		Graphics2D bf = (Graphics2D) bg.getGraphics();
		Rectangle rect = new Rectangle(0, 0, grassBG.getWidth(), grassBG
				.getHeight());
		TexturePaint texture = new TexturePaint(grassBG, rect);
		bf.setPaint(texture);
		bf.fillRect(0, 0, imgWidth, imgHeight);
		for (Iterator<Point> PointItr = map.getPointList(); PointItr.hasNext();) {
			Point p = PointItr.next();
			for (Iterator<Road> RoadItr = p.getRoadList(); RoadItr.hasNext();) {
				Road r = RoadItr.next();
				if (r.getStartPoint().equals(p)) {
					drawRoad(bf, r, roadColor);
				}
			}
		}

		// draw the selected road / vehicle
		selectedRoad = map.getRoad(transMapX(mouseX), transMapY(mouseY));
		if (mouseInPanel && selectedRoad != null) {
			Vehicle v = map.getVehicle(selectedRoad, transMapX(mouseX),
					transMapY(mouseY));
			if (v != null) {
				if (selectedVehicle != v) {
					selectedVehicle = v;
					clicked = false;
				}
			}
			if (selectedVehicle == null) {
				drawRoad(bf, selectedRoad, highLightRoadColor);
			}
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

		if (selectedVehicle == null) {
			// draw statistic information
			IStat stat = Simulator.getInstance().getStat();

			if (mouseInPanel && selectedRoad != null && stat != null) {
				StatBox box = StatBox.getInstance();
				box.acquireLock();
				box.prepare(stat, selectedRoad);
				java.awt.Point pos = getBoxPosition(box.getWidth(), box
						.getHeight());
				bf.drawImage(box, null, pos.x, pos.y);
				box.releaseLock();
			}
		}
		if (vPanel != null) {
			vPanel.setVehicle(selectedVehicle);
			vPanel.paint();
		}

		if (clicked == false)
			selectedVehicle = null;
		bf.drawImage(zoomPanel, null, ZoomPanel.anchorX, ZoomPanel.anchorY);

		graphics.drawImage(bg, null, 0, 0);
	}

	private void drawVehicleOnRoad(Graphics2D graphics, Road r) {
		r.acquireLock();
		for (Iterator<Vehicle> itr = r.getVehicleList(); itr.hasNext();) {
			Vehicle v = itr.next();
			if (v.equals(selectedVehicle))
				drawAVehicle(graphics, r, v, true);
			else
				drawAVehicle(graphics, r, v, false);
		}
		r.releaseLock();
	}

	private void drawAVehicle(Graphics2D graphics, Road r, Vehicle v,
			boolean emp) {
		Point s = v.getRoad().getPositionOnRoad(0, v.getLane());
		Point e = v.getRoad().getPositionOnRoad(v.getRoad().getLength(),
				v.getLane());
		double tanv = (e.getYAxis() - s.getYAxis())
				/ (e.getXAxis() - s.getXAxis());
		double theta = 0;
		Point px = new Point(v.getPoint().getXAxis(), v.getPoint().getYAxis());
		Road.moveLine(new Point(s.getXAxis(), s.getYAxis()), px, px,
				-Road.laneWidth / 2);
		double xx = px.getXAxis(), yy = px.getYAxis();
		if (tanv == Double.POSITIVE_INFINITY) {
			theta = 0;

		} else if (tanv == Double.NEGATIVE_INFINITY) {
			theta = 180;
		} else {
			theta = Math.toDegrees(Math.atan(tanv)) + 270;
		}
		theta += 180;
		if (s.getXAxis() > e.getXAxis()) {
			theta += 180;
			yy = yy + Road.laneWidth;
		}
		Log.getInstance().writeln(r + "\n" + theta);

		trans.setToRotation(Math.toRadians(theta));
		AffineTransform tmp = new AffineTransform();
		tmp.setToScale(scale * ImageLoader.scale, scale * ImageLoader.scale);
		trans.concatenate(tmp);
		BufferedImageOp op = new AffineTransformOp(trans,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		graphics.drawImage(img[v.getVehicleInf().getImageID()], op,
				(int) transImgX(xx), (int) transImgY(yy));
		if (emp) {
			trans.setToRotation(Math.toRadians(theta));
			tmp.setToScale(scale
					* ImageLoader.scale
					* 0.25
					* ((double) carBG.getHeight() / (double) img[v
							.getVehicleInf().getImageID()].getHeight()), scale
					* ImageLoader.scale
					* 0.25
					* ((double) carBG.getWidth() / (double) img[v
							.getVehicleInf().getImageID()].getWidth()));
			trans.concatenate(tmp);
			op = new AffineTransformOp(trans,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			graphics.drawImage(carBG, op, (int) transImgX(xx),
					(int) transImgY(yy));
		}
	}

	private void moveScreen() {
		if (mouseInPanel && mouseX - moveThreshold < 0) {
			moveLeft();
		}
		if (mouseInPanel && imgWidth < mouseX + moveThreshold) {
			moveRight();
		}
		if (mouseInPanel && mouseY - moveThreshold < 0) {
			moveUp();
		}
		if (mouseInPanel && mouseY + moveThreshold > imgHeight) {
			moveDown();
		}
	}

	protected void moveLeft() {
		moveLeft(1);
	}

	protected void moveRight() {
		moveRight(1);
	}

	protected void moveUp() {
		moveUp(1);
	}

	protected void moveDown() {
		moveDown(1);
	}

	private void moveLeft(int n) {
		startX = Math.max(0, startX - moveStep * n);
	}

	private void moveRight(int n) {
		startX = Math.min(MAXWIDTH - imgWidth, startX + moveStep * n);
	}

	private void moveUp(int n) {
		startY = Math.max(0, startY - moveStep * n);
	}

	private void moveDown(int n) {
		startY = Math.min(MAXHEIGHT - imgHeight, startY + moveStep * n);
	}

	private java.awt.Point getBoxPosition(int width, int height) {
		return new java.awt.Point(mouseX + 5, mouseY - height);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// if (!isDrag) {
		// dragX = e.getX();
		// dragY = e.getY();
		// isDrag = true;
		// }
		int xx = e.getX() - dragX, yy = e.getY() - dragY;
		// System.out.println(dragX + " " + xx + " " + dragY + " " + yy);
		dragX = e.getX();
		dragY = e.getY();
		int n = (xx * xx + yy * yy) / 10000;
		if (xx > 20)
			moveRight(n);
		else
			moveLeft(n);
		if (yy > 20)
			moveDown(n);
		else
			moveUp(n);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		zoomPanel.mouseMoved(e);
		// if (!isDrag)
		dispatchEvent(new Event(this, Event.MOUSE_INPUT, new MouseInput(e,
				(int) transMapX(e.getX()), (int) transMapY(e.getY()), false)));
		statusLabel.setText("current point on the map system of coordinates ("
				+ (int) transMapX(mouseX) + ", " + (int) transMapY(mouseY)
				+ ")");
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		zoomPanel.newCursor(-e.getWheelRotation());
	}

	protected void scaleResize(double num) {
		scale *= Math.pow(1.2, num);
		statusLabel.setText("map scale magnitude resized to " + scale);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		mouseX = arg0.getX();
		mouseY = arg0.getY();
		double xx = transMapX(mouseX), yy = transMapY(mouseY);

		boolean zoomResult = zoomPanel.mouseClicked(arg0);
		if (!zoomResult) {
			dispatchEvent(new Event(this, Event.MOUSE_INPUT, new MouseInput(
					arg0, (int) xx, (int) yy, true)));

			Road r = map.getRoad(xx, yy);
			if (r != null) {
				Vehicle v = map.getVehicle(r, xx, yy);
				if (v != null) {
					selectedVehicle = v;
					clicked = true;
				}
			}

			if (arg0.getButton() == MouseEvent.BUTTON3) {
				selectedVehicle = null;
				clicked = false;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		mouseInPanel = true;
		oldStatus = statusLabel.getText();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseInPanel = false;
		statusLabel.setText(oldStatus);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// nothing
		isDrag = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// if (isDrag) {
		// int xx = e.getX() - dragX, yy = e.getY() - dragY;
		// int n = (xx * xx + yy * yy) / 10000;
		// if (xx > 20)
		// moveRight(n);
		// else
		// moveLeft(n);
		// if (yy > 20)
		// moveDown(n);
		// else
		// moveUp(n);
		// isDrag = false;
		// }
		isDrag = false;
	}

	public void addEventListener(EventListener listener) {
		eventDispatcher.addEventListener(listener);
	}

	public void dispatchEvent(Event e) {
		eventDispatcher.dispatchEvent(e);
	}

	public void setVehicleDisplayPanel(VehicleDisplayPanel panel) {
		vPanel = panel;
	}
}
