package batik.apps.juo;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class TetSiren extends Object implements LineListener {

//public class Main extends Object{
	File soundFile;
	JDialog playingDialog;
	Clip clip;

	public static void main(String[] args) throws Exception {
		new TetSiren();
	}

	public TetSiren() throws Exception {

		JFrame f = new JFrame();
		File soundFile = new File("D:/auxi/fire_truck_1.wav");

		System.out.println("Playing " + soundFile.getName());

		Line.Info linfo = new Line.Info(Clip.class);
		Line line = AudioSystem.getLine(linfo);
		clip = (Clip) line;
		clip.addLineListener(this);

		AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
		clip.open(ais);
		//clip.setMicrosecondPosition(0);
		System.out.println("length: " + clip.getMicrosecondLength());
		
		clip.start();
		Thread.sleep(17000);
		clip.setMicrosecondPosition(20000000);
	}

	public void update(LineEvent le) {

		LineEvent.Type type = le.getType();
		if (type == LineEvent.Type.OPEN) {
			System.out.println("OPEN");

		} else if (type == LineEvent.Type.CLOSE) {
			System.out.println("CLOSE");
			System.exit(0);

		} else if (type == LineEvent.Type.START) {
			System.out.println("START");
			playingDialog.setVisible(true);

		} else if (type == LineEvent.Type.STOP) {
			System.out.println("STOP");
			playingDialog.setVisible(false);
			clip.close();
		}
	}
}