package batik.apps.juo;

import java.awt.event.MouseMotionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
import java.net.URL;
import java.text.NumberFormat;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Point;

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
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MouseEvent;

import sun.audio.AudioStream;

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
	int r = 220;
	private int clickCt;
	Setup s = null;
	Uhr_Basis ub;
	boolean alarmAdjustmentInProgress;
	boolean AM;
	AudioStream as= null;
	
	
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
		Element digitalTimeTextElement = document.createElementNS(svgNS, "text");
		Text digitalTimeText = document.createTextNode("textNode3");
		digitalTimeText.setNodeValue("Zeit: ");
		digitalTimeTextElement.appendChild(digitalTimeText);		
		digitalTimeTextElement.setAttributeNS(null, "x", "100");
		digitalTimeTextElement.setAttributeNS(null, "y", "75");
		digitalTimeTextElement.setAttributeNS(null, "font-size", "70");
		digitalTimeTextElement.setAttributeNS(null, "font-family", "AR DECODE");
		digitalTimeTextElement.setAttributeNS(null, "fill", "black");
		digitalTimeTextElement.setAttributeNS(null, "id", "theDigitalTimeText");		
		
		Element alarmTimeTextElement = document.createElementNS(svgNS, "text");
		Text text = document.createTextNode("textNode");
		text.setNodeValue("AlarmTimeText");
		alarmTimeTextElement.appendChild(text);		
		alarmTimeTextElement.setAttributeNS(null, "x", "100");
		alarmTimeTextElement.setAttributeNS(null, "y", "550");
		alarmTimeTextElement.setAttributeNS(null, "font-size", "25");
		alarmTimeTextElement.setAttributeNS(null, "fill", "red");
		alarmTimeTextElement.setAttributeNS(null, "id", "theAlarmTimeText");
		
		
		Element normalTimeTextElement = document.createElementNS(svgNS, "text");
		Text normalTimeText = document.createTextNode("textNode2");
		normalTimeText.setNodeValue("normalTimeText");
		normalTimeTextElement.appendChild(normalTimeText);		
		normalTimeTextElement.setAttributeNS(null, "x", "300");
		normalTimeTextElement.setAttributeNS(null, "y", "550");
		normalTimeTextElement.setAttributeNS(null, "font-size", "25");
		normalTimeTextElement.setAttributeNS(null, "fill", "green");
		normalTimeTextElement.setAttributeNS(null, "id", "TheNormalTimeTextElement");			
		
		
		Element circle = document.createElementNS(svgNS, "circle");
		circle.setAttributeNS(null, "stroke", "green");
		circle.setAttributeNS(null, "stroke-width", "5");
		circle.setAttributeNS(null, "r", "50");
		Integer int_xC = Integer.parseInt(mx);
		int_xC = int_xC +r;
		Integer int_yC = Integer.parseInt(my);
		int_yC = int_yC -r;
		circle.setAttributeNS(null, "cx", Integer.toString(int_xC));
		circle.setAttributeNS(null, "cy", Integer.toString(int_yC));
		circle.setAttributeNS(null, "id", "theCircle");
		Gradients.insertCoolRadialGradient(document);
		circle.setAttributeNS(null, "fill","url(#" + Gradients.COOL_RADIAL_GRADIENT_ID + ")");
		
		Element square = document.createElementNS(svgNS, "rect");
		square.setAttributeNS(null, "stroke", "grey");
		square.setAttributeNS(null, "stroke-width", "1");
		square.setAttributeNS(null, "width", "400");
		square.setAttributeNS(null, "height", "20");
		Integer int_x = Integer.parseInt(mx);
		int_x = int_x -r;
		Integer int_y = Integer.parseInt(my);
		int_y = int_y + 260;
		square.setAttributeNS(null, "x", Integer.toString(int_x));
		square.setAttributeNS(null, "y", Integer.toString(int_y));
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

		
        // Striche bei 1,2,3,4,5,6,7,8,9,10,11,12 Uhr
		r=r-20;
		int length = 80;
		
		Element clockMarkZwoelf = document.createElementNS(svgNS, "line");
		clockMarkZwoelf.setAttributeNS(null, "fill", "red");		
		clockMarkZwoelf.setAttributeNS(null, "stroke", "red");
		clockMarkZwoelf.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkZwoelf.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkZwoelf.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r-length)*Math.cos(Math.toRadians(270)))));					
		clockMarkZwoelf.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r-length)*Math.sin(Math.toRadians(270)))));
		clockMarkZwoelf.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)((r+length)*Math.cos(Math.toRadians(270)))));
		clockMarkZwoelf.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)((r+length)*Math.sin(Math.toRadians(270)))));		

		Element clockMarkEins = document.createElementNS(svgNS, "line");
		clockMarkEins.setAttributeNS(null, "fill", "red");		
		clockMarkEins.setAttributeNS(null, "stroke", "red");
		clockMarkEins.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkEins.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkEins.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.cos(Math.toRadians(300)))));					
		clockMarkEins.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.sin(Math.toRadians(300)))));
		clockMarkEins.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.cos(Math.toRadians(300)))));
		clockMarkEins.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.sin(Math.toRadians(300)))));		

		Element clockMarkZwei = document.createElementNS(svgNS, "line");
		clockMarkZwei.setAttributeNS(null, "fill", "red");		
		clockMarkZwei.setAttributeNS(null, "stroke", "red");
		clockMarkZwei.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkZwei.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkZwei.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.cos(Math.toRadians(330)))));					
		clockMarkZwei.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.sin(Math.toRadians(330)))));
		clockMarkZwei.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.cos(Math.toRadians(330)))));
		clockMarkZwei.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.sin(Math.toRadians(330)))));		

		Element clockMarkDrei = document.createElementNS(svgNS, "line");
		clockMarkDrei.setAttributeNS(null, "fill", "red");		
		clockMarkDrei.setAttributeNS(null, "stroke", "red");
		clockMarkDrei.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkDrei.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkDrei.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r-length)*Math.cos(Math.toRadians(0)))));					
		clockMarkDrei.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r-length)*Math.sin(Math.toRadians(0)))));
		clockMarkDrei.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)((r+length)*Math.cos(Math.toRadians(0)))));
		clockMarkDrei.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)((r+length)*Math.sin(Math.toRadians(0)))));		

		Element clockMarkVier = document.createElementNS(svgNS, "line");
		clockMarkVier.setAttributeNS(null, "fill", "red");		
		clockMarkVier.setAttributeNS(null, "stroke", "red");
		clockMarkVier.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkVier.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkVier.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.cos(Math.toRadians(30)))));					
		clockMarkVier.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.sin(Math.toRadians(30)))));
		clockMarkVier.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.cos(Math.toRadians(30)))));
		clockMarkVier.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.sin(Math.toRadians(30)))));		

		Element clockMarkFuenf = document.createElementNS(svgNS, "line");
		clockMarkFuenf.setAttributeNS(null, "fill", "red");		
		clockMarkFuenf.setAttributeNS(null, "stroke", "red");
		clockMarkFuenf.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkFuenf.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkFuenf.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.cos(Math.toRadians(60)))));					
		clockMarkFuenf.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.sin(Math.toRadians(60)))));
		clockMarkFuenf.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.cos(Math.toRadians(60)))));
		clockMarkFuenf.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.sin(Math.toRadians(60)))));		
		
		Element clockMarkSechs = document.createElementNS(svgNS, "line");
		clockMarkSechs.setAttributeNS(null, "fill", "red");		
		clockMarkSechs.setAttributeNS(null, "stroke", "red");
		clockMarkSechs.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkSechs.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkSechs.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r-length)*Math.cos(Math.toRadians(90)))));					
		clockMarkSechs.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r-length)*Math.sin(Math.toRadians(90)))));
		clockMarkSechs.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)((r+length)*Math.cos(Math.toRadians(90)))));
		clockMarkSechs.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)((r+length)*Math.sin(Math.toRadians(90)))));		

		Element clockMarkSieben = document.createElementNS(svgNS, "line");
		clockMarkSieben.setAttributeNS(null, "fill", "red");		
		clockMarkSieben.setAttributeNS(null, "stroke", "red");
		clockMarkSieben.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkSieben.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkSieben.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.cos(Math.toRadians(120)))));					
		clockMarkSieben.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.sin(Math.toRadians(120)))));
		clockMarkSieben.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.cos(Math.toRadians(120)))));
		clockMarkSieben.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.sin(Math.toRadians(120)))));		

		Element clockMarkAcht = document.createElementNS(svgNS, "line");
		clockMarkAcht.setAttributeNS(null, "fill", "red");		
		clockMarkAcht.setAttributeNS(null, "stroke", "red");
		clockMarkAcht.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkAcht.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkAcht.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.cos(Math.toRadians(150)))));					
		clockMarkAcht.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.sin(Math.toRadians(150)))));
		clockMarkAcht.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.cos(Math.toRadians(150)))));
		clockMarkAcht.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.sin(Math.toRadians(150)))));		

		Element clockMarkNeun = document.createElementNS(svgNS, "line");
		clockMarkNeun.setAttributeNS(null, "fill", "red");		
		clockMarkNeun.setAttributeNS(null, "stroke", "red");
		clockMarkNeun.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkNeun.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkNeun.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r-length)*Math.cos(Math.toRadians(180)))));					
		clockMarkNeun.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r-length)*Math.sin(Math.toRadians(180)))));
		clockMarkNeun.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)((r+length)*Math.cos(Math.toRadians(180)))));
		clockMarkNeun.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)((r+length)*Math.sin(Math.toRadians(180)))));		
		
		Element clockMarkZehn = document.createElementNS(svgNS, "line");
		clockMarkZehn.setAttributeNS(null, "fill", "red");		
		clockMarkZehn.setAttributeNS(null, "stroke", "red");
		clockMarkZehn.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkZehn.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkZehn.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.cos(Math.toRadians(210)))));					
		clockMarkZehn.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.sin(Math.toRadians(210)))));
		clockMarkZehn.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.cos(Math.toRadians(210)))));
		clockMarkZehn.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.sin(Math.toRadians(210)))));		

		Element clockMarkElf = document.createElementNS(svgNS, "line");
		clockMarkElf.setAttributeNS(null, "fill", "red");		
		clockMarkElf.setAttributeNS(null, "stroke", "red");
		clockMarkElf.setAttributeNS(null, "style", "stroke:rgb(255,0,0); stroke-width:10; stroke-linecap:round");
		clockMarkElf.setAttributeNS(null, "id", "theClockMarks");
		Gradients.insertCoolRadialGradient(document);
		clockMarkElf.setAttributeNS(null, "x1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.cos(Math.toRadians(240)))));					
		clockMarkElf.setAttributeNS(null, "y1", Integer.toString(Integer.parseInt(mx) + (int)((r)*Math.sin(Math.toRadians(240)))));
		clockMarkElf.setAttributeNS(null, "x2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.cos(Math.toRadians(240)))));
		clockMarkElf.setAttributeNS(null, "y2", Integer.toString(Integer.parseInt(mx) + (int)(r*Math.sin(Math.toRadians(240)))));		

		root.appendChild(rect2);
		root.appendChild(clockFace);
		
		root.appendChild(clockMarkZwoelf);
		root.appendChild(clockMarkEins);
		root.appendChild(clockMarkZwei);
		root.appendChild(clockMarkDrei);
		root.appendChild(clockMarkVier);
		root.appendChild(clockMarkFuenf);
		root.appendChild(clockMarkSechs);
		root.appendChild(clockMarkSieben);
		root.appendChild(clockMarkAcht);
		root.appendChild(clockMarkNeun);
		root.appendChild(clockMarkZehn);
		root.appendChild(clockMarkElf);
				
		root.appendChild(circle);
		root.appendChild(square);		
		
		root.appendChild(hoursHand);		
		root.appendChild(minutesHand);
		root.appendChild(secondsHand);
		root.appendChild(minutesHandAlarm);
		root.appendChild(alarmTimeTextElement);
		root.appendChild(normalTimeTextElement);
		root.appendChild(digitalTimeTextElement);
		
										
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
                
        DigitalDisplay dd = new DigitalDisplay(document,canvas,this);
        dd.starte();
	}	
	//end SVGInteractor()-Konstruktor

	
	public void setAM(boolean val){
		this.AM = val;		
	}

	
	//This method attaches all the required listeners
	//to those elements in the document which we want to make interactive
	public void registerListeners(){
		
		//Get a reference to the line and cast it as an EventTarget
		EventTarget t7 = (EventTarget)document.getElementById("theMinutesHandAlarm");		
		t7.addEventListener("mousedown", new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("mousedown MinuteHandAlarm");				
				alarmAdjustmentInProgress=true;
				System.out.println("t7 set alarmAdjustmentInProgress to:  " + alarmAdjustmentInProgress);
			}						
		}
		,false);
		
		EventTarget t9 = (EventTarget)document.getElementById("theMinutesHandAlarm");		
		t9.addEventListener("mouseup", new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("mouseup MinuteHandAlarm");
				alarmAdjustmentInProgress=false;				
				
				Element elt = document.getElementById("theAlarmTimeText");
				String str_text = elt.getTextContent();
				System.out.println("str_text: " + str_text);
				elt.setAttributeNS(null, "x1", "300");					
				elt.setAttributeNS(null, "y1", "300");				
				MouseEvent mevt = (MouseEvent)evt;
				System.out.println("clientX: " + mevt.getClientX());
				System.out.println("clientY: " + mevt.getClientY());				
				elt.setAttributeNS(null, "x2", Integer.toString(mevt.getClientX()));
    			elt.setAttributeNS(null, "y2", Integer.toString(mevt.getClientY()));					
				
				Point p1 = new Point(300,300);
				Point p2 = new Point(mevt.getClientX(),mevt.getClientY());
												
				Integer[] hourMin = Calculator.convertAngleToTime(p1, p2, AM);
				
				System.out.println("eventTarget mouseup hourMin[0]" + hourMin[0]);
				System.out.println("eventTarget mouseup hourMin[1]" + hourMin[1]);
				
				String str_min = hourMin[1].toString();
				if (hourMin[1] < 10){					
					str_min = "0" + (hourMin[1].toString());
				}			
				elt.setTextContent("Alarm: " + hourMin[0] + ":" + str_min + " Uhr");
				
			}						
		}
		,false);
		
		EventTarget t8 = (EventTarget)document.getElementById("theClockFace");		
		t8.addEventListener("mousemove", new EventListener() {
			public void handleEvent(Event evt) {
				
				//modify minuteHand Begin
				System.out.println("mousemove MinuteHandAlarm");
				Element el_minHand = document.getElementById("theMinutesHandAlarm");
								
				el_minHand.setAttributeNS(null, "x1", "300");					
				el_minHand.setAttributeNS(null, "y1", "300");
				
				MouseEvent mevt = (MouseEvent)evt;
				System.out.println("clientX: " + mevt.getClientX());
				System.out.println("clientY: " + mevt.getClientY());
				
				System.out.println("t8 alarmAdjustmentInProgress: " + alarmAdjustmentInProgress);
				if (alarmAdjustmentInProgress){
					el_minHand.setAttributeNS(null, "x2", Integer.toString(mevt.getClientX()));
					el_minHand.setAttributeNS(null, "y2", Integer.toString(mevt.getClientY()));					
				}				
				Point p1 = new Point(300,300);
				Point p2 = new Point(mevt.getClientX(),mevt.getClientY());												
				Integer[] hourMin = Calculator.convertAngleToTime(p1, p2, AM);				
				System.out.println("eventTarget mousemove hourMin[0]" + hourMin[0]);
				System.out.println("eventTarget mousemove hourMin[1]" + hourMin[1]);
				//modify minuteHand End
				
				//modify normalTimeTextElement Begin
				Element elt = document.getElementById("TheNormalTimeTextElement");
				String str_text = elt.getTextContent();
				String str_min = hourMin[1].toString();
				if (hourMin[1] < 10){					
					str_min = "0" + (hourMin[1].toString());
				}			
				elt.setTextContent("Time: " + hourMin[0] + ":" + str_min + " Uhr");
				//modify normalTimeTextElement End
			}						
		}
		,false);
		
		
		
		EventTarget t3 = (EventTarget)document.getElementById("theSecondsHand");
		// Add to the line a listener for the ‘click’ event
		t3.addEventListener("click",new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("click  Greetings from the SecondHand");
					
				
				Color selectedColor = JColorChooser.showDialog(null,"Farbe Sekundenzeiger auswählen", null);
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
				Color selectedColor = JColorChooser.showDialog(null,"Farbe Minutenzeiger auswählen", null);
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
				Color selectedColor = JColorChooser.showDialog(null,"Farbe Stundenzeiger auswählen", null);
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
		
		// Add to the square a listener for the ‘click’ event
		t2.addEventListener("click",new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("Greetings from the Square, click !");
				Element elt = document.getElementById("theSquare");
				
				String str_w = elt.getAttribute("width");				
				Integer int_w = Integer.parseInt(str_w);
				System.out.println("int_w: " + int_w);				
				int_w = int_w+1;				
				elt.setAttribute("width", Integer.toString(int_w));								
				String str_h = elt.getAttribute("height");				
				Integer int_h = Integer.parseInt(str_h);
				System.out.println("int_h: " + int_h);				
				int_h = int_h+1;				
				elt.setAttribute("height", Integer.toString(int_h));
			}
		}
		,false);	
		
		//Add to the square a listener for the 'mouseover' event
		t2.addEventListener("mouseover",new EventListener(){
			public void handleEvent(Event evt) {
				Element elt = document.getElementById("theSquare");
				
				Integer int_x = Integer.parseInt(mx);
				int_x = int_x -200;				
				elt.setAttribute("x",Integer.toString(int_x));
				elt.setAttribute("width","200");
				elt.setAttributeNS(null, "height", "20");				
				elt.setAttribute( "attributeName", "transform");
				elt.setAttribute( "attributeType", "XML");
				elt.setAttribute( "type", "rotate");
				elt.setAttribute( "dur", 10 + "s");
				elt.setAttribute( "repeatCount", "indefinite");
				elt.setAttribute( "from", "0 "+360+" "+120);
				elt.setAttribute( "to", 120+" "+360+" "+120);				
				starteSquareMoveThread();								
			}
		}
		,false);
		
		//Add to the square a listener for the 'mouseout' event
		t2.addEventListener("mouseout",new EventListener() {
			public void handleEvent(Event evt) {
				Element elt = document.getElementById("theSquare");
				
				Integer int_x = Integer.parseInt(mx);
				int_x = int_x -200;				
				elt.setAttribute("x",Integer.toString(int_x));
				elt.setAttribute("width","80");
				elt.setAttributeNS(null, "height", "20");				
				System.out.println("bin in mouseout theSquare!");
				stoppeSquareMoveThread(squareMov);
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
		
		// Add to the square a listener for the ‘click’ event
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
				
		private double deltaY = 4;
		boolean doRun = true;
		boolean down  = true;
				
		public void starte(){
			thread = new Thread(this);
			thread.start();
		}				
		
		public void stoppe(){
			System.out.println("stoppe CircleMovement-thread now");
			doRun = false;
		}
		
		public void run(){	
			
			while (doRun) {
				
				try {
					Thread.sleep(100);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
								
				// Returns immediately
				canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
					
					public void run(){	
						
						Element elt = document.getElementById("theCircle");

						int yPos = Integer.parseInt(elt.getAttribute("cy"));
						
						if ((yPos <= 50) || (yPos >=550)){
							deltaY  = - deltaY;
							System.out.println("deltaY: " +deltaY);
							
							if (yPos >=550){
								System.out.println("fülle gelb");
								elt.setAttributeNS(null, "fill","yellow");
								elt.setAttributeNS(null, "stroke","yellow");
								elt.setAttributeNS(null, "stroke-width","20");
								down = false;								
								as = Sound.soundStart();						
							}
							if (yPos <= 50){
								System.out.println("fülle gelb");
								elt.setAttributeNS(null, "fill","yellow");
								elt.setAttributeNS(null, "stroke","yellow");
								elt.setAttributeNS(null, "stroke-width","20");	
								down = true;
								Sound.soundStop(as);
							}							
						}
						else{
							if (down){														
								deltaY = (1.0*(yPos/30)*(yPos/30));
								System.out.println("deltaY: " + deltaY + " Bewegung: down");		
							}
							else{								
								deltaY = -(1.0*(yPos/30)*(yPos/30));
								System.out.println("deltaY: " + deltaY + " Bewegung: up");		
							}
						}
							
						yPos += deltaY;
						//System.out.println("(yPos % 100): " + (yPos % 100)); 
												
						if ((yPos % 4) ==0 ){
							elt.setAttributeNS(null, "fill","blue");
							elt.setAttributeNS(null, "stroke","red");
							elt.setAttributeNS(null, "stroke-width","20");	
							elt.setAttribute("stroke-opacity",".7");
																				
						}
						if ((yPos % 8) ==0 ){
							elt.setAttributeNS(null, "fill","red");
							elt.setAttributeNS(null, "stroke","blue");
							elt.setAttributeNS(null, "stroke-width","20");		
							elt.setAttribute("stroke-opacity",".7");												
						}
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
		
		boolean doRun = true;
				
		public void starte(){
			thread = new Thread(this); 
			thread.start();
		}
		
		public void stoppe(){
			System.out.println("stoppe SquareMovement-thread now");
			doRun=false;

		}
		
		private int deltaX = 2;
		
		public void run(){				

			while (doRun) {
			
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
						
						if (xPos <= 0 || (xPos >= 400))
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
