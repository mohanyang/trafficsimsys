package traffic.console.graphic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MapKeyListener implements KeyListener {

	private MapDisplayPanel container = null;

	public MapKeyListener(MapDisplayPanel c) {
		container = c;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
			container.moveUp();
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
			container.moveLeft();
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
			container.moveDown();
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
			container.moveRight();
			break;
		case KeyEvent.VK_PAGE_UP:
			container.zoomPanel.newCursor(1);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			container.zoomPanel.newCursor(-1);
			break;
		case KeyEvent.VK_F1:
			new HelpPanel();
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// do nothing
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// do nothing
	}

}
