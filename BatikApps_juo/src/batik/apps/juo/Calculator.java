package batik.apps.juo;

import java.awt.Point;
import java.text.NumberFormat;

public class Calculator {
	
	
	public static Integer[] convertAngleToTime(Point p1, Point p2){
		
		Integer[] hourMin = new Integer[2];
		
		double gegenKat = (p2.getX()-p1.getX()); 
		double anKat    = (p1.getY()-p2.getY());
		
		System.out.println("juo: Calculator.convertAngleToTime()->  Gegenkathete: " + gegenKat);
		System.out.println("juo: Calculator.convertAngleToTime()->  Ankathete: " + anKat);			
		System.out.println("juo: Calculator.convertAngleToTime()->  Quotient: " + gegenKat/anKat);			
		
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
		String minute = str_uhrzeit.substring(index+1,index+13);  //hier gibt es ein Problem bei 03:00 Uhr, 06:00 Uhr
																  //09:00 Uhr und 12:00 Uhr. Quotient ist 0!
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
		
		hourMin[0]=int_hour;
		hourMin[1]=int_minute;
		return hourMin;		
	}
}
