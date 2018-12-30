package batik.apps.juo;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import org.apache.batik.swing.JSVGCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.awt.Dimension;
import java.awt.Font;


public class Setup extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	public static JButton button1;
	public static JButton button2;
	public static JComboBox<String> comboBox;
	public static JTextField txt_background;
	public static JTextField txt_foreground;

	private Document document; // The SVG document
	private JSVGCanvas canvas;	
	
	Setup(Document d, JSVGCanvas c){
		
		this.document = d;
		this.canvas   = c;
		
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 550);        
        setLayout(new BorderLayout()); 
        setResizable(true);       
        
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.FRAME);                

        Dimension dim = new Dimension();
        dim.setSize(150, 25);                
        
        button1    = new JButton("Analog");
        button2    = new JButton("Digital");               
        button1.setPreferredSize(dim);                
        button2.setPreferredSize(dim);
		String[] choices = {"black girl","red girl","CHOICE 3","CHOICE 4","CHOICE 5","CHOICE 6","CHOICE 7"};
	    comboBox = new JComboBox<String>(choices);
	    comboBox.setVisible(true);
	    comboBox.setPreferredSize(dim);	    
	    final String fontName = comboBox.getSelectedItem().toString();	    
	    comboBox.setFont(new Font(fontName, Font.BOLD, 14));
	    ListCellRenderer<? super String> renderer = comboBox.getRenderer();
	    if(renderer instanceof BasicComboBoxRenderer)
	        ((BasicComboBoxRenderer) renderer).setHorizontalAlignment(SwingConstants.CENTER);	    
	    setSize(170, 650);
	    setLocation(430, 100);
	    JLabel hd_parameters = new JLabel("Parameters:");
	    JTextField txt_background       = new JTextField("background-color");
	    txt_background.setPreferredSize(dim);
	    txt_background.setFont(new Font(fontName, Font.BOLD, 14));
	    JTextField txt_foreground       = new JTextField("foreground-color");
	    txt_foreground.setPreferredSize(dim);
	    txt_foreground.setFont(new Font(fontName, Font.BOLD, 14));
	    	    
		JPanel    mainPanel   = new JPanel();		
		mainPanel.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		
		northPanel.add(comboBox,BorderLayout.LINE_START);	
		northPanel.add(button1,BorderLayout.LINE_START);	
		northPanel.add(button2,BorderLayout.LINE_START);
		northPanel.add(hd_parameters,BorderLayout.LINE_START);
		northPanel.add(txt_background,BorderLayout.LINE_START);
		northPanel.add(txt_foreground,BorderLayout.LINE_START);
				
		mainPanel.add(northPanel,BorderLayout.CENTER);				
	    add(mainPanel);
	    
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setResizable(true);
	    setVisible(true);

	    comboBox.addActionListener(this);
	    button1.addActionListener(this);
	    button2.addActionListener(this);
	    txt_background.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("txt_background wurde angeklickt");	
				Color selectedColor = JColorChooser.showDialog(null,"Farbe Hintergrund auswählen", null);
				System.out.println("selected color: " + selectedColor);
				System.out.println("red: " + selectedColor.getRed());
				System.out.println("green" + selectedColor.getGreen());
				System.out.println("blue: " + selectedColor.getBlue());
				txt_background.setText("[R,G,B] " + " ["+ selectedColor.getRed() + "," + selectedColor.getGreen() + "," + selectedColor.getBlue() + "]");
				String hex = String.format("#%02x%02x%02x", selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());  
				Element el = document.getElementById("theClockFace");
        		el.setAttributeNS(null, "fill",hex);
			}
			public void mouseEntered(MouseEvent arg0) {
				System.out.println("txt_background mouse entered!!!!");	
			}
			public void mouseExited(MouseEvent arg0) {	
				System.out.println("txt_background mouse exited!!!!");	
			}
			public void mousePressed(MouseEvent arg0) {
				System.out.println("txt_background mousePressed!!!!");	
			}
			public void mouseReleased(MouseEvent arg0) {
				System.out.println("txt_background mouseReleased!!!!");	
			}
	    });
	    
	    
	    txt_foreground.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("txt_foreground wurde angeklickt");				
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
			}
			public void mouseReleased(MouseEvent arg0) {
			}   	
	    });
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
                
        		Gradients.insertCoolPictureBlackGirl(document);	     		      		
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
