package batik.apps.juo;


public class CoordinatesGenerator implements Runnable{

	private int[] handCoordinates;
	private Thread thread;
	Uhr_Basis ub;

	public CoordinatesGenerator() {  			
		handCoordinates = new int[6];
		ub = new Uhr_Basis();
		thread = new Thread(this);
		thread.start();
	} 
	
	public int[] getCoordinates(){
		return this.handCoordinates;
	}


	public void run() {

		while (!Thread.currentThread().isInterrupted()) {

			handCoordinates = ub.getHandCoordinates();
			for (int i=0; i<handCoordinates.length; i++){
				handCoordinates = ub.getHandCoordinates();
				//System.out.println("handcoordinates_________: " + handCoordinates[i]);
			}

			//System.out.println("\n");	
			try {
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new CoordinatesGenerator();
	}
}




