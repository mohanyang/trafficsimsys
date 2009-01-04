package traffic.console.graphic;

import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import traffic.basic.Lib;

public class ZoomPanel extends BufferedImage {
	public static final long serialVersionUID = 11L;
	private static final int width = 60;
	private static final int height = 262;
	private static final int curMin = 0, curMax = 15;
	public static final int anchorX = 15, anchorY = 15;

	private Graphics2D graph = null;
	private BufferedImage bg = null;
	private BufferedImage cursorImg = null;
	private MapDisplayPanel container = null;

	private Cursor oldCursor = null;
	private boolean inPanel = false;
	private int curCursor = 10;

	private GridBox[] list = { new GridBox("up", 20, 36, 0, 16),
			new GridBox("left", 0, 16, 20, 36),
			new GridBox("down", 20, 36, 40, 56),
			new GridBox("right", 40, 56, 20, 36),
			new GridBox("in", 20, 36, 70, 86),
			new GridBox("out", 20, 36, 244, 260) };

	public ZoomPanel(MapDisplayPanel c) {
		super(width, height, TYPE_4BYTE_ABGR);
		graph = createGraphics();
		bg = ImageLoader.loadImageByName("bgMap.gif");
		Lib.assertTrue(bg != null);
		cursorImg = bg.getSubimage(20, 264, 16, 8);
		Lib.assertTrue(cursorImg != null);
		curCursor = 11;
		prepare();
		container = c;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	public void prepare() {
		graph.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,
				0.0f));
		graph.fillRect(0, 0, width, height);
		graph.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER,
				1.0f));
		graph.drawImage(bg, null, 0, 0);
		graph.drawImage(cursorImg, null, 20, 80 + (curMax - curCursor) * 11);
	}

	public void mouseMoved(MouseEvent e) {
		if (inPanel) {
			if (!isInside(e.getPoint())) {
				container.setCursor(oldCursor);
				inPanel = false;
			}
		} else {
			if (isInside(e.getPoint())) {
				oldCursor = container.getCursor();
				container.setCursor(new Cursor(Cursor.HAND_CURSOR));
				inPanel = true;
			}
		}
	}

	public boolean mouseClicked(MouseEvent e) {
		if (inPanel) {
			Point p = e.getPoint();
			int idx = getIndex(p.getX() - anchorX, p.getY() - anchorY);
			switch (idx) {
			case 0:
				container.moveUp();
				break;
			case 1:
				container.moveLeft();
				break;
			case 2:
				container.moveDown();
				break;
			case 3:
				container.moveRight();
				break;
			case 4:
				newCursor(1);
				break;
			case 5:
				newCursor(-1);
				break;
			default:
				return false;
			}
			return true;
		}
		return false;
	}

	public void newCursor(int k) {
		int t = curCursor + k;
		if (t >= curMin && t <= curMax) {
			curCursor = t;
			container.scaleResize(k);
			prepare();
		}
	}

	private boolean isInside(Point p) {
		double x = p.getX() - anchorX, y = p.getY() - anchorY;
		if (!(x >= 0 && x <= width && y >= 0 && y <= height))
			return false;
		int idx = getIndex(x, y);
		return idx >= 0 ? true : false;
	}

	private int getIndex(double x, double y) {
		for (int i = 0; i < list.length; ++i)
			if (list[i].isInside(x, y))
				return i;
		return -1;
	}

	class GridBox {
		String name;
		int x1, x2, y1, y2;

		GridBox(String name, int x1, int x2, int y1, int y2) {
			this.name = name;
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
		}

		boolean isInside(double x, double y) {
			return (x >= x1 && x <= x2 && y >= y1 && y <= y2);
		}
	}
}
