package batik.apps.juo;

import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.audio.AudioStream;

public class CircleMovement implements Runnable{	

	private double deltaY = 4;
	boolean doRun = true;
	boolean down  = true;
	private Thread thread;
	private JSVGCanvas canvas;
	private Document document;
	AudioStream as= null;
	
	
	public CircleMovement(JSVGCanvas c, Document d){
		this.canvas = c;
		this.document = d;
	}

	public void starte(){
		thread = new Thread(this);
		thread.start();
	}				

	public void stoppe(){
		System.out.println("stoppe CircleMovement-thread now");
		doRun = false;
	}

	public void run(){	

		while (doRun) {

			try {
				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Returns immediately
			canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {

				public void run(){	

					Element elt = document.getElementById("theCircle");

					int yPos = Integer.parseInt(elt.getAttribute("cy"));

					if ((yPos <= 50) || (yPos >=550)){
						deltaY  = - deltaY;
						System.out.println("deltaY: " +deltaY);

						if (yPos >=550){
							System.out.println("fülle gelb");
							elt.setAttributeNS(null, "fill","yellow");
							elt.setAttributeNS(null, "stroke","yellow");
							elt.setAttributeNS(null, "stroke-width","20");
							down = false;								
							as = Sound.soundStart();						
						}
						if (yPos <= 50){
							System.out.println("fülle gelb");
							elt.setAttributeNS(null, "fill","yellow");
							elt.setAttributeNS(null, "stroke","yellow");
							elt.setAttributeNS(null, "stroke-width","20");	
							down = true;
							Sound.soundStop(as);
						}							
					}
					else{
						if (down){														
							deltaY = (1.0*(yPos/30)*(yPos/30));
							System.out.println("deltaY: " + deltaY + " Bewegung: down");		
						}
						else{								
							deltaY = -(1.0*(yPos/30)*(yPos/30));
							System.out.println("deltaY: " + deltaY + " Bewegung: up");		
						}
					}

					yPos += deltaY;
					//System.out.println("(yPos % 100): " + (yPos % 100)); 

					if ((yPos % 4) ==0 ){
						elt.setAttributeNS(null, "fill","blue");
						elt.setAttributeNS(null, "stroke","red");
						elt.setAttributeNS(null, "stroke-width","20");	
						elt.setAttribute("stroke-opacity",".7");

					}
					if ((yPos % 8) ==0 ){
						elt.setAttributeNS(null, "fill","red");
						elt.setAttributeNS(null, "stroke","blue");
						elt.setAttributeNS(null, "stroke-width","20");		
						elt.setAttribute("stroke-opacity",".7");												
					}
					elt.setAttribute("cy", "" + yPos);
					System.out.println("yPos: " + yPos);						
				}
			});								
		}
	}
}
//CIRCLE END
