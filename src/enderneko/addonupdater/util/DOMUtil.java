package enderneko.addonupdater.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * @author enderneko
 * Aug 6, 2018
 */
public class DOMUtil {
	public static Document getDocument(File f) {
		Document doc = null; 
		try {
			if (f != null && f.exists()) {
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
			} else {
				doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				doc.setXmlVersion("1.0");
				doc.appendChild(doc.createElement("addons"));
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO log/show error msg
			e.printStackTrace();
		}
		return doc;
	}
	
	public static void writeToXML(Document doc, File f) {
		try {
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(f));
		} catch (TransformerException | TransformerFactoryConfigurationError e) {
			// TODO log/show error msg
			e.printStackTrace();
		}
	}
}
