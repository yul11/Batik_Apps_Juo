package batik.apps.juo;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Gradients {	

	public static final String COOL_RADIAL_GRADIENT_ID  = "cr_grad";
	public static final String VERTICAL_GRADIENT_ID     = "v_grad";
	public static final String PHOTO_GRADIENT_ID        = "picture";
	
	
	public static void deleteDefs(Document doc){
		
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Element root = doc.getDocumentElement();		
		
		try{
			System.out.println("defs ist:  " + root.getAttribute("defs"));
			System.out.println("style ist: " + root.getAttribute("style"));
			
			
			NodeList nList = doc.getElementsByTagName("string");
			System.out.println("nList.length ist: " + nList.getLength());

		    for (int temp = 0; temp < nList.getLength(); temp++) {
		        Node nNode = nList.item(temp);
		        System.out.println("\nCurrent Element :" + nNode.getNodeName());
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		            Element eElement = (Element) nNode;
		            System.out.println("Order number: " + 
		            eElement.getTextContent());
		         }
		     }
		   
			
	        Node firstChild = doc.getFirstChild();
	        System.out.println(firstChild.getChildNodes().getLength());
	        System.out.println(firstChild.getNodeType());
	        System.out.println(firstChild.getNodeName());

	        Node rootx = doc.getDocumentElement();
	        System.out.println(rootx.getChildNodes().getLength());
	        System.out.println(rootx.getNodeType());
	        System.out.println(rootx.getNodeName());
			
						
			NodeList cl = root.getChildNodes();
			
			for (int i=0; i<cl.getLength(); i++){
				
				Node n = cl.item(i);
				System.out.println("parentNode: " + n.getParentNode());
				System.out.println("firstChild: " + n.getFirstChild());
				System.out.println("lastChild:  " + n.getLastChild());				
			}
			
			Element defs = null;
			NodeList nl = root.getElementsByTagNameNS(svgNS, "defs");			
			
			for (int i=0; i<nl.getLength(); i++){
				System.out.println("Inhalt item " +i + " " +nl.item(i));
				
				Node nod = nl.item(i);
				System.out.println("nodeVal ist: " + nod.getNodeValue());
				System.out.println("nodeName ist: " + nod.getNodeName());
			}
						
			System.out.println("NodeList hat noch funktioniert, Länge: " +nl.getLength());
			
			if (nl.getLength()>0){
				defs = (Element) nl.item(0);								
				doc.removeChild(defs);
				System.out.println("removeChild hat noch funktioniert");				
			}
			else
				System.out.println("Länge NodeList ist 0");
				
			
		}
		catch (Exception e){
			System.out.println("deleteDefs() Exception: " + e.getMessage());
		}
	}


	public static void insertCoolPictureAfrica(Document doc) {	
		
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
				
		Element img = doc.createElementNS(svgNS, "image");						
		img.setAttributeNS(null, "x", "0");		
		img.setAttributeNS(null, "y", "0");
		img.setAttributeNS(null, "width", "120");
		img.setAttributeNS(null, "height", "120");
	    img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "file:///D:/PC/Software/Java/Batik2/svg_Dateien/African-Girl-2.svg"); 	    

	    pat.appendChild(img);				
		defs.appendChild(pat);			
	}
	
	public static void insertCoolPictureRedGirl(Document doc) {	
		
		String mx= "300";  //Mittelpunkt x-Koordinate
		String my= "300";  //Mittelpunkt y-Koordinate
		
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;			
		Element defs = getOrCreateDefs(doc);
		
		Element pat2 = doc.createElementNS(svgNS, "pattern");
		pat2.setAttributeNS(null, "id", PHOTO_GRADIENT_ID+"2");
		pat2.setAttributeNS(null, "x", mx);		
		pat2.setAttributeNS(null, "y", my);	
		pat2.setAttributeNS(null, "width", "120");
		pat2.setAttributeNS(null, "height", "120");
		pat2.setAttributeNS(null, "patternUnits", "userSpaceOnUse");
				
		Element img2 = doc.createElementNS(svgNS, "image");						
		img2.setAttributeNS(null, "x", "0");		
		img2.setAttributeNS(null, "y", "0");
		img2.setAttributeNS(null, "width", "120");
		img2.setAttributeNS(null, "height", "120");
		img2.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "file:///D:/PC/Software/Java/Batik2/svg_Dateien/erstauntMaedchen.svg");

		pat2.appendChild(img2);				
		defs.appendChild(pat2);			
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

//possible pictures
//img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "https://mdn.mozillademos.org/files/6457/mdn_logo_only_color.png");
//img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "https://mdn.mozillademos.org/files/6461/mdn_logo_only_color.png"); 		
//img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "https://www.bacb.de/wp-content/uploads/2016/03/audi-logo.png"); 		
//img.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", "" + "https://openclipart.org/download/190044/ccAzqg7fg9uzPw.svg"); 
