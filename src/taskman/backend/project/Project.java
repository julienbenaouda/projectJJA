package taskman.backend.project;


import taskman.backend.task.Task;
import taskman.backend.user.OperationNotPermittedException;
import taskman.backend.user.ProjectManager;
import taskman.backend.user.User;
import taskman.backend.wrappers.ProjectWrapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents a project.
 * @author Julien Benaouda, Jeroen Van Der Donckt
 */
public class Project implements ProjectWrapper {

	/**
	 * Creates a new project with the given values.
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
	 * The list of tasks for the project.
	 */
	private ArrayList<Task> taskList;

	/**
	 * Returns a list with all tasks of a project
	 * @return the tasks of the project.
	 */
	public List<Task> getTasks() {
		return new ArrayList<>(taskList);
	}

	/**
	 * Returns a list with all tasks the user has access to.
	 * @param user the user.
	 * @return a list with all tasks the user has access to.
	 */
	public List<Task> getTasks(User user) {
		return this.taskList.stream().filter(t -> t.hasAccessTo(user)).collect(Collectors.toList());
	}

	/**
	 * Return the Task with the given name.
	 * @param name a String.
	 * @return a Task.
	 * @throws IllegalArgumentException if no task exists with the given name.
	 */
	public Task getTask(String name) throws IllegalArgumentException {
		for (Task t: this.taskList) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		throw new IllegalArgumentException("A task with the given name does not exist!");
	}

    /**
     * Create a new task with the given parameters
     * @param name the name of the task.
     * @param description the description of the task
     * @param estimatedDuration the estimated duration of the task (in minutes)
     * @param acceptableDeviation the acceptable deviation of the task
     * @throws OperationNotPermittedException when the user is not allowed to create tasks
     * @post a new task is created and added to the project
     */
    public void createTask(String name, String description, long estimatedDuration, double acceptableDeviation, User user) {
    	if(!(user instanceof ProjectManager)) {
    		throw new OperationNotPermittedException("you are not allowed to created tasks!");
    	}
    	Task t = new Task(name, description, estimatedDuration, acceptableDeviation);
    	addTask(t);
    }

    /**
     * Adds a new task to the projects task list.
     * @param task the task to add.
     * @post The given task is added to the project.
     */
    private void addTask(Task task) {
    	taskList.add(task);
    }

    /**
     * This method removes a task from the projects task list.
     * @param task the task to remove from the project.
	 * @post the task is removed from the project.
     */
    private void removeTask(Task task) {
		taskList.remove(task);
	}

	/**
	 * Returns if the given user has access to this project.
	 * @param user a User.
	 * @return if the given user has access to this project.
	 */
	public boolean hasAccessTo(User user) {
		return this.taskList.stream().anyMatch(t -> t.hasAccessTo(user));
	}

	/**
	 * The name of the project.
	 */
	private String name;

	/**
	 * Returns the name of the project.
	 * @return the name of the project.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the project to the given name.
	 * @param name the name of the project.
	 * @throws IllegalArgumentException when the name is empty.
	 * @post the name of the project is set to the given name.
	 */
	private void setName(String name) {
		if(name.equals("") || name == null)
		{
			throw new IllegalArgumentException("The project name can't be empty. Please enter a valid project name to proceed");
		}
		this.name = name.trim();
	}

	/**
	 * The project description.
	 */
	private String description;

	/**
	 * Returns the project description.
	 * @return the project description.
	 */
    @Override
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the project description to the given description.
	 * @param description the description of the project.
	 * @throws IllegalArgumentException when de project description is null.
	 * @post the project description is set to the given description.
	 */
	private void setDescription(String description) {
		if(description == null) // TODO is dit echt nodig? Anders moet dit ook bij task gebeuren
		{
			throw new IllegalArgumentException("The description can't be null");
		}
		this.description = description.trim();
	}


	/**
	 * Return the creation time of the project.
	 * @return a LocalDateTime.
	 */
	@Override
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time of the project.
	 * @param creationTime the creationTime of the project.
	 * @throws IllegalArgumentException when the given date is null.
	 * @post the creation time of the project is set to the given creation time.
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
	 * Return the creation time of the project.
	 * @return a LocalDateTime.
	 */
	@Override
	public LocalDateTime getDueTime() {
		return dueTime;
	}

	/**
	 * Sets the due time of the project.
	 * @param dueTime the dueTime of the project.
	 * @throws IllegalArgumentException when the due time is earlier or equal than the creation time or when the due time is null.
	 * @post the due time of the project is set to the given due time.
	 */
	private void setDueTime(LocalDateTime dueTime) {
		if (dueTime == null){ // TODO: moet dit anders bij task dit ook
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
	 * Return the status of the project at the given time.
	 * @param time a LocalDateTime.
	 * @return a String.
	 */
	public String getStatus(LocalDateTime time) {
		if (time.isBefore(this.dueTime)) {
			return "active";
		}
		for (Task task: taskList) {
			if (!task.getStatus().equals("finished")) {
				return "failed";
			}
		}
		return "finished";
	}


}
