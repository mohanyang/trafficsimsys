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
	private double position;
	
	public RoadEntranceInfo(Road r, int l){
		this(r, l, 0);
	}
	
	public RoadEntranceInfo(Road r, int l, double p){
		assoc=r;
		lane=l;
		position=p;
	}
	
	public Road getRoad(){
		return assoc;
	}
	
	public int getLane(){
		return lane;
	}
	
	public double getPosition(){
		return position;
	}
	
	public double getClosestDistance(){
		return assoc.closestDistance(0, lane, null);
	}

}
