package traffic.external.system.road.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import traffic.external.system.road.RoadIterator;
import traffic.map.entity.Point;
import traffic.map.entity.Road;

/**
 * @author Isaac
 * 
 */
public class XMLRoadIterator extends RoadIterator {

	private Node node = null;
	private double[] axis = null;
	private int lane = 0;
	private byte[] laneInfo;
	private int n = 0;

	public XMLRoadIterator(Node n) {
		node = n;
		while (node != null && !node.getNodeName().equals("road"))
			node = node.getNextSibling();
		axis = new double[4];
	}

	@Override
	public boolean hasNext() {
		return node != null;
	}

	@Override
	public Road next() {
		analysisRoad(node);
		do {
			node = node.getNextSibling();
		} while (node != null && !node.getNodeName().equals("road"));
		return new Road(new Point(axis[0], axis[1]),
				new Point(axis[2], axis[3]), laneInfo);
	}

	private void analysisRoad(Node node) {
		if (node.getNodeName().equals("road")) {
			n = 0;
			for (Node child = node.getFirstChild(); child != null; child = child
					.getNextSibling()) {
				String name = child.getNodeName();
				if (name.equals("start_point") || name.equals("end_point"))
					analysisPoint(child);
				else if (name.equals("lane"))
					analysisLane(child);
				else if (name.equals("lanedescr"))
					analysisLaneDescription(child);
			}
		}
	}
	
	private void analysisLaneDescription(Node node){
		for (Node child=node.getFirstChild(); child!=null; child=child.getNextSibling()){
			String name=child.getNodeName();
			if (name.equals("descr"))
				analysisDirection(child);
		}
	}
	
	private void analysisDirection(Node node){
		laneInfo[lane++] = (byte) Integer.parseInt(node.getTextContent());
		System.out.println(laneInfo[lane-1]);
	}

	private void analysisPoint(Node node) {
		NamedNodeMap nodeMap = node.getAttributes();
		axis[n++] = Double.parseDouble(nodeMap.getNamedItem("x-axis")
				.getNodeValue());
		axis[n++] = Double.parseDouble(nodeMap.getNamedItem("y-axis")
				.getNodeValue());
	}

	private void analysisLane(Node node) {
		lane = Integer.parseInt(node.getTextContent());
		laneInfo = new byte[lane];
		lane = 0;
	}
}
