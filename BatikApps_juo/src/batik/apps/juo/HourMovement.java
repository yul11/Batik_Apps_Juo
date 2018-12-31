package batik.apps.juo;


import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class HourMovement implements Runnable{	
	
	Document document;
	JSVGCanvas canvas;
	private Thread thread;
	String mx= "300";  //Mittelpunkt x-Koordinate
	String my= "300";  //Mittelpunkt y-Koordinate
	int int_x  =300;
	int int_y = 300;

	private int[] handCoordinates;
	CoordinatesGenerator coorGen;
	
	public HourMovement(Document d, JSVGCanvas c){		
		this.document =d;
		this.canvas   =c;
		coorGen = new CoordinatesGenerator();
		int_x  =300;
		int_y = 300;

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void starte(){
		thread = new Thread(this); 
		thread.start();
	}		
	
	public void stoppe(){
		System.out.println("stoppe HourMovement-thread now");
		this.thread.stop();
	}

	
	public void run(){	
		
		while (!Thread.currentThread().isInterrupted()) {
			
			System.out.println("HourMovement-thread laeuft");	
			try {
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
				public void run(){													
					
					handCoordinates = coorGen.getCoordinates();				
					int_x = handCoordinates[4];
					int_y = handCoordinates[5];
					
					System.out.println("HourMovement()-> int_x: " + int_x);
					System.out.println("HourMovement()-> int_y: " + int_y + "\n");		
					
					Element elt = document.getElementById("theHoursHand");
					System.out.println("HourMovement()-> style: " + elt.getAttribute("style"));
					
					elt.setAttributeNS(null,"x1", mx);
					elt.setAttributeNS(null,"y1", my);
					elt.setAttributeNS(null,"x2", Integer.toString(int_x));
					elt.setAttributeNS(null,"y2", Integer.toString(int_y));
					
					System.out.println("x1()-> : " + mx);
					System.out.println("y1()-> : " + my);
					System.out.println("x2()-> : " + Integer.toString(int_x+Integer.parseInt(mx)));
					System.out.println("y2()-> : " + Integer.toString(int_y+Integer.parseInt(my)) + "\n");
				}
			});								
		}
	}
}
