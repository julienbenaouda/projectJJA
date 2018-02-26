
package TaskMan;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
	 * @param creationTime the creation time of the project
	 * @param dueTime the due time of the project
	 * @post a new project is created with the given attributes
	 */
	public Project(String name, String description, LocalDateTime creationTime, LocalDateTime dueTime)
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
	public void addTask(String taskDescription, Duration estimatedDuration, double acceptableDeviation, LocalDateTime startTime, LocalDateTime endTime, ArrayList<Task> dependencies, Task alternative) {
		Task t = new Task(taskDescription, estimatedDuration, acceptableDeviation, startTime, endTime, dependencies, alternative);
		taskList.add(t);
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
		this.name = name;
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
		this.description = description;
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
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
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
	private void setDueTime(LocalDateTime dueTime) {
		if(dueTime.compareTo(creationTime) > 0)
		{
			this.dueTime = dueTime;
		} else {
			throw new IllegalArgumentException("The due time can't be before the start time.");
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
		return (ArrayList<Task>) taskList.clone();
	}
	/**
	 * returns the task with the given ID
	 * @param id the id of the task
	 * @return the task with the given ID
	 * @throws IndexOutOfBoundsException When the given index is larger than the maximum size of the list.
	 */
	public Task getTask(int id) throws IndexOutOfBoundsException {
		return taskList.get(id);
	}
	/**
	 * The list of tasks for the project.
	 */
	private ArrayList<Task> taskList;

}
