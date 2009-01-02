package traffic.console.graphic;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class ControlPanel extends JPanel {
	public static final long serialVersionUID = 10L;

	public ControlPanel() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		this.setBorder(new LineBorder(Color.BLACK));
		this.setSize(200, 650);
	}
}
