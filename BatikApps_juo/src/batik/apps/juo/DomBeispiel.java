package batik.apps.juo;
//https://stackoverflow.com/questions/30092651/where-has-org-apache-batik-dom-svg-svgdomimplementation-gone

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class DomBeispiel {
	
	
	//Beispiel von:
	//http://www.wilfried-grupe.de/DOM.html
	public static void createXML_DOM(){
		org.w3c.dom.Document doc;  
		org.w3c.dom.Element e, ei, de;
		org.w3c.dom.NodeList list;
		org.w3c.dom.Node n;
		java.io.File f;
		org.w3c.dom.Text tn; 
		javax.xml.transform.Transformer transformer;   
		try {
			DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.newDocument();   
			e = doc.createElement("ROOT");   
			doc.appendChild(e);
			for(int i=1; i<10; i++)
			{
				ei = doc.createElement("Zahl");
				tn = doc.createTextNode(Integer.toString(i));
				ei.appendChild(tn);
				e.appendChild(ei);
			}    
			de = doc.getDocumentElement();
			list = de.getChildNodes();
			for(int i=0; i < list.getLength(); i++){
				n = list.item(i);    
				System.out.println(n.getNodeName() + ": " + n.getTextContent());
			}
			f = new  java.io.File("C:/wg/DOMgenerated.xml");
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
			transformer.transform(
					new javax.xml.transform.dom.DOMSource(doc), 
					new javax.xml.transform.stream.StreamResult(f));
		} catch (DOMException e1) {
		} catch (TransformerConfigurationException e1) {
		} catch (IllegalArgumentException e1) {
		} catch (ParserConfigurationException e1) {
		} catch (TransformerFactoryConfigurationError e1) {
		} catch (TransformerException e1) {
		} 
	}
	
	
	
	public static void DOMDemo(String filename) {
		try {
			File file = new File(filename);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("Zahl");
			System.out.println("Information: alle Zahl-Elemente ");
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node nMensch = nodeLst.item(s);
				if (nMensch.getNodeType() == Node.ELEMENT_NODE) {
					System.out.println(nMensch.getNodeName());
					NodeList nlistMensch = nMensch.getChildNodes();
					for (int mi = 0; mi < nlistMensch.getLength(); mi++) {
						Node ni = nlistMensch.item(mi);
						if (!ni.getNodeName().equals("Kauf") && ni.getNodeType() == Node.ELEMENT_NODE) {
							System.out.println(ni.getNodeName() + "\t->" + ni.getChildNodes().item(0).getNodeValue());
						}
					}
				}
			}
		} catch (DOMException e) { e.printStackTrace();
		} catch (ParserConfigurationException e) { e.printStackTrace();
		} catch (SAXException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace();
		}
	}	
	

			
	public static void main(String[] args){
        System.out.println("Ausgabe aus der main()-Methode");
		createXML_DOM();
		DOMDemo("C:/wg/DOMgenerated.xml");
    } 
}
