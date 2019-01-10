package batik.apps.juo;

public class CalculateTime {

	public static void main(String[] args) {
		
		
		double time;
		double winkel;
		double zwoelf = 12;
		double dreihundertsechzig = 360;
		
		winkel = 180; //entspricht 06:00 Uhr
		winkel = 300; //entspricht 06:30 Uhr
		
		time = (zwoelf / dreihundertsechzig) * winkel;
		
		System.out.println("winkel: " + winkel + " time: " + time);
		
		System.out.println("Minuten: " + time *60);
		

	}
}
