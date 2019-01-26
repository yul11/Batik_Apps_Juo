package batik.apps.juo;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AlarmControl implements Runnable{

	
	Document document;
	Uhr_Basis uhr_basis;
	private Thread thread;
	boolean doRun = true;
	boolean alarm = false;
	boolean alarmRuns = false;
	private CircleMovement circle_mov;
	private JSVGCanvas canvas;


	
    public AlarmControl(Document d, Uhr_Basis ub, JSVGCanvas c) {  
    	this.document  = d;
    	this.uhr_basis = ub;
    	this.canvas = c;
    }
 
    
	public void starte(){
		thread = new Thread(this); 
		thread.start();
	}
    
	public boolean getAlarmStatus(){
		return alarm;
	}
 
	public void run() {
		
		while (doRun){

			GregorianCalendar heute = uhr_basis.getTime();

			System.out.println("juo: AlarmControl()-> heute.HOUR_OF_DAY: " + heute.get(Calendar.HOUR_OF_DAY));
			System.out.println("juo: AlarmControl()-> heute.MINUTE:      " + heute.get(Calendar.MINUTE));
			System.out.println("juo: AlarmControl()-> heute.SECOND:      " + heute.get(Calendar.SECOND));	
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Element elt = document.getElementById("theAlarmTimeText");
			
			String str_aux = elt.getTextContent();
			
			if (str_aux.contains(":")){
				System.out.println("str_aux.indexOf(:) " + str_aux.indexOf(":"));
			
				String str_hour, str_min;			
				str_hour = str_aux.substring(str_aux.indexOf(":")-3, str_aux.indexOf(":"));
				str_min  = str_aux.substring(str_aux.indexOf(":")+1, str_aux.indexOf(":")+3);
				
				System.out.println("juo: str_hour: " + str_hour.trim());
				System.out.println("juo: str_min:  " + str_min.trim());				
				
				Integer int_hour = Integer.parseInt(str_hour.trim()); 
				Integer int_min = Integer.parseInt(str_min.trim()); 
				System.out.println("juo: int_hour: " + int_hour);
				System.out.println("juo: int_min:  " + int_min);
				
				if ((int_hour == heute.get(Calendar.HOUR_OF_DAY)) && (int_min == heute.get(Calendar.MINUTE))){					
					System.out.println("juo:                                   Weckzeit jetzt!");
					alarm = true;
				}
				else
					alarm = false;
				
				if (alarm){
					if (!alarmRuns){
						circle_mov = new CircleMovement(canvas, document);	
						circle_mov.starte();
						alarmRuns = true;					
					}
				}
				else{
					if (alarmRuns){
						circle_mov.stoppe();
						alarmRuns = false;											
					}
				}
			}
		}		
	} 
}
