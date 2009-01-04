package traffic.console.graphic;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class HelpPanel extends JDialog {
	static public final long serialVersionUID = 21870L;

	public HelpPanel() {
		super();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		setIconImage(ImageLoader.systemIcon);
		setTitle("Help");
		Simple p = new Simple();
		add(p);
		setSize(p.getWidth(), p.getHeight());
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

	private class Simple extends JPanel {
		static public final long serialVersionUID = 11545L;
		HashMap<String, String> helpKeyContent = new HashMap<String, String>();
		HashMap<String, String> helpMouseContent = new HashMap<String, String>();
		final Font descrFont = new Font("Courier New", Font.BOLD, 16);
		final Font contentFont = new Font("Courier New", 0, 12);
		final int titleX = 40, titleY = 40;
		final int firstDescrLineX = 40;
		final int firstDescrLineY = 40;
		final int descrLineHeight = 20;
		final int contentLineHeight = 20;
		final int firstContentLineX = 50;

		public Simple() {
			setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createTitledBorder("Shortcuts"), BorderFactory
					.createEmptyBorder(5, 5, 5, 5)));
			helpKeyContent.put("Arrow Keys", "Navigate the map.");
			helpKeyContent.put("Page up", "Zoom in");
			helpKeyContent.put("Page down", "Zoom out");
			helpMouseContent.put("Move Mouse to map border",
					"Roll the map automatically");
			helpMouseContent.put("Click mouse", "Select a car");
			helpMouseContent.put("Double-click mouse",
					"Add a new vehicle at the specified position");
			helpMouseContent.put("Mouse drag", "Navigate the map");
			setSize(500, titleY + firstDescrLineY
					+ (descrLineHeight + contentLineHeight)
					* (helpKeyContent.size() + helpMouseContent.size()));
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setFont(descrFont);
			int count = 0;
			for (Entry<String, String> current : helpKeyContent.entrySet()) {
				g.drawString(current.getKey(), firstDescrLineX, firstDescrLineY
						+ count * (descrLineHeight + contentLineHeight));
				++count;
			}
			for (Entry<String, String> current : helpMouseContent.entrySet()) {
				g.drawString(current.getKey(), firstDescrLineX, firstDescrLineY
						+ count * (descrLineHeight + contentLineHeight));
				++count;
			}
			count = 0;
			g.setFont(contentFont);
			for (Entry<String, String> current : helpKeyContent.entrySet()) {
				g.drawString(current.getValue(), firstContentLineX,
						firstDescrLineY + count
								* (descrLineHeight + contentLineHeight)
								+ descrLineHeight);
				++count;
			}
			for (Entry<String, String> current : helpMouseContent.entrySet()) {
				g.drawString(current.getValue(), firstContentLineX,
						firstDescrLineY + count
								* (descrLineHeight + contentLineHeight)
								+ descrLineHeight);
				++count;
			}
		}
	}
}
