package traffic.console.graphic;

import javax.swing.JFrame;
import javax.swing.UIManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public class SplashWindow extends JFrame {

	public SplashWindow(){
		super();
		this.setSize(800, 600);
		Simple temp=new Simple();
		temp.setVisible(true);
		this.add(temp);
		this.setVisible(true);
	}
		
	private class Simple extends JPanel{
		Image img;
		public Simple(){
			super();
			try {
				img=ImageLoader.loadImage(0);
			}
			catch (Exception e){
				
			}
			setSize(800, 600);
		}
		
	    public Dimension getPreferredSize() {
	        return new Dimension(250,200);
	    }

		public void paintComponents(Graphics g){
			super.paintComponent(g);
//			graph.drawImage(img, 0, 0, this);
			g.drawString("abc", 100, 100);
		}
	}
}
