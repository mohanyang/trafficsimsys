package traffic.console.graphic;

import java.awt.Font;
import java.awt.Graphics;
import java.util.TreeMap;
import java.util.AbstractMap.SimpleEntry;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class HelpPanel extends TemplatePanel {
	static public final long serialVersionUID = 21870L;

	public HelpPanel() {
		super();
	}

	public void subConstruct() {
		setTitle("Help");
		Simple p = new Simple();
		add(p);
		setSize(p.getWidth(), p.getHeight());
	}

	private class Simple extends JPanel {
		static public final long serialVersionUID = 11545L;
		TreeMap<Integer, SimpleEntry<String, String>> helpKeyContent = new TreeMap<Integer, SimpleEntry<String, String>>();
		TreeMap<Integer, SimpleEntry<String, String>> helpMouseContent = new TreeMap<Integer, SimpleEntry<String, String>>();
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
			helpKeyContent.put(0, new SimpleEntry<String, String>(
					"Keyboard shortcuts:", "==================="));
			helpKeyContent.put(1, new SimpleEntry<String, String>("Arrow Keys",
					"Navigate the map."));
			helpKeyContent.put(2, new SimpleEntry<String, String>("Page up",
					"Zoom in"));
			helpKeyContent.put(3, new SimpleEntry<String, String>("Page down",
					"Zoom out"));
			helpMouseContent.put(1, new SimpleEntry<String, String>(
					"Move Mouse to map border", "Roll the map automatically"));
			helpMouseContent.put(0, new SimpleEntry<String, String>(
					"Mouse shortcuts:", "================"));
			helpMouseContent.put(2, new SimpleEntry<String, String>(
					"Click mouse", "Select a car"));
			helpMouseContent.put(3, new SimpleEntry<String, String>(
					"Double-click mouse",
					"Add a new vehicle at the specified position"));
			helpMouseContent.put(4, new SimpleEntry<String, String>(
					"Mouse drag", "Navigate the map"));
			setSize(500, titleY + firstDescrLineY
					+ (descrLineHeight + contentLineHeight)
					* (helpKeyContent.size() + helpMouseContent.size()));
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setFont(descrFont);

			int count = 0;
			for (Integer c = helpKeyContent.firstKey(); c != null; c = helpKeyContent
					.higherKey(c)) {
				SimpleEntry<String, String> current = helpKeyContent.get(c);
				g.drawString(current.getKey(), firstDescrLineX, firstDescrLineY
						+ count * (descrLineHeight + contentLineHeight));
				++count;
			}
			for (Integer c = helpMouseContent.firstKey(); c != null; c = helpMouseContent
					.higherKey(c)) {
				SimpleEntry<String, String> current = helpMouseContent.get(c);
				g.drawString(current.getKey(), firstDescrLineX, firstDescrLineY
						+ count * (descrLineHeight + contentLineHeight));
				++count;
			}

			count = 0;
			g.setFont(contentFont);
			for (Integer c = helpKeyContent.firstKey(); c != null; c = helpKeyContent
					.higherKey(c)) {
				SimpleEntry<String, String> current = helpKeyContent.get(c);
				g.drawString(current.getValue(), firstContentLineX,
						firstDescrLineY + count
								* (descrLineHeight + contentLineHeight)
								+ descrLineHeight);
				++count;
			}
			for (Integer c = helpMouseContent.firstKey(); c != null; c = helpMouseContent
					.higherKey(c)) {
				SimpleEntry<String, String> current = helpMouseContent.get(c);
				g.drawString(current.getValue(), firstContentLineX,
						firstDescrLineY + count
								* (descrLineHeight + contentLineHeight)
								+ descrLineHeight);
				++count;
			}
		}
	}
}
