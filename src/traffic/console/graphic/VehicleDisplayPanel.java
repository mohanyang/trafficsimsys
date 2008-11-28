package traffic.console.graphic;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class VehicleDisplayPanel extends JPanel {
	static public final long serialVersionUID = 3L;
	BufferedImage img = null;
	AffineTransform trans = null;
	Timer timer;
	private int n = 0;

	public VehicleDisplayPanel() {
		this.setOpaque(false);
		try {
			img = ImageIO.read(new File("./image/1.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		trans = new AffineTransform();
		timer = new Timer();
		timer.schedule(new RemindTask(), 0, 100);
		this.setSize(800, 600);
	}

	class RemindTask extends TimerTask {
		public void run() {
			repaint();
			n = n + 10;
			if (n > 600) {
				timer.cancel();
			}
		}
	}

	public void repaint() {
		Graphics graphics = getGraphics();
		if (graphics != null) {
			graphics.clearRect(0, 0, 800, 600);
			trans.setToRotation(-1, 1, 0, 0);
			BufferedImageOp op = new AffineTransformOp(trans,
					AffineTransformOp.TYPE_BICUBIC);
			((Graphics2D) graphics).drawImage(img, op, n, n - 16);
		}
	}
}