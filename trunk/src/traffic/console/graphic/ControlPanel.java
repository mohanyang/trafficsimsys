package traffic.console.graphic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.UIManager;

public class ControlPanel extends JPanel {
	public static final long serialVersionUID = 10L;

	BufferedImage bf = ImageLoader.loadImageByName("infobg.gif");

	public ControlPanel() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		this.setToolTipText("11");
		this.setSize(100, 200);
	}

	protected void paintComponent(Graphics g) {
		setOpaque(true);
		super.paintComponent(g);
		g.drawImage(bf, 0, 0, null);
	}
}
