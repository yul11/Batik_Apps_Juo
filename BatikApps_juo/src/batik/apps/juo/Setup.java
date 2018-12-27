package batik.apps.juo;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Setup extends SVGInteractor implements ActionListener{


	private static final long serialVersionUID = 1L;
	public static JButton button1;
	public static JButton button2;
	public static JComboBox<String> comboBox;
	private Document document; // The SVG document
	
	
	Setup(Document d){
		
		this.document = d;
		
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 550);        
        setLayout(new BorderLayout()); 
        setResizable(true);
        
        button1    = new JButton("Analog");
        button2    = new JButton("Digital");
		//Comobo-Box
		String[] choices = {"african Girl","erstauntes Mädchen","CHOICE 3","CHOICE 4","CHOICE 5","CHOICE 6","CHOICE 7"};
	    comboBox = new JComboBox<String>(choices);
	    comboBox.setVisible(true);
	    setSize(170, 500);
	    setLocation(100, 100);
	    
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
			
            if (sItem.equals("african Girl")){
                System.out.println("chose african Girl!");              
        		Gradients.insertCoolPictureAfrica(document);	     		
        		Element elt = document.getElementById("theClockFace");
        		elt.setAttributeNS(null, "fill","url(#" + Gradients.PHOTO_GRADIENT_ID + ")");                
            } 
            else{ 
            	if (sItem.equals("erstauntes Mädchen")){
                    System.out.println("chose red Girl!");
                    
            		Gradients.insertCoolPictureRedGirl(document);	     		
            		Element elt = document.getElementById("theClockFace");
            		elt.setAttributeNS(null, "fill","url(#" + Gradients.PHOTO_GRADIENT_ID + ")");
            	}
                else
                	System.out.println("ein anderes Mädchen ausgewählt!");
            } 
		}
	}
	
	
		 


	
	
    public static void main(String[] args) { 
    	
    	Document document = null;
    	new Setup(document);
    }
}
