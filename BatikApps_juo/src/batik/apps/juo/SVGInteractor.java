package batik.apps.juo;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherAdapter;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherEvent;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;



@SuppressWarnings("unused")
public class SVGInteractor extends JFrame{
	

	private static final long serialVersionUID = 1L;
	
	//SVG namespace string to be used throughout the application
	private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
	private JSVGCanvas canvas = new JSVGCanvas();
	private Document document; // The SVG document
	//private org.apache.batik.bridge.Window window;     // The window object
	private Window window;     // The window object
	
	
	public SVGInteractor(){
		
		super("SVG Interactor");		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();		
		canvas.setMySize(new Dimension(600, 400));
		
		//Force the canvas to always be dynamic
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		
		// Obtain the Window reference when it becomes available
		canvas.addSVGLoadEventDispatcherListener(
				new SVGLoadEventDispatcherAdapter(){
			
					public void svgLoadEventDispatchStarted(  
							
							SVGLoadEventDispatcherEvent e) {
							//window = canvas.getUpdateManager().getScriptingEnvironment().createWindow();
					}
			}
		);

		//Create the document and attach it to the canvas		
		DOMImplementation dom = SVGDOMImplementation.getDOMImplementation();
		document = dom.createDocument(svgNS, "svg", null);
		canvas.setDocument(document);
		
		// Get a reference to the <svg> element
		Element root = document.getDocumentElement();
		
		// Create and append to the root a couple of basic shapes
		Element circle = document.createElementNS(svgNS, "circle");
		circle.setAttributeNS(null, "fill", "lightsteelblue");					
		circle.setAttributeNS(null, "stroke", "darkslateblue");
		circle.setAttributeNS(null, "stroke-width", "5");
		circle.setAttributeNS(null, "r", "70");
		circle.setAttributeNS(null, "cx", "120");
		circle.setAttributeNS(null, "cy", "180");
		circle.setAttributeNS(null, "id", "theCircle");
		
		Element square = document.createElementNS(svgNS, "rect");
		square.setAttributeNS(null, "fill", "plum");
		square.setAttributeNS(null, "stroke", "indigo");
		square.setAttributeNS(null, "stroke-width", "5");
		square.setAttributeNS(null, "width", "160");
		square.setAttributeNS(null, "height", "160");
		square.setAttributeNS(null, "x", "360");
		square.setAttributeNS(null, "y", "120");
		square.setAttributeNS(null, "id", "theSquare");
		
		root.appendChild(circle);
		root.appendChild(square);
		
		//Attach the listeners to the shapes	
		registerListeners();
		
		// Complete the construction of the program window
		panel.add(canvas);
		this.setContentPane(panel);
		this.pack();
		this.setBounds(150,150,this.getWidth(),this.getHeight());
	
	}
	

	
	//This mehtod attaches all the required listeners
	//to those elements in the document which we want to make interactive
	public void registerListeners(){
		
		//Get a reference to the circle and cast it as an EventTarget
		EventTarget t1 = (EventTarget)document.getElementById("theCircle");
		
		//Add a listener for the click-event
		t1.addEventListener("click",new OnClickCircleAction(), false);
		
		//Add a listener for the mouseover-event
		t1.addEventListener("mouseover",new OnMouseOverCircleAction(),false);
		
		//Add a listener for the mouseout event
		t1.addEventListener("mouseout",new EventListener(){
			public void handleEvent(Event evt){
				Element elt = document.getElementById("theCircle");
				elt.setAttribute("fill","lightsteelblue");
			}
		}
		,false);
		
		
		//Add a listener for the SVGLoad event		
		t1.addEventListener("SVGLoad", new EventListener(){			
			public void handleEvent(Event evt){
				//window.setInterval(new CircleMovement(),20);
			}
		}
		,false);

		
		
		// Get a reference to the square as an event target
		EventTarget t2 = (EventTarget)document.getElementById("theSquare");
		
		// Add to the square a listener for the �click� event
		t2.addEventListener("click",new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("Greetings from the Square!");
			}
		}
		,false);	

		
		
		
		//Add to the square a listener for the 'mouseover' event
		t2.addEventListener("mouseover",new EventListener(){
			public void handleEvent(Event evt) {
				Element elt = document.getElementById("theSquare");
				elt.setAttribute("x","340");
				//elt.setAttribute("y","l00");
				elt.setAttribute("width","200");
				//elt.setAttribute("height","200");
				
				elt.setAttribute("attributeName", "transform");
				elt.setAttribute( "attributeType", "XML");
				elt.setAttribute( "type", "rotate");
				elt.setAttribute( "dur", 10 + "s");
				elt.setAttribute( "repeatCount", "indefinite");
				elt.setAttribute( "from", "0 "+360+" "+120);
				elt.setAttribute( "to", 120+" "+360+" "+120);
			}
		}
		,false);

		
		
		
		
		//Add to the square a listener for the 'mouseout' event
		t2.addEventListener("mouseout",new EventListener() {
			public void handleEvent(Event evt) {
				Element elt = document.getElementById("theSquare");
				elt.setAttribute("x", "360");
				//elt.setAttribute("y","l20");
				elt.setAttribute("width","160");
				//elt.setAttribute("height","l60");
			}
		}
		,false);
	}
	
	
	

	
	

	
	
	// An implementation of the EventListener interface,
	//to work as a �click� event listener for the circle
	public class OnClickCircleAction implements EventListener {
		public void handleEvent(Event evt) {
			//window.alert("Hi from the Circle");
			System.out.println("Hi from the Circle");
		}
	}

	// An implementation of the EventListener interface,
	// to work as a 'mouseover' event listener for the circle
	public class OnMouseOverCircleAction implements EventListener {
		public void handleEvent(Event evt) {			
			Element elt = document.getElementById("theCircle");			
			elt.setAttribute("fill","yellow");			
			elt.setAttribute("fill-opacity",".5");

		}
	}
		

		
	//An inner class encapsulating the laws of the circles movement
	public class CircleMovement implements Runnable{		
		private int deltaY = 1;
		
		public void run(){			
		Element elt = document.getElementById("theCircle");
				int yPos = Integer.parseInt(elt.getAttribute("cy"));

				if (yPos <= 70 || (yPos >= 330))
				   deltaY  = - deltaY;
				
				yPos += deltaY;							
				elt.setAttribute("cy", "" + yPos);
		}
	}		
		
		
		
	//An inner class encapsulating the laws of the square movement
	public class SquareMovement implements Runnable{		
		private int deltaX = 2;
		
		public void run(){			
		Element elt = document.getElementById("theSquare");
				int xPos = Integer.parseInt(elt.getAttribute("x"));

				if (xPos <= 0 || (xPos >= 440))
				   deltaX  = - deltaX;
				
				xPos += deltaX;							
				elt.setAttribute("x", "" + xPos);
		}
	}				
	


	
	
	//Entry point into the program
	public static void main(String[] args){		
		SVGInteractor inter = new SVGInteractor();
		inter.setVisible(true);
	}	
}
