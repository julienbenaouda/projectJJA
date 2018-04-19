package taskman.backend.importexport;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author Julien Benaouda, Alexander Braekevelt
 *
 */
public class XmlObject {

	private XStream xstream;

	/**
	 * creates a new xml object with given user manager, project organizer and resource manager
	 * @param projectOrganizer the projectOrganizer to add to the xml file
	 * @param userManager the user manager to add to the file
	 * @param resourceManager the resource manager to add to the file
	 * @param clock the clock to add to the file
	 * @throws IllegalArgumentException when one of the parameters is null
	 * @post a new XMLObject is created with the given parameters
	 */
	public XmlObject(ProjectOrganizer projectOrganizer, UserManager userManager, ResourceManager resourceManager, Clock clock) {
		if(projectOrganizer == null || userManager == null || resourceManager == null || clock == null) {
			throw new IllegalArgumentException("No parameter can't be null!");
		}
		this.projectOrganizer = projectOrganizer;
		this.userManager = userManager;
		this.resourceManager = resourceManager;
		this.clock = clock;
	}
	
	/**
	 * represents the resource manager
	 */
	private final ResourceManager resourceManager;
	
	/**
	 * @return the resourceManager
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * represents the user manager
	 */
	private final UserManager userManager;
	
	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * represents the project organizer
	 */
	private final ProjectOrganizer projectOrganizer;
	
	/**
	 * @return the projectOrganizer
	 */
	public ProjectOrganizer getProjectOrganizer() {
		return projectOrganizer;
	}

	/**
	 * represents the system clock
	 */
	private final Clock clock;

	/**
	 * @return the clock
	 */
	public Clock getClock() {
		return clock;
	}

	/**
	 * Creates an XStream object.
	 * @return an XStream object.
	 */
	private static XStream createXstream() {
		XStream stream = new XStream(new DomDriver());
		XStream.setupDefaultSecurity(stream); // to be removed after 1.5
		stream.allowTypesByWildcard(new String[] {
				"taskman.**"
		});
		return stream;
	}
	
	/**
	 * converts the system to an xml string
	 * @return an xml string containing all data from the system
	 * @throws ImportExportException when something goes wrong during the parsing of the objects
	 */
	public String toXMLString() throws ImportExportException {
		XStream stream = createXstream();
		String XMLString;
		try {
			XMLString = stream.toXML(this);
		} catch (Exception e) {
			throw new ImportExportException("Something went wrong during the conversion to XML: " + e.getMessage());
		}
		return XMLString;
	}
	
	/**
	 * converts an xml string to an XMLObject that can be used to reconstruct the system
	 * @param string the xml string to convert
	 * @throws ImportExportException when something goes wrong during the parsing of the string 
	 */
	public static XmlObject fromXMLString(String string) throws ImportExportException {
		XStream stream = createXstream();
		XmlObject obj;
		try {
			obj = (XmlObject) stream.fromXML(string);
		} catch (Exception e) {
			throw new ImportExportException("Something went wrong during the parsing of the string: " + e.getMessage());
		}
		return obj;
	}
	
	/**
	 * saves the system to an XML file
	 * @param path the path of the file
	 * @throws ImportExportException 
	 */
	public void saveToFile(String path) throws ImportExportException {
		try (BufferedWriter w = new BufferedWriter(new FileWriter(path))) {
			w.write(toXMLString());
		} catch(Exception e) {
			throw new ImportExportException("Something went wrong writing the string to file: " +e.getMessage());
		}
	}
	
	/**
	 * restores a string from a file and parses it
	 * @param path the path to the file
	 * @return an xml object containing all system data
	 * @throws ImportExportException when something goes wrong during the parsing of the file
	 */
	public static XmlObject restoreFromFile(String path) throws ImportExportException {
		try(BufferedReader r = new BufferedReader(new FileReader(path))) {
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line = r.readLine()) != null) {
				sb.append(line);
			}
			return fromXMLString(sb.toString());
		} catch (Exception e) {
			throw new ImportExportException("Something went wrong druing parsing of the file: " + e.getMessage());
		}
	}

}
