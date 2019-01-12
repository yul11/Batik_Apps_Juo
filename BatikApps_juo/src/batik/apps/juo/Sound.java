package batik.apps.juo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import  sun.audio.*;

//import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.scene.media.MediaPlayer;


import javafx.scene.media.*;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.Media;



//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;

public class Sound {
	
	
	
	public void playSound() { 
	    try {
		// Open an input stream  to the audio file.
		InputStream in = new FileInputStream("D:/auxi/fire_truck_1.wav");

		// Create an AudioStream object from the input stream.
		AudioStream as = new AudioStream(in);         

		// Use the static class member "player" from class AudioPlayer to play clip.
		AudioPlayer.player.start(as); 		
		
		Thread.sleep(17000);

		// Similarly, to stop the audio.
		AudioPlayer.player.stop(as); 
	    }
		catch(Exception ex) {
		    System.out.println("Error with playing sound.");
		    ex.printStackTrace();
		}
	} 
	
	public void playSound3() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("D:/auxi/fire_truck_1.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	        Thread.sleep(17000);
	        //clip.setMicrosecondPosition(18000000);
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	

	
	public static void main(String[] args){		
		Sound sound = new Sound();
		sound.playSound();
	}
}
	

