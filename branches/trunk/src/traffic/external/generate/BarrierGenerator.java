package traffic.external.generate;

import java.util.LinkedList;

public class BarrierGenerator implements GenerateController{
	public BarrierGenerator(){
	}
	
	public static synchronized BarrierGenerator getInstance(){
		if(instance==null){
			instance=new BarrierGenerator();
		}
		return instance;
	}
	
	public int generate(){
		return 0;
	}
	
	public Object getparameter(int index){
		switch(index){
		case 0:
 
		default:
			System.out.print("error parameter\n");
			return -1;
		}
	}
	
	public int setparameter(Object o, int index){
		switch(index){
		case 0:
			break;
		default:
			System.out.print("error parameter\n");
			return -1;
		}
		return -1;
	}
	
	public int setallparameter(LinkedList list){
		int size=list.size();
		if(size!=paranum){
			System.out.print("wrong parameter number\n");
			return -1;
		}
		for(int i=0;i<size;i++){
			if(setparameter(list.removeFirst(),i)<0){
				return -1;
			}
		}		
		return 0;
	}

	private static BarrierGenerator instance=null;
	
	private static int paranum=4;
}
