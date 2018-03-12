
package taskman;


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
	 * @param task the task to add
	 * @post The given task is added to the project
	 */
	public void addTask(Task task) {
		// TODO: ge zijt case met lege lijst vergeten
		int low = 0;
	 	int middle = low;
	 	int high = taskList.size();
	 	while (low < high) {
			middle = (low+high)/2;
		 	Task middleTask = taskList.get(middle);
		 	if(middleTask.getID().equals(task.getID())) {
				 break;
		 	}
		 	else if (middleTask.getID() > task.getID()) {
				 high = middle - 1;
		 	} else {
				 low = middle + 1;
		 	}
	 	}
	 	taskList.add(middle, task);
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
	 * @throws IllegalArgumentException When the project doesn't contain a task with the given ID.
	 */
	public Task getTask(int id) {
		return taskList.get(getTaskIndex(id));
	}
	
	private int getTaskIndex(int id) throws IllegalArgumentException
	{
		int low = 0;
		int high = taskList.size()-1;
		System.out.println(high);
		// int index = -1;
		while(low <= high)
		{
			int middle = (low+high)/2;
			Task t = taskList.get(middle);
			System.out.println(id +", " +t.getID());
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
	 * Add a project to an XmlObject.
	 * @param projectObject an XmlObject.
	 * @post the project will be added to the XmlObject.
	 * @throws XmlException if the project cannot be added to the XmlObject.
	 */
	public void addToXml(XmlObject projectObject) throws XmlException {
		projectObject.addAttribute("name", getName());
		projectObject.addText("description", getDescription());
		projectObject.addText("creationTime", getCreationTime().format(dateFormatter));
		projectObject.addText("dueTime", getDueTime().format(dateFormatter));
		for(Task task: taskList)
		{
			XmlObject object = projectObject.addXmlObject("task");
			task.addToXml(object);
		}
	}

	/**
	 * Restore a project from an XmlObject.
	 * @param projectObject the XmlObject.
	 * @return the restored project.
	 * @throws XmlException if the project can't be created.
	 */
	public static Project getFromXml(XmlObject projectObject) throws XmlException {
		String name = projectObject.getTexts("name").get(0);
		String description = projectObject.getTexts("description").get(0);
		String creationTime = projectObject.getTexts("creationTime").get(0);
		String dueTime = projectObject.getTexts("dueTime").get(0);
		Project project = new Project(name, description, creationTime, dueTime);
		for (XmlObject taskObject : projectObject.getXmlObjects("task")) {
			project.addTask(Task.getFromXml(taskObject));
		}
		for (XmlObject taskObject : projectObject.getXmlObjects("task")) {
			Integer id = Integer.parseInt(taskObject.getAttribute("id"));
			Task task = project.getTask(id);

			// TODO: dit testen (deze if heb ik toegevoegd)
			if (taskObject.getTexts("alternative").get(0) != null) {
				Integer alternativeId = Integer.parseInt(taskObject.getTexts("alternative").get(0));
				task.setAlternative(project.getTask(alternativeId));
			}

			ArrayList<Task> dependencies = new ArrayList<>();
			for (String s : taskObject.getTexts("dependency")) {
				Integer dependencyId = Integer.parseInt(s);
				dependencies.add(project.getTask(dependencyId));
			}
			task.restoreDependencies(dependencies);
		}
		return project;
	}
	
	/**
	 * This method generates a form containing all parameters needed to create a new project. All values are empty and can be filled in, and then passed back to the project.
	 * @return A HashMap containing all elements that need to be filled in to create a new project
	 */
	public static HashMap<String, String> getCreationForm()
	{
		HashMap<String, String> form = new LinkedHashMap<>();
		form.put("name", "");
		form.put("description", "");
		form.put("creationTime", "");
		form.put("dueTime", "");
		return form;
	}

}
