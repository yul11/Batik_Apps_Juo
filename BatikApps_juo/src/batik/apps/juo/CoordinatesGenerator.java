package batik.apps.juo;

import org.w3c.dom.Document;

public class CoordinatesGenerator implements Runnable{

	private int[] handCoordinates;
	private Thread thread;
	Uhr_Basis ub;
	Document document;

	public CoordinatesGenerator(Document d, Uhr_Basis ub) {  			
		handCoordinates = new int[6];
		thread = new Thread(this);
		thread.start();
		this.document = d;
		this.ub = ub;
	} 
	
	public int[] getCoordinates(){
		return this.handCoordinates;
	}


	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			handCoordinates = ub.getHandCoordinates();
			for (int i=0; i<handCoordinates.length; i++){
				handCoordinates = ub.getHandCoordinates();
			}

			try {
				Thread.sleep(1);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}




