package traffic.event;

import java.awt.event.MouseEvent;

public class MouseInput {
	private MouseEvent mouseEvent;
	private int x, y;

	public MouseInput(MouseEvent e, int x, int y) {
		mouseEvent = e;
		this.x = x;
		this.y = y;
	}

	public MouseEvent getMouseEvent() {
		return mouseEvent;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
