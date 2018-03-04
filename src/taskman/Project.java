
package taskman;


import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

/**
 * This class represents a project.
 * @author Julien Benaouda
 *
 */
public class Project {

	/**
	 * creates a new project with the given values
	 * @param name the project name
	 * @param description the project description
	 * @param creationTime the creation time of the project. The creation time must be of the following format: dd/mm/yyyy hh:mm.
	 * @param dueTime the due time of the project. The due time must be of the following format: dd/mm/yyyy hh:mm
	 * @post a new project is created with the given attributes
	 */
	public Project(String name, String description, String creationTime, String dueTime)
	{
		setName(name);
		setDescription(description);
		setCreationTime(creationTime);
		setDueTime(dueTime);
		taskList = new ArrayList<>();
	}
	
	/**
	 * adds a new task to the project
	 * @param taskDescription the task description
	 * @param estimatedDuration the estimated duration of the task
	 * @param acceptableDeviation the acceptable deviation of the task
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param dependencies a list of dependencies
	 * @param alternative the alternative when the task fails
	 * @post a new task with the give values and status available is added to the project
	 */
	public void addTask(Task t) {
		taskList.add(t);
		// In this stage, the list doesn't need to be sorted as IDs are always increasing.
	}
	
	/**
	 * This method removes a task from the project.
	 * @param id the ID of the task to remove
	 * @throws IllegalArgumentException If a task with the give ID does not exist in the project.
	 */
	public void removeTask(int id) throws IllegalArgumentException
	{
		taskList.remove(getTaskIndex(id));
	}
	
	public HashMap<String, String> getTaskDetails(int id)
	{
		Task t = taskList.get(getTaskIndex(id));
		return t.getTaskDetails();
	}

	/**
	 * returns the name of the project
	 * @return the name of the project
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the project to the given name
	 * @param name the name of the project.
	 * @post the name of the project is set to the given name
	 */
	private void setName(String name) {
		if(name.equals("") || name == null)
		{
			throw new IllegalArgumentException("The project name can't be empty. Please enter a valid project name to proceed");
		}
		this.name = name.trim();
	}

	/**
	 * The name of the project.
	 */
	private String name;

	/**
	 * returns the project description
	 * @return the project description 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the porject description to the given description
	 * @param description the description of the project
	 * @post the project description is set to the given description
	 */
	private void setDescription(String description) {
		this.description = description.trim();
	}

	/**
	 * The project description.
	 */
	private String description;

	/**
	 * returns the creation time of the project
	 * @return the creationTime of the project
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * sets the creation time of the project
	 * @param creationTime the creationTime of the project
	 * @post the creation time of the project is set to the given creation time
	 */
	public void setCreationTime(String creationTime) {
		try {
			this.creationTime = LocalDateTime.parse(creationTime, dateFormatter);
		} catch (DateTimeParseException e)
		{
			throw new IllegalArgumentException("the value " +creationTime +" doesn't match the expected format. Please correct the error and try again.");
		}
	}

	/**
	 * The time of creation of the object.
	 */
	private LocalDateTime creationTime;

	/**
	 * returns the due time of the project
	 * @return the dueTime of the project
	 */
	public LocalDateTime getDueTime() {
		return dueTime;
	}

	/**
	 * Sets the due time of the project.
	 * @param dueTime the dueTime of the project
	 * @pre the due time must be later than the creation time
	 * @post the due time of the project is set to the give due time
	 */
	private void setDueTime(String dueTime) {
		try {
			this.dueTime = LocalDateTime.parse(dueTime, dateFormatter);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("The provided value " +dueTime +" doesn't match the expected format. Please correct the error and try again.");
		}
		if(this.dueTime.compareTo(creationTime) <= 0)
		{
			throw new IllegalArgumentException("The due time can't be before or equal to the start time. Correct the error and try again.");
		}
	}

	/**
	 * The due time of the project.
	 */
	private LocalDateTime dueTime;

	/**
	 * Returns a list with all tasks of a project
	 * @return the tasks of the project
	 */
	public List<Task> getTasks() {
		return ((List<Task>)taskList.clone());
	}
	
