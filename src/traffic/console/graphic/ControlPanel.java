package traffic.console.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class ControlPanel extends JPanel {
	public static final long serialVersionUID = 10L;

	BufferedImage bf = ImageLoader.loadImageByName("bgMap.gif");

	public ControlPanel() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		this.setBackground(Color.WHITE);
		this.setSize(200, 750);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bf, 0, 0, this);
	}
}
