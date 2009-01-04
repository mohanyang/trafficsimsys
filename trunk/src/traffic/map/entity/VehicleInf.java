package traffic.map.entity;

import traffic.console.graphic.ImageLoader;

/**
 * @author Isaac
 * 
 */
public class VehicleInf {
	int imageID;
	int maxspeed;
	int initspeed;
	
	int length;

	public VehicleInf(int imageID, int maxspeed, int initspeed){
		this.imageID = imageID;
		this.maxspeed=maxspeed;
		this.initspeed=initspeed;
		try {
			this.length = (int)Math.floor(ImageLoader.loadImage(imageID).getHeight()
					/ (1.0*ImageLoader.loadImage(imageID).getWidth()/Road.laneWidth));
		}
		catch (Exception e){
			this.length = 40;
		}
		if(maxspeed<initspeed){
			initspeed=maxspeed;
			System.err.print("initial speed exceeds max speed\n");
		}
	}
	
	public int getLength() {
		return length;
	}

	public int getImageID() {
		return imageID;
	}
	
	public int getmaxspeed(){
		return maxspeed;
	}
	
	public int getinitspeed(){
		return initspeed;
	}
}
