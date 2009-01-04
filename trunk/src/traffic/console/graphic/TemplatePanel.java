package traffic.console.graphic;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.UIManager;

public abstract class TemplatePanel extends JDialog {
	static public final long serialVersionUID = 1234L;

	public TemplatePanel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		setIconImage(ImageLoader.systemIcon);
		subConstruct();
		centerize();
		setVisible(true);
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

	abstract public void subConstruct();

}
