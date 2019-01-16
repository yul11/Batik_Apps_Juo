package batik.apps.juo;

//sound-examples from:
//http://www.pacdv.com/sounds/transportation_sounds.html

import java.io.FileInputStream;
import java.io.InputStream;
import  sun.audio.*;


public class Sound {
		
	public static AudioStream soundStart() { 
		AudioStream as=null;
	    try {
	    	InputStream in = new FileInputStream("D:/PC/Software/Java/git/Batik_Apps_Juo2/BatikApps_juo/src/batik/apps/juo/bottle_pop_3.wav");
			//InputStream in = new FileInputStream("D:/PC/Software/Java/git/Batik_Apps_Juo2/BatikApps_juo/src/batik/apps/juo/fire_truck_1.wav");	
			//InputStream in = new FileInputStream("D:/PC/Software/Java/git/Batik_Apps_Juo2/BatikApps_juo/src/batik/apps/juo/dhol_drums.wav");
			as = new AudioStream(in);         
			AudioPlayer.player.start(as);
	    }
		catch(Exception ex) {
		    System.out.println("juo: Sound.soundStart()-> Error with playing sound.");
		    ex.printStackTrace();
		}
		return as;
	}
	
	public static void soundStop(AudioStream as) { 
	    try {
	    	AudioPlayer.player.stop(as); 
	    }
		catch(Exception ex) {
		    System.out.println("juo: Sound.soundStop()-> Error with playing sound.");
		    ex.printStackTrace();
		}
	} 	
	
	public static void main(String[] args){		
		Sound.soundStart();
	}	
}
	

