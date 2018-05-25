package taskman.backend.importexport;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.time.Clock;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * This class represents the XmlObject of the System.
 * It is responsible for exporting the system to an xml string and saving this string to a file, and importing an xml string and converting it to system objects. Therefore it keeps a reference to the project manager, resource manager, clock and user manager.
 * @author Julien Benaouda, Alexander Braekevelt
 *
 */
public class XmlObject {

	private XStream xstream;

	/**
	 * creates a new xml object with given user manager, project organizer and resource manager
	 * @param branchOfficeManager the branch office manager to add to the xml file
	 * @param clock the clock to add to the file
	 * @throws IllegalArgumentException when one of the parameters is null
	 * @post a new XMLObject is created with the given parameters
	 */
	public XmlObject(BranchOfficeManager branchOfficeManager, Clock clock) {
		if(branchOfficeManager == null || clock == null) {
			throw new IllegalArgumentException("No parameter can't be null!");
		}
		this.branchOfficeManager = branchOfficeManager;
		this.clock = clock;
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
	 * represents the branch office manager
	 */
	private BranchOfficeManager branchOfficeManager;
	
	/**
	 * @return the branch office manager
	 */
	public BranchOfficeManager getBranchOfficeManager() {
		return branchOfficeManager;
	}

	/**
	 * Creates an XStream object.
	 * @return an XStream object.
	 */
	private static XStream createXstream() {
		XStream stream = new XStream(new DomDriver());
		XStream.setupDefaultSecurity(stream);
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
			throw new ImportExportException("Something went wrong writing the string to file: " + e.getMessage());
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
			String line;
			StringBuilder sb = new StringBuilder();
			while((line = r.readLine()) != null) {
				sb.append(line);
			}
			return fromXMLString(sb.toString());
		} catch (Exception e) {
			throw new ImportExportException("Something went wrong during parsing of the file: " + e.getMessage());
		}
	}

}
