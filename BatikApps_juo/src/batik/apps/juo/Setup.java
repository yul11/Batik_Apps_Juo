package batik.apps.juo;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Setup extends SVGInteractor implements ActionListener{


	private static final long serialVersionUID = 1L;
	public static JButton button1;
	public static JButton button2;
	public static JComboBox<String> comboBox;
	private Document document; // The SVG document
	private JSVGCanvas canvas;
	
	
	Setup(Document d, JSVGCanvas c){
		
		this.document = d;
		this.canvas   = c;
		
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 550);        
        setLayout(new BorderLayout()); 
        setResizable(true);
        
        button1    = new JButton("Analog");
        button2    = new JButton("Digital");
		//Comobo-Box
		String[] choices = {"black girl","red girl","CHOICE 3","CHOICE 4","CHOICE 5","CHOICE 6","CHOICE 7"};
	    comboBox = new JComboBox<String>(choices);
	    comboBox.setVisible(true);
	    setSize(170, 650);
	    setLocation(430, 100);
	    
		JPanel    mainPanel   = new JPanel();		
		mainPanel.setLayout(new BorderLayout());				
		JPanel northPanel = new JPanel();
		
		northPanel.add(comboBox,BorderLayout.LINE_START);	
		northPanel.add(button1,BorderLayout.LINE_START);	
		northPanel.add(button2,BorderLayout.LINE_START);
				
		mainPanel.add(northPanel,BorderLayout.CENTER);				
	    add(mainPanel);
	    
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(true);
	    setVisible(true);

	    comboBox.addActionListener(this);
	    button1.addActionListener(this);
	    button2.addActionListener(this);
	}

	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == button1){
			System.out.println("Analog-button gedrückt");
			setVisible(true);
		}		
		if (e.getSource() == button2){
			System.out.println("Digital-button gedrückt");
			setVisible(true);
		}	
		
		
		if (e.getSource() == comboBox){			
			@SuppressWarnings("unchecked")
			JComboBox<String> bx = (JComboBox<String>) e.getSource();			
			String sItem = (String)bx.getSelectedItem();
			
            if (sItem.equals("black girl")){
                System.out.println("chose black girl");
                
        		Gradients.insertCoolPictureAfrica(document);	     		      		
        		Element el = document.getElementById("theClockFace");
        		el.setAttributeNS(null, "fill","url(#" + Gradients.PHOTO_GRADIENT_ID + ")");
        		
        		System.out.println("Id black girl: " + el.getAttribute("id"));
        		System.out.println("Fill black girl: " + el.getAttribute("fill"));
       		       		
				canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
					public void run(){	
						System.out.println("bin in Setup()-> run, black girl");					
					}
				});
           } 
            else{ 
            	if (sItem.equals("red girl")){
                    System.out.println("chose red girl");                    
                    
            		Gradients.insertCoolPictureRedGirl(document);         		
            		Element elt = document.getElementById("theClockFace"); 
            		elt.setAttributeNS(null, "fill","url(#" + Gradients.PHOTO_GRADIENT_ID +"2" + ")");
            		//elt.setAttributeNS(null, "fill","red");

            		System.out.println("Id red girl: " + elt.getAttribute("id"));
            		System.out.println("Fill red girl: " + elt.getAttribute("fill"));
            		           		
    				canvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(new Runnable() {
    					public void run(){    						
    						System.out.println("bin in Setup()-> run, red girl");
    					}
    				});            		
            	}
                else
                	System.out.println("ein anderes Mädchen ausgewählt!");
            } 
		}
	}
}
