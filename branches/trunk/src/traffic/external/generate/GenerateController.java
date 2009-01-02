package traffic.external.generate;

import java.util.LinkedList;

//@author liangda li

public interface GenerateController {
	public int generate();
	public Object getparameter(int index);
	public int setparameter(Object o, int index);
	public int setallparameter(LinkedList list);
}
