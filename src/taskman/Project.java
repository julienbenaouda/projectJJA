
package taskman;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

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
	 * @throws IllegalArgumentException when one of the given parameters is not of a valid format.
	 * @post a new project is created with the given attributes
	 */
	public Project(String name, String description, String creationTime, String dueTime) throws IllegalArgumentException
	{
		setName(name);
		setDescription(description);
		setCreationTime(creationTime);
		setDueTime(dueTime);
		taskList = new ArrayList<>();
	}
	
	/**
	 * Creates a new project with parameters from the given hashmap.
	 * @param form the HashMap from which to extract the parameters.
	 * @throws IllegalArgumentException when one of the parameters is abscent or not valid.
	 * @post a new project is created with the given parameters
	 */
	public Project(HashMap<String, String> form) throws IllegalArgumentException {
		this(form.get("name"), form.get("description"), form.get("creationTime"), form.get("dueTime"));
	}
	
	/**
	 * adds a new task to the projects task list
	 * @param t the task to add
	 * @post The given task is added to the project
	 */
	public void addTask(Task t) {
		taskList.add(t);
		// In this stage, the list doesn't need to be sorted as IDs are always increasing.
	}
	
	/**
	 * This method removes a task from the projects task list.
	 * @param id the ID of the task to remove
	 * @throws IllegalArgumentException If a task with the given ID does not exist in the project.
	 */
	public void removeTask(int id) throws IllegalArgumentException
	{
		taskList.remove(getTaskIndex(id));
	}

	/**
	 * Returns the task details of the task
	 * @param id the id of the task
	 * @return a HashMap containing as keys the detail name and as value the corresponding detail value
	 * @throws IllegalArgumentException when a task with the given ID does not exist in this project.
	 */
	public HashMap<String, String> getTaskDetails(Integer id) throws IllegalArgumentException
	{
		int index = getTaskIndex(id);
		System.out.println("continue");
		Task t = taskList.get(index);
		return t.getTaskDetails();
	}

    /**
     * Returns a list of task details of the available tasks
     * @return a List containing for each available task a HashMap containing as key the detail name and as value the corresponding detail value
     */
    public ArrayList<HashMap<String, String>> getAvailableTaskDetails(){
	    ArrayList<HashMap<String, String>> availableTaskDetailsList = new ArrayList<>();
        for (int id : getTaskIds()){
            HashMap<String, String> taskDetails = getTaskDetails(id);
            if (Status.fromString(taskDetails.get("status")) == Status.AVAILABLE){
                availableTaskDetailsList.add(taskDetails);
            }
        }
        return availableTaskDetailsList;
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
	 * @throws IllegalArgumentException when the name is empty
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
	 * Sets the project description to the given description
	 * @param description the description of the project
	 * @throws IllegalArgumentException when de project description is null
	 * @post the project description is set to the given description
	 */
	private void setDescription(String description) {
		if(description == null)
		{
			throw new IllegalArgumentException("The description can't be null");
		}
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
	 * @throws IllegalArgumentException when the given date is not valid or null.
	 * @post the creation time of the project is set to the given creation time
	 */
	private void setCreationTime(String creationTime) {
		if(creationTime == null)
		{
			throw new IllegalArgumentException("The creation time can't be null");
		}
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
	 * @throws IllegalArgumentException when the due time is earlier or equal than the creation time or when the due time is null
	 * @post the due time of the project is set to the given due time
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
	public ArrayList<Task> getTasks() {
		return ((ArrayList<Task>)taskList.clone());
	}
	
	/**
	 * This method returns a list with all the task IDs contained in the project
	 * @returns A list of task IDs
	 */
	public List<Integer> getTaskIds()
	{
		List<Integer> IDs = new ArrayList<>();
		for(Task t: taskList)
		{
			IDs.add(t.getID());
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
	
	private int getTaskIndex(int id) throws IllegalArgumentException
	{
		int low = 0;
		int high = taskList.size()-1;
		// System.out.println(high);
		// int index = -1;
		while(low <= high)
		{
			int middle = (low+high)/2;
			Task t = taskList.get(middle);
			if(t.getID() == id)
			{
				return middle;
			}
			if(t.getID() > id) {
				high = middle;
			} else {
				low=middle+1;
			}
		}
		throw new IllegalArgumentException("A task with the given id does not exist in this project.");
	}

	/**
	 * The list of tasks for the project.
	 */
	private ArrayList<Task> taskList;
	
	/**
	 * The DateTimeFormatter used to convert LocalDateTimes to Strings and Strings to LocalDateTimes.
	 */
	private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

	/**
	 * This method returns a hashmap containing the project properties.
	 * @return A HashMap containing each property of the project. It also returns a comma separated list of all task IDs contained in the project. The property names can be used as keys.
	 */
	public HashMap<String, String> getProjectDetails()
	{
		HashMap<String, String> details = new LinkedHashMap<>();
		details.put("name", getName());
		details.put("description", getDescription());
		details.put("creationTime", getCreationTime().format(dateFormatter));
		details.put("dueTime", getDueTime().format(dateFormatter));
		StringBuilder sb = new StringBuilder();
		for(Integer i: getTaskIds()) {
			sb.append(i.toString() +",");
		}
		// delete the last comma if it is present
		int index = sb.lastIndexOf(",");
		if(index != -1) {
			sb.deleteCharAt(index);
		}
		details.put("tasks", sb.toString());
		return details;
	}

	
	/**
	 * This method returns an XML string containing all project details.
	 * @returns an XML element containing all project details.
	 * @throws OperationNotSupportedException when the xml string can't be created.
	 */
	public Element saveToXML() throws OperationNotSupportedException
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
				tasks.appendChild(t.saveToXML());
			}
			p.appendChild(tasks);
			return p;
		} catch (Exception e) {
			throw new XMLParserException(e.getMessage());
		}
	}
	
	/**
	 * This method converts a xml element containing project data to a project.
	 * @param project the xml element containing the project data
	 * @return a new project with the data from the xml document
	 * @throws OperationNotSupportedException when the provided element can't be parsed.
	 */
	public static Project restoreFromXML(Element project) throws OperationNotSupportedException
	{
		try {
			if(!(project.getNodeName().equals("project")))
			{
				throw new XMLParserException("the xml file you provided is not in the correct format. Please correct the errors or try another file");
			}
			String name = project.getElementsByTagName("name").item(0).getTextContent();
			String description = project.getElementsByTagName("description").item(0).getTextContent();
			String creationTime = project.getElementsByTagName("creationTime").item(0).getTextContent();
			String dueTime = project.getElementsByTagName("dueTime").item(0).getTextContent();
			Project p = new Project(name, description, creationTime, dueTime);
			Node tasks = project.getElementsByTagName("tasks").item(0);
			if(tasks.getNodeType() != Node.ELEMENT_NODE)
			{
				throw new XMLParserException("the xml file has not the correct format. Pleas correct the errors or try another file");
			}
			Element tasksElem = (Element)tasks;
			NodeList tl = tasksElem.getElementsByTagName("task");
			for(int i = 0; i < tl.getLength(); i++)
			{
				p.addTask(Task.restoreFromXML((Element)tl.item(i)));
			}
			return p;
		} catch (Exception e)
		{
			throw new XMLParserException(e.getMessage());
		}
	}
	
	/**
	 * This method generates a form containing all parameters needed to create a new project. All values are empty and can be filled in, and then passed back to the project.
	 * @return A HashMap containing all elements that need to be filled in to create a new project
	 */
	public static HashMap<String, String> getCreationForm()
	{
		HashMap<String, String> form = new HashMap<>();
		form.put("name", "");
		form.put("description", "");
		form.put("creationTime", "");
		form.put("dueTime", "");
		return form;
	}

}
