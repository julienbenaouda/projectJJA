
package taskman.Backend;


import taskman.Backend.Task.Task;

import java.time.LocalDateTime;
import java.util.*;

/**
 * This class represents a project.
 *
 * @author Julien Benaouda, Jeroen Van Der Donckt
 */
public class Project implements Entity {

	/**
	 * creates a new project with the given values
	 *
	 * @param name the project name
	 * @param description the project description
	 * @param creationTime the creation time of the project. The creation time must be of the following format: dd/mm/yyyy hh:mm.
	 * @param dueTime the due time of the project. The due time must be of the following format: dd/mm/yyyy hh:mm
	 * @param user the current user
	 * @throws IllegalArgumentException when one of the given parameters is not of a valid format. TODO: is dit nodig?
	 * @throws OperationNotPermittedException when the user doesn't have access to create a project
	 * @post a new project is created with the given attributes
	 */
	public Project(String name, String description, LocalDateTime creationTime, LocalDateTime dueTime, User user) {
		if(!(user instanceof ProjectManager)) {
			throw new OperationNotPermittedException("You don't have permission to create a project!");
		}
		setName(name);
		setDescription(description);
		setCreationTime(creationTime);
		setDueTime(dueTime);
		taskList = new ArrayList<>();
	}

    
    /**
     * Create a new task with the given parameters
     * @param description the description of the task
     * @param estimatedDuration the estimated duration of the task (in minutes)
     * @param acceptableDeviation the acceptable deviation of the task
     * @throws OperationNotPermittedException when the user is not allowed to create tasks
     * @post a new task is created and added to the project
     */
    public void createTask(String description, long estimatedDuration, double acceptableDeviation, User user) {
    	if(!(user instanceof ProjectManager)) {
    		throw new OperationNotPermittedException("you are not allowed to created tasks!");
    	}
    	Task t = new Task(description, estimatedDuration, acceptableDeviation);
    	addTaskToList(t);
    }

    /**
     * Adds a new task to the projects task list.
     *
     * @param task the task to add
     * @throws IllegalStateException the illegal state exception
     * @post The given task is added to the project
     */
    private void addTaskToList(Task task) throws IllegalStateException { // TODO: waarom gooit dit illegal state exception?
    	taskList.add(task);
    }
    
    /**
     * Returns the status of the project (active, finished, failed)
     * @return a String
     */
    public String getStatus(LocalDateTime systemtime) {
    	if (systemtime.isBefore(this.dueTime)) {
    		return "active";
		}
		for (Task task: taskList) {
			if (!task.isFinished()) {
				return "failed";
			}
		}
		return "finished";
	}

    /**
     * This method removes a task from the projects task list.
     *
     * @param task the task to remove from the project
	 * @post the task is removed from the project
     */
    public void removeTask(Task task) {
		taskList.remove(task);
	}

	// TODO: getTaskDetails() en getAvailableTaskDetails() heb ik verwijderd

    /**
     * Returns the name of the project.
     *
     * @return the name of the project
     */
    public String getName() {
		return name;
	}

	/**
	 * Sets the name of the project to the given name.
	 *
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
     * Returns the project description.
     *
     * @return the project description
     */
    public String getDescription() {
		return description;
	}

	/**
	 * Sets the project description to the given description.
	 *
	 * @param description the description of the project
	 * @throws IllegalArgumentException when de project description is null
	 * @post the project description is set to the given description
	 */
	private void setDescription(String description) {
		if(description == null) // TODO is dit echt nodig? Anders moet dit ook bij task gebeuren
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
     *
     * @return the creationTime of the project
     */
    public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time of the project.
	 *
	 * @param creationTime the creationTime of the project
	 * @throws IllegalArgumentException when the given date is null
	 * @post the creation time of the project is set to the given creation time
	 */
	private void setCreationTime(LocalDateTime creationTime) {
		if(creationTime == null) { // TODO: moet dit? anders moet dit ook in TASK
			throw new IllegalArgumentException("The creation time can't be null");
		}
		this.creationTime = creationTime;

	}

	/**
	 * The time of creation of the object.
	 */
	private LocalDateTime creationTime;

    /**
     * Returns the due time of the project.
     *
     * @return the dueTime of the project
     */
    public LocalDateTime getDueTime() {
		return dueTime;
	}

	/**
	 * Sets the due time of the project.
	 *
	 * @param dueTime the dueTime of the project
	 * @throws IllegalArgumentException when the due time is earlier or equal than the creation time or when the due time is null
	 * @post the due time of the project is set to the given due time
	 */
	private void setDueTime(LocalDateTime dueTime) {
		if (dueTime == null){ // TODO: moet dit anders bij Task dit ook
			throw new IllegalArgumentException("The due time can't be null");
		}
		if(dueTime.compareTo(creationTime) <= 0) // TODO: moet dit ook niet bij setCreationTime? Anders bij TASK dit ook
		{
			throw new IllegalArgumentException("The due time can't be before or equal to the start time.");
		}
		this.dueTime = dueTime;
	}

	/**
	 * The due time of the project.
	 */
	private LocalDateTime dueTime;


	/**
	 * Returns the task at the given index.
	 *
	 * @param index the index of the task
	 * @return the task at the given index in the tasklist
	 * @throws IndexOutOfBoundsException if the index is not in range of the tasklist
	 */
	public Task getTask(int index) throws IndexOutOfBoundsException{
		return taskList.get(index);
	}

    /**
     * Returns a list with all tasks of a project
     *
     * @return the tasks of the project
     */
    public ArrayList<Task> getTasks() {
		return ((ArrayList<Task>)taskList.clone());
	}

	/**
	 * Return the number of tasks in the project.
	 * @return an Integer.
	 */
	public Integer getNumberOfTasks() {
    	return this.taskList.size();
	}


	/**
	 * The list of tasks for the project.
	 */
	private ArrayList<Task> taskList;
	
	/**
	 * accepts a visitor 
	 * @param v the visitor to be accepted
	 */
	@Override
	public void accept(Visitor v) {
		v.visitProject(this);
	}

}
