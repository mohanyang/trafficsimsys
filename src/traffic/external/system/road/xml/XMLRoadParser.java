package traffic.external.system.road.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import traffic.external.system.road.RoadIterator;

/**
 * @author Isaac
 * 
 */
public class XMLRoadParser {
	private Document doc = null;

	public XMLRoadParser(String file) {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new File(file));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		analysisDoc();
	}

	private Node itr = null;

	public RoadIterator iterator() {
		return new XMLRoadIterator(itr);
	}

	private void analysisDoc() {
		if (doc.getNodeName().equals("#document"))
			analysisMap(doc.getFirstChild());
	}

	private void analysisMap(Node node) {
		if (node.getNodeName().equals("map")) {
			itr = node.getFirstChild();
		}
	}
}