package traffic.map.entity;

/**
 * @author Isaac
 * 
 */
public class VehicleInf {
	int imageID;
	int maxspeed;
	int initspeed;

	public VehicleInf(int imageID, int maxspeed, int initspeed) {
		this.imageID = imageID;
		this.maxspeed=maxspeed;
		this.initspeed=initspeed;
		if(maxspeed<initspeed){
			initspeed=maxspeed;
			System.out.print("initial speed exceeds max speed\n");
		}
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
