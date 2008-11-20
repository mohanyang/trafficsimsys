package traffic.map.handler;

import org.junit.Test;

import traffic.basic.Config;

/**
 * @author Isaac
 * 
 */
public class LoadHandlerTest {

	@Test
	public void testRun() {
		Config.load("traffic.ini");
		new LoadHandler().run();
	}
}
