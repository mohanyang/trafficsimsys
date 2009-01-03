package traffic.console.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.UIManager;

import traffic.basic.Lib;

public class SplashWindow extends JWindow {
	static public final long serialVersionUID = 111L;
	private static final int width = 480, height = 320;
	BufferedImage img = null;
	JProgressBar bar = null;

	public SplashWindow() {
		super();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		bar = new JProgressBar();
		bar.setMaximum(100);
		setLayout(new BorderLayout());
		getContentPane().add(new Simple(), BorderLayout.CENTER);
		getContentPane().add(bar, BorderLayout.SOUTH);

		pack();
		setSize(width, height);

		centerize();
		setVisible(true);
	}

	public void setRatio(double r) {
		bar.setValue((int) (r * 100));
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (Lib.isEqual(r, 1.0))
			dispose();
	}

	private void centerize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width / 2;
		int screenHeight = screenSize.height / 2;
		int height = getHeight();
		int width = getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);
	}

	private class Simple extends JPanel {
		static public final long serialVersionUID = 112L;

		public Simple() {
			img = ImageLoader.loadImageByName("start.gif");
			Lib.assertTrue(img != null);
			setSize(img.getWidth(), img.getHeight());
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			double scale = 1.2;
			AffineTransform trans = new AffineTransform();
			trans.setToScale(scale, scale);
			BufferedImageOp op = new AffineTransformOp(trans,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			((Graphics2D) g).drawImage(img, op, (int) (width / 2 - img
					.getWidth()
					/ 2 * scale), (int) ((height - 20) / 2 - img.getHeight()
					/ 2 * scale));
		}
	}
}