	/**
	 * This methods returns a listr with all the task IDs of contained in the project
	 * @returns A list of task IDs
	 */
	public List<Integer> getTaskIds()
	{
		List<Integer> IDs = new ArrayList<>();
		for(Task t: taskList)
		{
			IDs.add(t.getId());
		}
		return IDs;
	}

	/**
	 * returns the task with the given ID
	 * @param id the id of the task
	 * @return the task with the given ID
	 * @throws IllegalArgumentException When the project doesn't contain a task with the give ID.
	 */
	public Task getTask(int id) {
		return taskList.get(getTaskIndex(id));
	}
	
	private int getTaskIndex(int id)
	{
		int index;
		index = Collections.binarySearch(taskList, new Integer(id));
		if(index < 0)
		{
			throw new IllegalArgumentException("A task with the specified ID does not exist in this project.");
		}
		return index;
	}

	/**
	 * The list of tasks for the project.
	 */
	private ArrayList<Task> taskList;
	
	/**
	 * The DateTimeFormatter used to convert LocalDateTimes to Strings and Strings to LocalDateTimes.
	 */
	private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	/**
	 * This method returns a hashmap containing the project properties.
	 * @return A hashmap containing each property of the project, except for the list of tasks. The property names can be used as keys.
	 */
	public HashMap<String, String> getProjectDetails()
	{
		HashMap<String, String> details = new HashMap<>();
		details.put("name", getName());
		details.put("description", getDescription());
		details.put("creationTime", getCreationTime().format(dateFormatter));
		details.put("dueTime", getDueTime().format(dateFormatter));
		return details;
	}
	
	/**
	 * This method returns an XML string containing all project details.
	 * @returns an XML element containing all project details.
	 * @throws OperationNotSupportedException when the xml string can't be created.
	 */
	public Element saveToString() throws OperationNotSupportedException
	{
		try {
			// create the document
			DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = df.newDocumentBuilder();
			Document doc = db.newDocument();
			// add all project attributes
			Element p = doc.createElement("project");
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(getName()));
			p.appendChild(name);
			Element description = doc.createElement("description");
			description.appendChild(doc.createTextNode(getDescription()));
			p.appendChild(description);
			Element creationTime = doc.createElement("creationTime");
			creationTime.appendChild(doc.createTextNode(getCreationTime().format(dateFormatter)));
			p.appendChild(creationTime);
			Element dueTime = doc.createElement("dueTime");
			dueTime.appendChild(doc.createTextNode(getDueTime().format(dateFormatter)));
			p.appendChild(dueTime);
			Element tasks = doc.createElement("tasks");
			// add all tasks of the project
			for(Task t: taskList)
			{
				tasks.appendChild(t.saveToString());
			}
			p.appendChild(tasks);
			return p;
		} catch (Exception e) {
			throw new OperationNotSupportedException("Problem parsing projects to file: " +e);
		}
		return null;
	}
	
	/**
	 * This method converts a xml element containing project data to a project.
	 * @param project the xml element containing the project data
	 * @return a new project with the data from the xml document
	 * @throws OperationNotSupportedException when the provided element can't be parsed.
	 */
	public static Project restoreFromString(Element project) throws OperationNotSupportedException
	{
		try {
			if(!(project.getNodeName().equals("project")))
			{
				throw new OperationNotSupportedException("the xml file is not in the correct format");
			}
			String name = project.getElementsByTagName("name").item(0).getTextContent();
			String description = project.getElementsByTagName("description").item(0).getTextContent();
			String creationTime = project.getElementsByTagName("creationTime").item(0).getTextContent();
			String dueTime = project.getElementsByTagName("dueTime").item(0).getTextContent();
			Project p = new Project(name, description, creationTime, dueTime);
			Node tasks = project.getElementsByTagName("tasks").item(0);
			if(tasks.getNodeType() != Node.ELEMENT_NODE)
			{
				throw new OperationNotSupportedException("the xml file has not the correct format.");
			}
			Element tasksElem = (Element)tasks;
			NodeList tl = tasksElem.getElementsByTagName("task");
			for(int i = 0; i < tl.getLength(); i++)
			{
				p.addTask(Task.restoreFromString(tl.item(i)));
			}
			return p;
		} catch (Exception e)
		{
			throw new OperationNotSupportedException("Something went wrong parsing the xml file");
		}
		return null;
	}

}
