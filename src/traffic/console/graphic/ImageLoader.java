package traffic.console.graphic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage loadImage(int index) throws IOException {
		try {
			switch (index){
			case 0:
				return ImageIO.read(new File("./image/1.gif"));
			case 1:
				return ImageIO.read(new File("./image/2.gif"));
			case 2:
				return ImageIO.read(new File("./image/3.gif"));
			case 3:
				return ImageIO.read(new File("./image/4.gif"));
			default:
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
