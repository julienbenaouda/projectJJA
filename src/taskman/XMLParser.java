/**
 * 
 */
package taskman;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class represents the xml parser. It is responsable for exporting and importing project data.
 * @author Julien
 *
 */
public class XMLParser {
	/**
	 * This method converts a xml document to a list of projects, and initializes the system time.
	 * @param path the path to the file to import from
	 * @return a list with all projects.
	 * @throws SAXException
	 * @throws IOException When the given file is not ffound.
	 * @throws IllegalArgumentException when de format of the xml file is not valid.
	 */
	public static ArrayList<Project> importXML(String path) throws SAXException, IOException, IllegalArgumentException
	{
			ArrayList<Project> projects = new ArrayList<>();
			File f = new File(path);
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = df.newDocumentBuilder();
			Document doc = db.parse(f);
			doc.getDocumentElement().normalize();
			Node projectsnode = doc.getElementsByTagName("projects").item(0);
			if (projectsnode.getNodeType() != Node.ELEMENT_NODE) {
				throw new IllegalArgumentException("The xml file is not valid");
			}
			Element projectsElem = (Element)projectsnode;
			NodeList projectlist = projectsElem.getElementsByTagName("project");
			for(int i = 0; i < projectlist.getLength(); i++)
			{
				if (projectlist.item(i).getNodeType() != Node.ELEMENT_NODE) {
					throw new IllegalArgumentException("The xml file is not valid.");
				}
				Element e = (Element)projectlist.item(i);
				Project p = Project.restoreFromXML(e);
				projects.add(p);
			}
			Node clockNode = doc.getElementsByTagName("clock").item(0);
			if (clockNode.getNodeType() != Node.ELEMENT_NODE) {
				throw new IllegalArgumentException("The XML file is not valis.");
			}
			Clock.restoreFromXML((Element)clockNode);
			return projects;
	}
	
	public static void exportXML(String path, ArrayList<Project> projects) throws ParserConfigurationException, DOMException, OperationNotSupportedException
	{
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = df.newDocumentBuilder();
		Document doc = db.newDocument();
		Element projectsElem = doc.createElement("projects");
		for(Project p: projects) {
			projectsElem.appendChild(p.saveToXML());
		}
		doc.appendChild(projectsElem);
		doc.appendChild(Clock.saveToXML());
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		DOMSource s = new DOMSource(doc);
		StreamResult r = new StreamResult(new File(path));
		t.transform(s, r);
	}
}
