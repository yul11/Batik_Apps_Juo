package batik.apps.juo;

import java.awt.event.MouseMotionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
import java.net.URL;
import java.text.NumberFormat;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.bridge.BridgeExtension;
import org.apache.batik.script.Interpreter;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;
import org.w3c.dom.events.UIEvent;
import org.w3c.dom.views.AbstractView;
import javax.swing.JColorChooser;



@SuppressWarnings("unused")
public class SVGInteractor extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	//SVG namespace string to be used throughout the application
	private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
	private JSVGCanvas canvas = new JSVGCanvas();
	private Document document; // The SVG document
	private Thread thread;
	private CircleMovement circleMov;
	private SquareMovement squareMov;
	private SecondMovement secondMove;
	private MinuteMovement minuteMove;
	private HourMovement   hourMove;

	JPanel panel;
	String mx= "300";  //Mittelpunkt x-Koordinate
	String my= "300";  //Mittelpunkt y-Koordinate
	private int clickCt;
	Setup s = null;
	Uhr_Basis ub;

	
	
	public SVGInteractor(){
		
		super("SVGInteractor");	
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();		
		canvas.setMySize(new Dimension(600, 600));
		
		//Force the canvas to always be dynamic
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);

		//Create the document and attach it to the canvas		
		DOMImplementation dom = SVGDOMImplementation.getDOMImplementation();
		document = dom.createDocument(svgNS, "svg", null);
		canvas.setDocument(document);
					
		// Get a reference to the <svg> element
		Element root = document.getDocumentElement();
		
		// Create and append to the root a couple of basic shapes
		Element circle = document.createElementNS(svgNS, "circle");
		//circle.setAttributeNS(null, "stroke", "darkslateblue");
		circle.setAttributeNS(null, "stroke", "green");

		circle.setAttributeNS(null, "stroke-width", "5");
		circle.setAttributeNS(null, "r", "70");
		circle.setAttributeNS(null, "cx", "120");
		circle.setAttributeNS(null, "cy", "180");
		circle.setAttributeNS(null, "id", "theCircle");
		Gradients.insertCoolRadialGradient(document);
		circle.setAttributeNS(null, "fill","url(#" + Gradients.COOL_RADIAL_GRADIENT_ID + ")");
		
		Element square = document.createElementNS(svgNS, "rect");
		//square.setAttributeNS(null, "fill", "plum");
		square.setAttributeNS(null, "stroke", "indigo");
		square.setAttributeNS(null, "stroke-width", "5");
		square.setAttributeNS(null, "width", "160");
		square.setAttributeNS(null, "height", "160");
		square.setAttributeNS(null, "x", mx);
		square.setAttributeNS(null, "y", my);
		square.setAttributeNS(null, "id", "theSquare");		
		Gradients.insertCoolVerticalGradient(document);		
		square.setAttributeNS(null, "fill","url(#" + Gradients.VERTICAL_GRADIENT_ID + ")");		
		
		Element secondsHand = document.createElementNS(svgNS, "line");
		secondsHand.setAttributeNS(null, "x1", "300");					
		secondsHand.setAttributeNS(null, "y1", "300");
		secondsHand.setAttributeNS(null, "x2", "300");
		secondsHand.setAttributeNS(null, "y2", "100");		
		secondsHand.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		secondsHand.setAttributeNS(null, "id", "theSecondsHand");	
		Gradients.insertCoolRadialGradient(document);
		secondsHand.setAttributeNS(null, "fill","url(#" + Gradients.COOL_RADIAL_GRADIENT_ID + ")");
				
		Element minutesHand = document.createElementNS(svgNS, "line");
		minutesHand.setAttributeNS(null, "x1", "300");					
		minutesHand.setAttributeNS(null, "y1", "300");
		minutesHand.setAttributeNS(null, "x2", "300");
		minutesHand.setAttributeNS(null, "y2", "100");		
		minutesHand.setAttributeNS(null, "style", "stroke:rgb(100,100,100); stroke-width:10; stroke-linecap:round");
		minutesHand.setAttributeNS(null, "id", "theMinutesHand");	
		Gradients.insertCoolRadialGradient(document);
		minutesHand.setAttributeNS(null, "fill","url(#" + Gradients.COOL_RADIAL_GRADIENT_ID + ")");
				
		Element minutesHandAlarm = document.createElementNS(svgNS, "line");
		minutesHandAlarm.setAttributeNS(null, "x1", "300");					
		minutesHandAlarm.setAttributeNS(null, "y1", "300");
		minutesHandAlarm.setAttributeNS(null, "x2", "300");
		minutesHandAlarm.setAttributeNS(null, "y2", "100");		
		minutesHandAlarm.setAttributeNS(null, "style", "stroke:rgb(200,200,200); stroke-width:10; stroke-linecap:round");
		minutesHandAlarm.setAttributeNS(null, "id", "theMinutesHandAlarm");	
		Gradients.insertCoolRadialGradient(document);
		
		Element hoursHand = document.createElementNS(svgNS, "line");
		hoursHand.setAttributeNS(null, "x1", "300");					
		hoursHand.setAttributeNS(null, "y1", "300");
		hoursHand.setAttributeNS(null, "x2", "300");
		hoursHand.setAttributeNS(null, "y2", "100");		
		hoursHand.setAttributeNS(null, "style", "stroke:rgb(100,100,100); stroke-width:10; stroke-linecap:round");
		hoursHand.setAttributeNS(null, "id", "theHoursHand");	
		Gradients.insertCoolRadialGradient(document);
		hoursHand.setAttributeNS(null, "fill","url(#" + Gradients.COOL_RADIAL_GRADIENT_ID + ")");
		
		Element rect2 = document.createElementNS(svgNS, "rect");
		//rect2.setAttributeNS(null, "fill", "plum");
		rect2.setAttributeNS(null, "stroke", "indigo");
		rect2.setAttributeNS(null, "stroke-width", "5");
		rect2.setAttributeNS(null, "x", "400");
		rect2.setAttributeNS(null, "y", "150");
		rect2.setAttributeNS(null, "id", "theSquare2");
		rect2.setAttributeNS(null, "stroke", "black");
		rect2.setAttributeNS(null, "width", "160");
		rect2.setAttributeNS(null, "height", "160");
		Gradients.insertCoolVerticalGradient(document);
		rect2.setAttributeNS(null, "fill","url(#" + Gradients.VERTICAL_GRADIENT_ID + ")");		
		
		// Creates the face (Ziffernblatt) of a clock 
		Element clockFace = document.createElementNS(svgNS, "circle");
		//clockFace.setAttributeNS(null, "fill", "green");					
		clockFace.setAttributeNS(null, "stroke", "yellow");
		clockFace.setAttributeNS(null, "stroke-width", "5");
		clockFace.setAttributeNS(null, "r", "200");
		clockFace.setAttributeNS(null, "cx", mx);
		clockFace.setAttributeNS(null, "cy", my);
		clockFace.setAttributeNS(null, "id", "theClockFace");
		Gradients.insertCoolRadialGradient(document);
		clockFace.setAttributeNS(null, "fill","url(#" + Gradients.COOL_RADIAL_GRADIENT_ID + ")");		
				
		root.appendChild(rect2);
		root.appendChild(clockFace);
		root.appendChild(circle);
		root.appendChild(square);
		
		root.appendChild(hoursHand);		
		root.appendChild(minutesHand);
		root.appendChild(secondsHand);
		root.appendChild(minutesHandAlarm);
								
		//Attach the listeners to the shapes	
		registerListeners();
		
		// Complete the construction of the program window
		panel.add(canvas);
		
		clickCt = 0;		
		this.setContentPane(panel);
		this.pack();
		this.setBounds(600,100,this.getWidth(),this.getHeight());
		
		secondMove = new SecondMovement(document,canvas);
		secondMove.starte();		
		minuteMove = new MinuteMovement(document,canvas);
		minuteMove.starte();		
		hourMove   = new HourMovement(document,canvas);
        hourMove.starte();					
	}	
	//end SVGInteractor()-Konstruktor
	


	
	//This method attaches all the required listeners
	//to those elements in the document which we want to make interactive
	public void registerListeners(){
		
		//Get a reference to the line and cast it as an EventTarget
		
		
		EventTarget t7 = (EventTarget)document.getElementById("theMinutesHandAlarm");		
		t7.addEventListener("mousedown", new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("mousedown MinuteHandAlarm");
				Element elt = document.getElementById("theMinutesHandAlarm");
				
				elt.setAttributeNS(null, "x1", "300");					
				elt.setAttributeNS(null, "y1", "300");
				elt.setAttributeNS(null, "x2", "300");
				elt.setAttributeNS(null, "y2", "500");	
				
				MouseEvent mevt = (MouseEvent)evt;
				System.out.println("clientX: " + mevt.getClientX());
				System.out.println("clientY: " + mevt.getClientY());

			}						
		}
		,false);
		
		
		EventTarget t8 = (EventTarget)document.getElementById("theClockFace");		
		t8.addEventListener("mouseup", new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("mouseup MinuteHandAlarm");
				Element elt = document.getElementById("theMinutesHandAlarm");
				
				MouseEvent mevt = (MouseEvent)evt;
				elt.setAttributeNS(null, "x1", "300");					
				elt.setAttributeNS(null, "y1", "300");
				
				System.out.println("clientX: " + mevt.getClientX());
				System.out.println("clientY: " + mevt.getClientY());

				elt.setAttributeNS(null, "x2", Integer.toString(mevt.getClientX()));
				elt.setAttributeNS(null, "y2", Integer.toString(mevt.getClientY()));
				
				double gegenKat = (mevt.getClientX()-300); 
				double anKat    = (300-(mevt.getClientY()));
				
				System.out.println("Gegenkathete: " + gegenKat);;
				System.out.println("Ankathete:    " + anKat);				
				System.out.println("Quotient: " + gegenKat/anKat);
			
				double winkel = Math.toDegrees(Math.atan2(gegenKat,anKat)); 
				
				System.out.println("winkel: " + winkel);
				
				double time;
				double zwoelf = 12;
				double dreihundertsechzig = 360;
				double uhrzeit = 0;
				
				time = (zwoelf/dreihundertsechzig) * winkel;
				
				System.out.println("winkel: " + winkel + " time: " + time);				
				System.out.println("Minuten: " + time *60);				
				uhrzeit = 12+(time);				
				System.out.println("Uhrzeit: " +"0"+uhrzeit);
				
				String str_uhrzeit = String.valueOf("0"+uhrzeit);

				int index = str_uhrzeit.indexOf('.');
				
				System.out.println("Punkt gefunden an Position: " + index);
				
				String hour   = str_uhrzeit.substring(index-2,index);
				String minute = str_uhrzeit.substring(index+1,index+9);				
				String str_aux = "0."+minute; 				
				Double d_minute = Double.parseDouble(str_aux);
				System.out.println("d_minute: " + d_minute);				
				d_minute = d_minute * 60;
				System.out.println("d_minute:  " + d_minute + " ungerundet");
			
				NumberFormat numf = NumberFormat.getInstance();
				numf.setMaximumFractionDigits(0); // max. 0 stellen hinter komma
				numf.format(d_minute);
				System.out.println("d_minute: " + numf.format(d_minute) + " gerundet");

				
				int int_hour = Integer.parseInt(hour);
				System.out.println("hour: " + hour);
				
				int int_minute = Integer.parseInt(numf.format(d_minute));
				System.out.println("minute: " + int_minute);
			}						
		}
		,false);
		
		
		
		EventTarget t3 = (EventTarget)document.getElementById("theSecondsHand");
		// Add to the line a listener for the �click� event
		t3.addEventListener("click",new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("click  Greetings from the SecondHand");
					
				
				Color selectedColor = JColorChooser.showDialog(null,"Farbe Sekundenzeiger ausw�hlen", null);
				String hex = String.format("#%02x%02x%02x", selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());  
				System.out.println("hex: " + hex);
				Element elt = document.getElementById("theSecondsHand");
				elt.setAttributeNS(null, "style", "stroke:rgb(" + selectedColor.getRed() + "," + selectedColor.getGreen() + "," + selectedColor.getBlue() + "); stroke-width:10; stroke-linecap:round");
			}						
		}
		,false);
		t3.addEventListener("mouseout",new EventListener() {	
			public void handleEvent(Event evt) {
				System.out.println("mouseout Greetings from the SecondHand!");
				Element elt = document.getElementById("theSecondsHand");								
				//secondMove.stoppe();
			}
		}
		,false);
		
		
		EventTarget t5 = (EventTarget)document.getElementById("theMinutesHand");		
		t5.addEventListener("click",new EventListener(){
			public void handleEvent(Event evt) {
				System.out.println("click  Greetings from the MinutesHand!");	
				Color selectedColor = JColorChooser.showDialog(null,"Farbe Minutenzeiger ausw�hlen", null);
				String hex = String.format("#%02x%02x%02x", selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());  
				System.out.println("hex: " + hex);
				Element elt = document.getElementById("theMinutesHand");
				elt.setAttributeNS(null, "style", "stroke:rgb(" + selectedColor.getRed() + "," + selectedColor.getGreen() + "," + selectedColor.getBlue() + "); stroke-width:10; stroke-linecap:round");
			}						
		}
		,false);
		

		
		
		
		
		
				
		
		EventTarget t6 = (EventTarget)document.getElementById("theHoursHand");
		t6.addEventListener("click",new EventListener(){
			public void handleEvent(Event evt) {
				System.out.println("click  Greetings from the HoursHand!");	
				Color selectedColor = JColorChooser.showDialog(null,"Farbe Stundenzeiger ausw�hlen", null);
				String hex = String.format("#%02x%02x%02x", selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());  
				System.out.println("hex: " + hex);
				Element elt = document.getElementById("theHoursHand");
				elt.setAttributeNS(null, "style", "stroke:rgb(" + selectedColor.getRed() + "," + selectedColor.getGreen() + "," + selectedColor.getBlue() + "); stroke-width:10; stroke-linecap:round");
			}						
		}
		,false);
		
				
		//Get a reference to the circle and cast it as an EventTarget
		EventTarget t1 = (EventTarget)document.getElementById("theCircle");
		
		//Add a listener for the click-event
		t1.addEventListener("click",new EventListener(){
			public void handleEvent(Event evt) {
				//window.alert("Hi from the Circle");
				System.out.println("Hi from the Circle");
			}
		},false);

		
		//Add a listener for the mouseover-event
		t1.addEventListener("mouseover",new EventListener(){
			
			public void handleEvent(Event evt) {			
				Element elt = document.getElementById("theCircle");			
				elt.setAttribute("fill","yellow");			
				elt.setAttribute("fill-opacity",".5");			
				starteCircleMoveThread();    //juo added on 18.11.2018
			}						
		}
		,false);

		
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
		
		// Add to the square a listener for the �click� event
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

		
		// Get a reference to the square as an event target
		EventTarget t4 = (EventTarget)document.getElementById("theClockFace");
		
		// Add to the square a listener for the �click� event
		t4.addEventListener("click",new EventListener() {
			public void handleEvent(Event evt) {
				Element elt = document.getElementById("theClockFace");
				String  str_r = elt.getAttribute("r");				
				Integer int_r = Integer.parseInt(str_r);

				String str_stroke = elt.getAttribute("stroke");
				elt.setAttributeNS(null, "stroke", "red");
				
				if ((clickCt % 2)!=1){
					int_r = int_r+10;
					s = new Setup(document, canvas);
				}
				else{
					int_r = int_r-10;
					if (s!=null){
						s.dispose();
						elt.setAttributeNS(null, "stroke", "yellow");
					}
					else{
						System.out.println("Oject s ist null, nothing to dispose!");
					}						
				}
				elt.setAttribute("r", Integer.toString(int_r));
				clickCt++;
			}
		}
		,false);		
	}
	
	

	
	
	
	
	//CIRCLE START
	public void starteCircleMoveThread(){		
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
