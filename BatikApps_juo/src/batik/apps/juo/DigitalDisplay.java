package batik.apps.juo;


import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;



public class DigitalDisplay implements Runnable{	
	
	Document document;
	JSVGCanvas canvas;
	private Thread thread;
	String mx= "300";  //Mittelpunkt x-Koordinate
	String my= "300";  //Mittelpunkt y-Koordinate
	int int_x  =300;
	int int_y = 300;
	protected GregorianCalendar heute;
	SVGInteractor svgInter;
	boolean AM;

	
	public DigitalDisplay(Document d, JSVGCanvas c, SVGInteractor s){		
		this.document =d;
		this.canvas   =c;
		this.svgInter =s;
		int_x  =300;
		int_y = 300;

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registerListener();
	}

	
	public void registerListener(){
		EventTarget t1 = (EventTarget)document.getElementById("theDigitalTimeText");
		
		t1.addEventListener("click", new EventListener() {
			public void handleEvent(Event evt) {
				System.out.println("DigitalDisplay()-> clicked DigitalDisplay");				
				if (AM == true){
					AM = false;
					System.out.println("DigitalDisplay()-> setze AM auf false");
				}
				else{
					AM = true;
					System.out.println("DigitalDisplay()-> setze AM auf true");
				}				
				svgInter.setAM(AM);
			}
		}
	,false);
	}
	
	

	public void starte(){
		thread = new Thread(this); 
		thread.start();
	}		
	
	public void stoppe(){
		System.out.println("stoppe DigitalDisplay-thread now");
		this.thread.stop();
	}

	
	public void run(){	
		
		while (!Thread.currentThread().isInterrupted()) {
			
			heute = new GregorianCalendar();
						
			try {
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
				public void run(){					
					
					int sec = heute.get(Calendar.SECOND);
					String str_sec = Integer.toString(sec);
					if (sec < 10)					
						str_sec = "0" + (sec);			
					
					int min = heute.get(Calendar.MINUTE);
					String str_min = Integer.toString(min);
					if (min < 10)					
						str_min = "0" + (min);
										
					Element elt = document.getElementById("theDigitalTimeText");
					
					elt.setTextContent(heute.get(Calendar.HOUR_OF_DAY) + ":" + str_min + ":" + str_sec);					
				}
			});								
		}
	}
}
