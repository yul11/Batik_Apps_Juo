package batik.apps.juo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
//import org.apache.batik.transcoder.TranscoderInput;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;




public class SVGConjurer extends JFrame implements ChangeListener, MouseListener{
	
	private static final long serialVersionUID = 1L;

	//Namespace string, to be used throughout the class
	private final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
	
	//Center coordinates and radius of the most recent ball
	private int xPos, yPos;
	private int radius = 70;
	
	//The canvas to display results
	private JSVGCanvas canvas;
	
	//The spinners to change position and radius of the most recent ball
	private JSpinner spR,spX,spY;
	
	//The labels for spinners
	JLabel lblR = new JLabel("Radius:");
	JLabel lblX = new JLabel("X:");
	JLabel lblY = new JLabel("Y:");
	
	//The document we are going to create and modify
	private Document document;
	
	//A reference to the most recently added ball	
	private Element mostRecentBall;


	
	//Constructor
	public SVGConjurer(){
		
		super ("SVG Conjurer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		JPanel p     = new JPanel();
		
		canvas = new JSVGCanvas();
		canvas.setMySize(new Dimension(200,200));
		
		//We are going to react on mouse clicks, so the appropriate listener is added
		canvas.addMouseListener(this);
		
		//The following line is needed for our canvas to react an every change of the document associated with it
		canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
		
		SpinnerModel rModel = new SpinnerNumberModel(0,0,150,5);
		spR = new JSpinner(rModel);
		spR.addChangeListener(this);
		
		SpinnerModel xModel = new SpinnerNumberModel(0,0,200,10);
		spX = new JSpinner(xModel);
		spX.addChangeListener(this);
		
		SpinnerModel yModel = new SpinnerNumberModel(0,0,200,10);
		spY = new JSpinner(yModel);
		spY.addChangeListener(this);
		
		//It makes sense to disable these components until the first ball was created
		lblR.setEnabled(false);
		lblX.setEnabled(false);
		lblY.setEnabled(false);
		spX.setEnabled(false);
		spR.setEnabled(false);
		spY.setEnabled(false);
		
		p.add(lblR);
		p.add(spR);
		p.add(lblX);
		p.add(spX);
		p.add(lblY);
		p.add(spY);
		
		panel.setLayout(new BorderLayout());
		panel.add("North", p);
		panel.add("Center", canvas);
		this.setContentPane(panel);
		this.pack();
		this.setBounds(150,150,this.getWidth(), this.getHeight());
		
		DOMImplementation dom = SVGDOMImplementation.getDOMImplementation();
		document = dom.createDocument(svgNS, "svg",null);
		
		//This is where we add our gradient definitions to the document
		Gradients.insertCoolRadialGradient(document);
		
		//Finally, the document is associated with the canvas
		canvas.setDocument(document);
	}
	
	
	//This method is invoked when a mouse button was clicked
	public void mouseClicked(MouseEvent e){
		
		//Set the properties to the coordinates of the point, where mouse button was clicked
		xPos = e.getX();
		yPos = e.getY();
		
		//Add a circle element to the document
		addBall();
		
		//Now we can enable the controls to change the attributes of the newly added element
		enableControls();
	}

	
	//The following four methods are required for a MouseListener interface implementation, but we are not using them for anything
	public void mousePressed(MouseEvent e){
	}
	
	public void mouseReleased(MouseEvent e){
	}
	
	public void mouseEntered (MouseEvent e){
	}
	
	public void mouseExited (MouseEvent e){
	}	

	
	private void enableControls(){
		
		lblR.setEnabled(true);;
		lblX.setEnabled(true);
		lblY.setEnabled(true);
		
		//We are not only enabling the spinners, but also setting them to the current coordinates and radius
		spX.setValue(new Integer(xPos));
		spX.setEnabled(true);
		
		spR.setValue(new Integer(radius));
		spR.setEnabled(true);
		
		spY.setValue(new Integer(yPos));;
		spY.setEnabled(true);
	}

	
	//Changing the coordinates and the radius of the most recent ball
	public void stateChanged(ChangeEvent e){
		
		JSpinner source = (JSpinner) e.getSource();
		if (source == spR){
			SpinnerNumberModel model = (SpinnerNumberModel) source.getModel();
			radius = ((Integer) model.getValue()).intValue();
			modifyBall();
		}
		
		if (source == spX){
			SpinnerNumberModel model = (SpinnerNumberModel) source.getModel();
			xPos = ((Integer)model.getValue()).intValue();
			modifyBall();
		}
		
		if (source == spY){
			SpinnerNumberModel model = (SpinnerNumberModel) source.getModel();
			yPos = ((Integer) model.getValue()).intValue();
			modifyBall();
		}
	}
	
	private void addBall(){
		
		//The code is put into a Runnable object
		Runnable r = new Runnable(){
			public void run(){
				Element root = document.getDocumentElement();
				
				//A new ball is created
				Element circle = document.createElementNS(svgNS, "circle");
				
				circle.setAttributeNS(null,"fill", "url(#"+Gradients.COOL_RADIAL_GRADIENT_ID);	
				//circle.setAttributeNS(null,"fill", Gradients.COOL_RADIAL_GRADIENT_ID);
				
				circle.setAttributeNS(null,"stroke","black");
				circle.setAttributeNS(null,"stroke-width","1");
				circle.setAttributeNS(null,"r", "" + radius);
				circle.setAttributeNS(null,"cx","" + xPos);
				circle.setAttributeNS(null,"cy","" + yPos);

				//A reference to the newball is stored in a class member
				mostRecentBall = circle;
				
				//And finally, the new element is appended to the root
				root.appendChild(circle);				
			}		
		};
		
		//Running our code in the UpdateManager thread
		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
	}
	
	
	private void modifyBall(){
		
		//The code is put into a Runnable object
		Runnable r = new Runnable(){
			public void run(){
				if (mostRecentBall != null){
					
					//Obtain a reference to the r attribute
					Attr r = mostRecentBall.getAttributeNode("r");
					
				    //And set the attributes value to the current radius"
					r.setValue("" +radius);
					
					//The same for the other two attributes 
					Attr cx = mostRecentBall.getAttributeNode("cx");
					cx.setValue("" + xPos);
					
					Attr cy = mostRecentBall.getAttributeNode("cy");
					cy.setValue("" + yPos);
				}
			}
		};
		
		//Running our code in the UpdateManager thread
		UpdateManager um = canvas.getUpdateManager();
		um.getUpdateRunnableQueue().invokeLater(r);
	}
	
	public void JuosTranscoder(Document d){
		//TranscoderInput input = new TranscoderInput(d);
		//File file = fcOpenFile.getSelectedFile();
		return;
	}
	
	
	//The entry point into the program
	public static void main(String[] args){
		SVGConjurer conjurer = new SVGConjurer();
		conjurer.setVisible(true);
	}
}









