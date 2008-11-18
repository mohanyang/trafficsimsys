package traffic.map.entity;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import traffic.basic.Lib;

public class MapTest {

	Map map = null;
	LinkedList<Point> list = null;

	@Test
	public void testMap() {
		Lib.seedRandom(0);
		map = new Map();
		System.out.println(map + " created");
		for (int i = 0; i < 10000; ++i)
			map.newPoint(Lib.random() * 100, Lib.random() * 100);
		System.out.println("point created");

		list = new LinkedList<Point>();
		for (Iterator<Point> itr = map.getPointList(); itr.hasNext();) {
			Point p = itr.next();
			list.add(p);
		}
		System.out.println("total point count " + list.size());

		int size = list.size();
		for (int i = 0; i < 100000; ++i) {
			Point b = list.get(Lib.random(size)), e = list
					.get(Lib.random(size));
			map.newRoad(b.xAxis, b.yAxis, e.xAxis, e.yAxis, Lib.random(8));
		}
		System.out.println("road created");
	}
}
