package batik.apps.juo;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;


public class GetDomRepresentationFromSVGFile {
	
	
	
	 @SuppressWarnings("deprecation")
	public void getRep() throws MalformedURLException, IOException{
		 
		 JFileChooser fc = new JFileChooser();
		 int r = fc.showOpenDialog(null);
		 
		 if (r==JFileChooser.APPROVE_OPTION){
			System.out.println("Datei: " + fc.getSelectedFile().getName());
			File file = fc.getSelectedFile();
			
			System.out.println("URL: " + file.toURL().toString());

			String parser = XMLResourceDescriptor.getXMLParserClassName();
			System.out.println("parser: " + parser);
			
			
			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
			//Document doc = 
			f.createDocument(file.toURL().toString());
			
			//Your code working with the Document goes here
			//doc.createElement("test_juo");
		 }
		 
		 
	 }
	 
	 
	 
	public static void main(String[] args) throws MalformedURLException, IOException{
		GetDomRepresentationFromSVGFile d = new GetDomRepresentationFromSVGFile();
		d.getRep();
	}

}
