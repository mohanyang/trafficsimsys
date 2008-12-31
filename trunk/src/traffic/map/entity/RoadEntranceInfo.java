package traffic.map.entity;

/**
 * A RoadEntranceInfo class is simple record 
 * about the intersection point, telling the direction,
 * closest car of a lane joining an intersection,
 * used for interchanging information between
 * classes.
 * 
 * @author huangsx
 *
 */

public class RoadEntranceInfo {
	
	private Road assoc;
	private int lane;
	
	public RoadEntranceInfo(Road r, int l){
		assoc=r;
		lane=l;
	}
	
	public Road getRoad(){
		return assoc;
	}
	
	public int getLane(){
		return lane;
	}
	
	public double getClosestDistance(){
		return assoc.closestDistance(0, lane, null);
	}

}
