package traffic.event;

import java.awt.event.MouseEvent;

public class MouseInput {
	private MouseEvent mouseEvent;
	private int x, y;
	private boolean click;

	public MouseInput(MouseEvent e, int x, int y, boolean click) {
		mouseEvent = e;
		this.x = x;
		this.y = y;
		this.click = click;
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

	public boolean isClick() {
		return click;
	}
}
