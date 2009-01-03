package traffic.console.graphic;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class ControlPanel extends JPanel {
	public static final long serialVersionUID = 10L;

	public ControlPanel() {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(""), BorderFactory.createEmptyBorder(5, 5,
				5, 5)));
		setSize(200, 650);
	}
}
