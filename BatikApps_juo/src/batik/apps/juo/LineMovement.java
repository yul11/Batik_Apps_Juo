package batik.apps.juo;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;




public class LineMovement implements Runnable{	
	
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
									
			try {
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
							
			//canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
				//public void run(){													
					
					System.out.println("LineMovement()-> bin in invokeLater line");	
					
				
					int sec=0;
					
					while (true){
					
						
						Element elt = document.getElementById("theLine");
						System.out.println("LineMovement()-> style: " + elt.getAttribute("style"));

						
						double alf = (90 - 6 * sec) * Math.PI / 180;
						double x = Math.cos(alf) *190;
						double y = Math.sin(alf) *190;
						
						System.out.println("LineMovement()-> sec: " + sec);
						
						int int_x = (int)x;
						int int_y = (int)y;
						System.out.println("LineMovement()-> int_x: " + int_x);
						System.out.println("LineMovement()-> int_y: " + int_y + "\n");
						

						
						elt.setAttributeNS(null,"x1", "300");
						elt.setAttributeNS(null,"y1", "200");
						elt.setAttributeNS(null,"x2", Integer.toString(int_x+300));
						elt.setAttributeNS(null,"y2", Integer.toString(int_y+200));
						
						
						System.out.println("x1()-> : 300");
						System.out.println("y1()-> : 200");
						System.out.println("x2()-> : " + Integer.toString(int_x+300));
						System.out.println("y2()-> : " + Integer.toString(int_y+200) + "\n");
						
						
						try {
							Thread.sleep(1000);
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						sec++;
						
						if (sec>59){
							sec=0;
						}						
					}
				}
			//});								
		}
	}
//}
//LINE END
