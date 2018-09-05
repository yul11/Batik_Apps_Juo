package batik.apps.juo;
//https://stackoverflow.com/questions/30092651/where-has-org-apache-batik-dom-svg-svgdomimplementation-gone

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;




public class Dom {
		
	
	public void juosTest(){	
		
		// Obtaining a DOM implementation
		DOMImplementation dom = SVGDOMImplementation.getDOMImplementation();	
		
		//Creating a Document
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document document = dom.createDocument(svgNS,  "svg", null);	
		
		//Getting the root element
		Element root = document.getDocumentElement();
		
		//Setting the width and heigth attributes of the root element
		root.setAttributeNS(null,"width","400");
		root.setAttributeNS(null,"heigth","400");
		
		//Creating the first circle
		Element circle1 = document.createElementNS(svgNS, "circle");	
		circle1.setAttributeNS(null,"fill","yellow");			
		circle1.setAttributeNS(null, "stroke", "lime");		
		circle1.setAttributeNS(null, "stroke-widht", "20");		
		circle1.setAttributeNS(null, "r", "45");		
		circle1.setAttributeNS(null, "cx", "197");		
		circle1.setAttributeNS(null, "cy", "110");
		
		//Attaching the circle to the root element
		root.appendChild(circle1);		
	}	
	
	
	
	public static void main(String[] args){
		
		Dom d = new Dom();
		d.juosTest();		
    } 
}
