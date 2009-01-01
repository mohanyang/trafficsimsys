package traffic.external.generate;

//@author liangda li

public class MyFactory {
	private VehicleGenerator vehiclegen;
	private BarrierGenerator barriergen;
	
	private static MyFactory instance=null;
	
	public static synchronized MyFactory getInstance(){
		if(instance==null){
			instance=new MyFactory();
		}
		return instance;
	}
	
	public VehicleGenerator getVehicleGenerator(){
		if(vehiclegen==null){
			try{
				vehiclegen=(VehicleGenerator)Class.forName("VehicleGenerator").newInstance();
			}catch (Exception e){
				System.out.print("VehicleGenerator construct error\n");
			}
		}
		return vehiclegen;
	}
	
	public BarrierGenerator getBarrierGenerator(){
		if(barriergen==null){
			try{
				barriergen=(BarrierGenerator)Class.forName("BarrierGenerator").newInstance();
			}catch (Exception e){
				System.out.print("BarrierGenerator construct error\n");
			}
		}
		return barriergen;
	}
}
