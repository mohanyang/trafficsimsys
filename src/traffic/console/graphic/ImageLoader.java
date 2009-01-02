package traffic.console.graphic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {

	public static BufferedImage loadImage(int index) throws IOException {
		try {
			switch (index) {
			// case 0:
			// return ImageIO.read(new File("./image/1.gif"));
			// case 1:
			// return ImageIO.read(new File("./image/2.gif"));
			// case 2:
			// return ImageIO.read(new File("./image/3.gif"));
			// case 3:
			// return ImageIO.read(new File("./image/4.gif"));
			case 0:
				return ImageIO.read(new File("./image/1.gif"));
			case 1:
				return ImageIO.read(new File("./image/2.gif"));
			case 2:
				return ImageIO.read(new File("./image/3.gif"));
			case 3:
				return ImageIO.read(new File("./image/4.gif"));
			case 4:
				return ImageIO.read(new File("./image/5.gif"));
			case 5:
				return ImageIO.read(new File("./image/6.gif"));
			case 6:
				return ImageIO.read(new File("./image/7.gif"));
			case 7:
				return ImageIO.read(new File("./image/8.gif"));
			case 8:
				return ImageIO.read(new File("./image/9.gif"));
			case 9:
				return ImageIO.read(new File("./image/10.gif"));
			case 10:
				return ImageIO.read(new File("./image/11.gif"));
			case 11:
				return ImageIO.read(new File("./image/12.gif"));
			case 12:
				return ImageIO.read(new File("./image/13.gif"));
			case 13:
				return ImageIO.read(new File("./image/14.gif"));
			case 14:
				return ImageIO.read(new File("./image/15.gif"));
			default:
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
