package batik.apps.juo;

import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.awt.Dimension;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.bridge.BridgeExtension;
import org.apache.batik.script.Interpreter;
import org.apache.batik.script.Window;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherAdapter;
import org.apache.batik.swing.svg.SVGLoadEventDispatcherEvent;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;



@SuppressWarnings("unused")
public class SVGInteractor extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	//SVG namespace string to be used throughout the application
	private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
	private JSVGCanvas canvas = new JSVGCanvas();
	private Document document; // The SVG document
	//private org.apache.batik.bridge.Window window;     // The window object
	private Window window;     // The window object
	private Thread thread;
	private CircleMovement circleMov;
	private SquareMovement squareMov;
	private LineMovement lineMove;
	JPanel panel;

	
	
	public SVGInteractor(){
		
		super("SVG Interactor");		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();		
		canvas.setMySize(new Dimension(600, 400));
		
		//Force the canvas to always be dynamic
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		
						
		Interpreter interpreter;		
		try {
			URL myURL = new URL("http://example.com/");
			org.apache.batik.bridge.RhinoInterpreterFactory fact = new org.apache.batik.bridge.RhinoInterpreterFactory();
			interpreter = fact.createInterpreter(myURL, true);
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// Obtain the Window reference when it becomes available
		canvas.addSVGLoadEventDispatcherListener(
			new SVGLoadEventDispatcherAdapter(){
			
				public void svgLoadEventDispatchStarted(  
						SVGLoadEventDispatcherEvent e) {
						//window = canvas.getUpdateManager().getScriptingEnvironment().createWindow(interpreter,svgNS);
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
		
		Element line = document.createElementNS(svgNS, "line");
		line.setAttributeNS(null, "x1", "300");					
		line.setAttributeNS(null, "y1", "200");
		line.setAttributeNS(null, "x2", "300");
		line.setAttributeNS(null, "y2", "10");
		line.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		line.setAttributeNS(null, "id", "theLine");		


		
		
		
		Element rect2 = document.createElementNS(svgNS, "rect");
		rect2.setAttributeNS(null, "fill", "plum");
		rect2.setAttributeNS(null, "stroke", "indigo");
		rect2.setAttributeNS(null, "stroke-width", "5");
		rect2.setAttributeNS(null, "width", "160");
		rect2.setAttributeNS(null, "height", "160");
		rect2.setAttributeNS(null, "x", "400");
		rect2.setAttributeNS(null, "y", "150");
		rect2.setAttributeNS(null, "id", "theSquare");
		
		rect2.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linejoin:round");
		
		rect2.setAttributeNS(null, "linear-gradient", "to right, red, orange, yellow, green, blue, indigo, green");
		
		NodeList nl = rect2.getElementsByTagName(svgNS);
		nl.getLength();
		
		String str_tn = rect2.getTagName();
		
		 
		
		
		//line.setAttributeNS(null, "filter", "stroke:rgb(0,255,0);stroke-width:10");
		//line33.setAttributeNS(null,  "filter x", "0" );
		//line33.setAttributeNS(null,  "filter y", "0" );
		//line33.setAttributeNS(null,  "width", "200%" );		
		//line33.setAttributeNS(null, "transform", "translate(100,100) rotate(90)");		
		//line33.setAttributeNS(null, "feGaussianBlur", "500");
		
		
		
		root.appendChild(circle);
		root.appendChild(square);
		root.appendChild(line);
		root.appendChild(rect2);
		
		
		//Attach the listeners to the shapes	
		registerListeners();
		
		// Complete the construction of the program window
		panel.add(canvas);
		
		this.setContentPane(panel);
		this.pack();
		this.setBounds(150,150,this.getWidth(),this.getHeight());		
	
	}	
	//end SVGInteractor()-Konstruktor


	

	
	
	
	//This method attaches all the required listeners
	//to those elements in the document which we want to make interactive
	public void registerListeners(){

		
		//Get a reference to the line and cast it as an EventTarget
		EventTarget t3 = (EventTarget)document.getElementById("theLine");
		
		// Add to the line a listener for the ‘click’ event
		t3.addEventListener("click",new EventListener() {
		//t3.addEventListener("mouseover",new EventListener() {	
			public void handleEvent(Event evt) {
				System.out.println("click Greetings from the line!");
				Element elt = document.getElementById("theLine");				
				
				lineMove = new LineMovement(document,canvas);
				lineMove.starte();
			}
		}
		,false);	
		
		t3.addEventListener("mouseout",new EventListener() {	
			public void handleEvent(Event evt) {
				System.out.println("mouseout Greetings from the line!");
				Element elt = document.getElementById("theLine");								
				lineMove.stoppe();
			}
		}
		,false);	
		

				
				
		
				
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
				
				System.out.println("bin in mouseout theCircle!");
				stoppeCircleMoveThread(circleMov);    //juo added on 15.12.2018				
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
		
		// Add to the square a listener for the ‘click’ event
		t2.addEventListener("click",new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("Greetings from the Square!");
				Element elt = document.getElementById("theSquare");
				
				String str_w = elt.getAttribute("width");				
				Integer int_w = Integer.parseInt(str_w);
				System.out.println("int_w: " + int_w);				
				int_w = int_w+10;				
				elt.setAttribute("width", Integer.toString(int_w));
				
				
				String str_h = elt.getAttribute("height");				
				Integer int_h = Integer.parseInt(str_h);
				System.out.println("int_h: " + int_h);				
				int_h = int_h+10;				
				elt.setAttribute("height", Integer.toString(int_h));
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
				elt.setAttributeNS(null, "height", "200");
				
				elt.setAttribute( "attributeName", "transform");
				elt.setAttribute( "attributeType", "XML");
				elt.setAttribute( "type", "rotate");
				elt.setAttribute( "dur", 10 + "s");
				elt.setAttribute( "repeatCount", "indefinite");
				elt.setAttribute( "from", "0 "+360+" "+120);
				elt.setAttribute( "to", 120+" "+360+" "+120);
				
				starteSquareMoveThread();  //juo added on 18.11.2018								
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
				elt.setAttributeNS(null, "height", "160");
				
				System.out.println("bin in mouseout theSquare!");
				stoppeSquareMoveThread(squareMov);    //juo added on 15.12.2018
			}
		}
		,false);
		
		//Add to the square a listener for the 'mousemove' event
		t2.addEventListener("mousemove",new EventListener() {
			public void handleEvent(Event evt) {
				Element elt = document.getElementById("theSquare");
				
				String str_fill = elt.getAttribute("fill");
				System.out.println("str_fill: " + str_fill);
				
				if (str_fill.equals("plum"))
					elt.setAttribute("fill","red");
				
				if (str_fill.equals("red")){				
					elt.setAttribute("fill","green");
				}

				if (str_fill.equals("green")){				
					elt.setAttribute("fill","plum");
				}				
				System.out.println("bin in mousemove theSquare!");				
			}
		}
		,false);
		

	}
	
	
	

	
	

	
	
	// An implementation of the EventListener interface,
	//to work as a ‘click’ event listener for the circle
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
			starteCircleMoveThread();    //juo added on 18.11.2018
		}
	}

	
	
	
	
	//CIRCLE START
	public void starteCircleMoveThread(){		
		//CircleMovement mov = new CircleMovement();	 //original
		circleMov = new CircleMovement();	
		circleMov.starte();
	}
	
	public void stoppeCircleMoveThread(CircleMovement mov){
		System.out.println("rufe circleMov.stoppe() auf!!!");
		mov.stoppe();			
	}
	
	//An inner class encapsulating the laws of the circles movement
	public class CircleMovement implements Runnable{	
				
		private int deltaY = 10;
				
		public void starte(){
			thread = new Thread(this); 
			thread.start();
		}				
		
		public void stoppe(){
			System.out.println("stoppe CircleMovement-thread now");
			thread.stop();
		}
		
		public void run(){	
			
			while (!Thread.currentThread().isInterrupted()) {
								
				System.out.println("CircleMovement-thread laeuft...");
				
				try {
					Thread.sleep(100);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
								
				// Returns immediately
				canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
				    // Insert some actions on the DOM here
					public void run(){													
						
						System.out.println("bin in invokeLater theCircle");							
						
						Element elt = document.getElementById("theCircle");				
						
						int yPos = Integer.parseInt(elt.getAttribute("cy"));
						
						if ((yPos <= 70) || (yPos >=330)){
							deltaY  = - deltaY;
							System.out.println("deltaY: " +deltaY);
						}
							
						yPos += deltaY;	
						
						elt.setAttribute("cy", "" + yPos);
						System.out.println("yPos: " + yPos);
						
					}
				});								
			}
		}
	}
	//CIRCLE END

	



	
	
	//SQUARE START
	public void starteSquareMoveThread(){		
	    squareMov = new SquareMovement();			
	    squareMov.starte();
	}
	
	public void stoppeSquareMoveThread(SquareMovement mov){
		System.out.println("rufe SquareMovement-thread auf!!!");
		squareMov.stoppe();			
	}

	//An inner class encapsulating the laws of the square movement
	public class SquareMovement implements Runnable{
				
		public void starte(){
			thread = new Thread(this); 
			thread.start();
		}
		
		public void stoppe(){
			System.out.println("stoppe SquareMovement-thread now");
			thread.stop();
		}

		
		
		
		public void aktualisiere(){
			panel.repaint();
		}

		
		
		
		private int deltaX = 2;
		
		public void run(){				

			while (!Thread.currentThread().isInterrupted()) {
			
				System.out.println("SquareMovement-thread laeuft");								
				try {
					Thread.sleep(10);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
								
				// Returns immediately
				canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
					public void run(){													
						
						System.out.println("bin in invokeLater theSquare");	
						
						Element elt = document.getElementById("theSquare");
						int xPos = Integer.parseInt(elt.getAttribute("x"));
						
						if (xPos <= 0 || (xPos >= 440))
						   deltaX  = - deltaX;
						
						xPos += deltaX;							
						elt.setAttribute("x", "" + xPos);	
						
					}
				});				
			}
		}
	}
	//SQUARE END

	
	
	//Entry point into the program
	public static void main(String[] args){		
		SVGInteractor inter = new SVGInteractor();
		inter.setVisible(true);
	}
}
