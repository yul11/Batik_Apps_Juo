package batik.apps.juo;
import java.awt.Color;

import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;




public class LineMovement implements Runnable{	
	
	Document document;
	JSVGCanvas canvas;
	private Thread thread;
	JPanel panel;
	int sec=0;
	String mx= "300";  //Mittelpunkt x-Koordinate
	String my= "200";  //Mittelpunkt y-Koordinate
	
	public LineMovement(Document d, JSVGCanvas c, JPanel p){		
		this.document =d;
		this.canvas   =c;
		this.panel    =p;
	}

	public void starte(){
		thread = new Thread(this); 
		thread.start();
	}		
	
	public void stoppe(){
		System.out.println("stoppe LineMovement-thread now");
		this.thread.stop();
	}

	
	public void run(){	
		
		while (!Thread.currentThread().isInterrupted()) {
			
			System.out.println("LineMovement-thread laeuft");	
			try {
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
				public void run(){													
					
					System.out.println("LineMovement()-> bin in invokeLater line");						
					
					Element elt = document.getElementById("theLine");
					System.out.println("LineMovement()-> style: " + elt.getAttribute("style"));
					
					double alf = (180 - 6 * sec) * Math.PI / 180;
					double x = Math.sin(alf) *190;
					double y = Math.cos(alf) *190;
					
					System.out.println("LineMovement()-> sec: " + sec);
					
					int int_x = (int)x;
					int int_y = (int)y;
					System.out.println("LineMovement()-> int_x: " + int_x);
					System.out.println("LineMovement()-> int_y: " + int_y + "\n");
					
					elt.setAttributeNS(null,"x1", mx);
					elt.setAttributeNS(null,"y1", my);
					elt.setAttributeNS(null,"x2", Integer.toString(int_x+Integer.parseInt(mx)));
					elt.setAttributeNS(null,"y2", Integer.toString(int_y+Integer.parseInt(my)));
					
					System.out.println("x1()-> : " + mx);
					System.out.println("y1()-> : " + my);
					System.out.println("x2()-> : " + Integer.toString(int_x+Integer.parseInt(mx)));
					System.out.println("y2()-> : " + Integer.toString(int_y+Integer.parseInt(my)) + "\n");
									
					sec++;
					System.out.println("panel.repaint,panel.repaint,panel.repaint,panel.repaint,panel.repaint,panel.repaint,panel.repaint,panel.repaint");						
					panel.setForeground(Color.blue);
					panel.createToolTip();					
					
					if (sec>59){
						sec=0;
					}											
				}
			});								
		}
	}
}
//LINE END
