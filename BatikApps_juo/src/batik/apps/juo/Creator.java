package batik.apps.juo;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class Creator extends JFrame implements ActionListener{		

	private static final long serialVersionUID = 1L;
	//declare buttons
	private JButton btnCircle;
	private JButton btnSquare;
	//declare the SVG Generator
	private SVGGraphics2D generator;
	private JPanel  panel;

	
	
	//constructor
	public Creator(){
		super("SVG Creator");		
		//the default close operation
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		//prepare the container
		panel = new JPanel();
		btnCircle = new JButton("Create a Circle");
		btnSquare = new JButton("Create a Square");
		btnCircle.addActionListener(this);
		btnSquare.addActionListener(this);
	
		//Add buttons to the Panel an set the Panel to the Frame
		panel.add(btnCircle);
		panel.add(btnSquare);
		this.setContentPane(panel);
		this.pack();	
		//Change the postion of the program window.The default is the top left corner	
		this.setBounds(150,150,this.getWidth(), this.getHeight());
		
		//This is where the SVG-Generator is instantiated
		DOMImplementation dom = GenericDOMImplementation.getDOMImplementation();
		Document doc = dom.createDocument(null,"svg",null);
		
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(doc);
		ctx.setComment("This file was created by juo with the svg-generator from Batik");
		
		generator = new SVGGraphics2D(ctx,false);
	}
	

	
	//The Action Listener which runs when a button is pressed
	public void actionPerformed(ActionEvent ae){
		
		//which button was pressed?
		Object source = ae.getSource();
		if (source == btnCircle){
			System.out.println("Draw a circle");
			//drawCircle(generator);
		}
		if (source == btnSquare){
			System.out.println("Draw a square");
			//drawSquare(generator);
		}
		
		//Write the generated SVG-document to a file
		try{
			FileWriter file = new FileWriter("D://auxi//batik_filetest.svg");
			PrintWriter writer = new PrintWriter(file);
			//generator.stream(writer,true);
			writer.close();
		}
		catch (IOException ioe){
			System.err.println("IO problem: " + ioe.toString());
			
		}		
	}

	
	
	// And finally the methods that actually draw a circle and a square using the available methods of Graphics2D
	private void drawSquare(Graphics2D g2d){
		
		//create a rectangle with equal sides
		Rectangle rect = new Rectangle(10,10,100,100);
		
		//set the color and the stroke to draw the outline and draw it
		g2d.setPaint(Color.red);
		g2d.setStroke(new BasicStroke(15.0f));
		g2d.draw(rect);
		
		//set the color to fill the square and fill it 
		g2d.setPaint(Color.lightGray);
		g2d.fill(rect);
	}

	
	
	private void drawCircle(Graphics2D g2d){
		
		//Create a circle
		Ellipse2D circle = new Ellipse2D.Double(10,10,100,100);
		
		//Set the color and the stroke to draw the outline and draw it		
		g2d.setPaint(Color.green);
		g2d.setStroke(new BasicStroke(15.0f));
		g2d.draw(circle);
		
		//Set the color to fill the circle and fill it
		g2d.setPaint(Color.yellow);
		g2d.fill(circle);
	}



	public static void main(String[] args){
		
		Creator creator = new Creator();
		creator.setVisible(true);
	}	
}	
