package batik.apps.juo;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Gradients {	

	public static final String COOL_RADIAL_GRADIENT_ID  = "cr_grad";
	public static final String VERTICAL_GRADIENT_ID     = "v_grad";
	public static final String PHOTO_GRADIENT_ID        = "picture";
	// id in <pattern id="picture" x="20..."



	public static void insertCoolPicture(Document doc) {	
		
		String mx= "300";  //Mittelpunkt x-Koordinate
		String my= "300";  //Mittelpunkt y-Koordinate
		
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;	
		
		Element defs = getOrCreateDefs(doc);
		
		Element pat = doc.createElementNS(svgNS, "pattern");
		pat.setAttributeNS(null, "id", PHOTO_GRADIENT_ID);
		pat.setAttributeNS(null, "x", mx);		
		pat.setAttributeNS(null, "y", my);	
		pat.setAttributeNS(null, "width", "120");
		pat.setAttributeNS(null, "height", "120");
		pat.setAttributeNS(null, "patternUnits", "userSpaceOnUse");
		//pat.setAttributeNS(null, "patternUnits", "objectBoundingBox");
				
		Element img = doc.createElementNS(svgNS, "image");						
		//img.setAttributeNS(null, "x", "315");		
		//img.setAttributeNS(null, "y", "150");
		img.setAttributeNS(null, "x", "0");		
		img.setAttributeNS(null, "y", "0");
		img.setAttributeNS(null, "width", "120");
		img.setAttributeNS(null, "height", "120");
		//img.setAttributeNS(null, "xlink\\:href", "audi_logo.jpg");		
		//img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "https://mdn.mozillademos.org/files/6457/mdn_logo_only_color.png");
	    img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "https://mdn.mozillademos.org/files/6461/mdn_logo_only_color.png"); 		
	    //img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "https://www.bacb.de/wp-content/uploads/2016/03/audi-logo.png"); 		

	    
		pat.appendChild(img);				
		defs.appendChild(pat);			
	}

	
	
	public static void insertCoolRadialGradient(Document doc) {		
	
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;			
		Element defs = getOrCreateDefs(doc);
		
		Element gradient = doc.createElementNS(svgNS, "radialGradient");		
		gradient.setAttributeNS(null, "id", COOL_RADIAL_GRADIENT_ID);		
		gradient.setAttributeNS(null, "fx", "70%");		
		gradient.setAttributeNS(null, "fy", "50%");
		
		Element stop1 = doc.createElementNS(svgNS, "stop");		
		stop1.setAttributeNS(null,"offset", "0%");		
		stop1.setAttributeNS(null, "stop-color", "#FF0000"); //red
		gradient.appendChild(stop1);
		
		Element stop2 = doc.createElementNS(svgNS, "stop");
		stop2.setAttributeNS(null, "offset", "100%");
		stop2.setAttributeNS(null, "stop-color","#00FF00");	 //green
		gradient.appendChild(stop2);
		
		defs.appendChild(gradient);				
	}

	
	
	public static void insertVerticalGradient(Document doc) {
			
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Element defs = getOrCreateDefs(doc);
	
		Element gradient = doc.createElementNS(svgNS, "linearGradient");
		gradient.setAttributeNS(null, "id", VERTICAL_GRADIENT_ID);
		gradient.setAttributeNS(null, "x1","0%");
		gradient.setAttributeNS(null, "x2","0%");
		gradient.setAttributeNS(null, "y1","0%");
	    gradient.setAttributeNS(null, "y2","100%");

		Element stopl = doc.createElementNS(svgNS, "stop");
		stopl.setAttributeNS(null,"offset", "0%");
		stopl.setAttributeNS(null, "stop-color","#666");
		gradient.appendChild(stopl);
		
		Element stop2 = doc.createElementNS(svgNS, "stop");
		stop2.setAttributeNS(null, "offset", "100%");
		stop2.setAttributeNS(null, "stop-color", "#fff");
		gradient.appendChild(stop2);
		defs.appendChild(gradient);
	}

		
	// The document might already have a <defs> element
	// so we check that and if it doesn't, we add one static Element		
	private static Element getOrCreateDefs(Document doc){		

		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Element root = doc.getDocumentElement();
		Element defs = null;
		NodeList nl = root.getElementsByTagNameNS(svgNS, "defs");
			
		if (nl.getLength() > 0) {
			defs = (Element) nl.item(0);
		} 
		else {
			defs = doc.createElementNS(svgNS , "defs"); 
			root.appendChild(defs);
		}
		return defs;
	}
}