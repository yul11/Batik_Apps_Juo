package batik.apps.juo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JPanel;



public class Uhr_Basis extends JPanel implements Runnable {

 
    private static final long serialVersionUID = 302743725004371394L; 
    protected Point centerPoint    = new Point(250, 250); 
    protected Point secondPoint_a  = new Point(0,0);
    private   Point secondPoint_b  = new Point(0,0);	//Stummel Sekunden
    private   Point minutePoint    = new Point(0,0);
    private   Point hourPoint      = new Point(0,0);        
    private    Thread thread;
    protected Graphics2D g2d;
    private   Color colorSecondHand;
    private int xM;
    private int yM;
    private int lengthSecondHand;
    private int lengthMinuteHand;
    private int lengthHourHand;
    private int lengthFactor;
    protected int angleSec;
    protected int angleMin;
    protected int angleHou;
    protected GregorianCalendar heute;
    JPanel p = new JPanel();
    protected String str_sec;
    protected int stateUhrBasis;
    private int[] handCoordinates;
    

    

   
    public Uhr_Basis() {   	
    	thread = new Thread(this);
    	thread.start();
    	lengthSecondHand = 25;
    	lengthMinuteHand = 23;
    	lengthHourHand   = 15;
    	lengthFactor  = 10;
    	stateUhrBasis = 1;
    	handCoordinates = new int[6];
    	xM=300;
    	yM=300;
    } 
    
    public void set_stateUhrBasis(int state){
    	this.stateUhrBasis = state;    	
    }  
         
    public Thread getThread(){
    	return this.thread;
    }
    
    public int[] getHandCoordinates(){
    	return handCoordinates;
    }
    
    
     
    
    public void paintComponent(Graphics g) {	    
	    super.paintComponent(g);
	    
	    Dimension d = getSize();
		int w = d.width;
		int h = d.height;	
		lengthFactor = w/50;

		xM = w/2;
		yM = h/2;
	    g2d = (Graphics2D) g;	    
	 
	    // background
	    g2d.setColor(Color.WHITE);
	    //g2d.fillOval(0, 0, 500, 500);
	 
	    // border
	    g2d.setColor(Color.BLACK);
	    g2d.drawOval(0, 0, w, h);
	    	    
	    // minutes
	    g2d.drawLine(xM, yM, minutePoint.x, minutePoint.y);
	 
	    // hours
	    g2d.drawLine(xM, yM, hourPoint.x, hourPoint.y);
	 
	    // seconds
	    g2d.setColor(colorSecondHand);
	    g2d.drawLine(xM, yM, secondPoint_a.x, secondPoint_a.y);
	    g2d.drawLine(xM, yM, secondPoint_b.x, secondPoint_b.y);			//Stummel Sekundenzeiger
	    
    }
 
    @Override
    public void run() {
        
        try{
		    while (!Thread.currentThread().isInterrupted()) {
		
		        heute = new GregorianCalendar();

				Thread.sleep(500);
				
				angleSec = (360 / 60) * heute.get(Calendar.SECOND);	
		        secondPoint_a.x = ((int) ( lengthSecondHand * (lengthFactor) * Math.sin(Math.toRadians(angleSec))) + xM);
		        secondPoint_a.y = ((int) (-lengthSecondHand * (lengthFactor) * Math.cos(Math.toRadians(angleSec))) + yM);	 
      
		        angleMin = (360 / 60) * heute.get(Calendar.MINUTE);	
		        minutePoint.x = ((int) ( lengthMinuteHand * (lengthFactor) * Math.sin(Math.toRadians(angleMin))) + xM);
		        minutePoint.y = ((int) (-lengthMinuteHand * (lengthFactor) * Math.cos(Math.toRadians(angleMin))) + yM);	
		        
		        angleHou = (int) (((360 / 12) * heute.get(Calendar.HOUR_OF_DAY)) + (((double) (30d / 60d)) * heute.get(Calendar.MINUTE)));	 
		        hourPoint.x = ((int) ( lengthHourHand * (lengthFactor) * Math.sin(Math.toRadians(angleHou))) + xM);
		        hourPoint.y = ((int) (-lengthHourHand * (lengthFactor) * Math.cos(Math.toRadians(angleHou))) + yM);		        
		        
		        System.out.println("Stunde:  " + heute.get(Calendar.HOUR) );
		        System.out.println("Minute:  " + heute.get(Calendar.MINUTE));		        
		        System.out.println("Sekunde: " + heute.get(Calendar.SECOND)+ "\n");
		        
		        handCoordinates[0]=secondPoint_a.x;
		        handCoordinates[1]=secondPoint_a.y;
		        handCoordinates[2]=minutePoint.x;
		        handCoordinates[3]=minutePoint.y;
		        handCoordinates[4]=hourPoint.x;
		        handCoordinates[5]=hourPoint.y;
		        		 
		        //if (stateUhrBasis==1)
		        //	repaint();
		    }		    
        }
        catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
            System.out.println("Uhr_Basis()-> thread was interrupted. Error: " + e.getMessage());
        }  
    }
}