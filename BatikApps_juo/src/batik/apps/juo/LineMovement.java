package batik.apps.juo;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;




//An inner class encapsulating the laws of the circles movement
public class LineMovement implements Runnable{	
	
	private int deltaY = 10;
	Document document;
	JSVGCanvas canvas;
	private Thread thread;

		
	
	public LineMovement(Document d, JSVGCanvas c){		
		this.document =d;
		this.canvas   =c;
	}

	public void starte(){
		thread = new Thread(this); 
		thread.start();
	}		
	
	
	
	public void run(){	
		
		while (!Thread.currentThread().isInterrupted()) {
							
			System.out.println("LineMovement-thread laeuft...");
			
			try {
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
							
			// Returns immediately
			//canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
			    // Insert some actions on the DOM here
				//public void run(){													
					
					System.out.println("LineMovement()-> bin in invokeLater line");	
					
					Element elt = document.getElementById("theLine");
					System.out.println("LineMovement()-> style: " + elt.getAttribute("style"));
										
					String str_x2= elt.getAttribute("x2");
					System.out.println("str_x2: " + str_x2);										
					int xPos = Integer.parseInt(elt.getAttribute("x2"));					
					xPos = xPos +10;					
					elt.setAttribute("x2", "" + xPos);

					String str_y2= elt.getAttribute("y2");
					System.out.println("str_y2: " + str_y2);										
					int yPos = Integer.parseInt(elt.getAttribute("y2"));					
					yPos = yPos -10;					
					elt.setAttribute("y2", "" + yPos);
					
					
					
				}
			//});								
		}
	}
//}
//LINE END
