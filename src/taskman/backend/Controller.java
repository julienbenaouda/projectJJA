package taskman.backend;

import taskman.backend.importexport.ImportExportException;
import taskman.backend.importexport.XmlObject;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.task.Task;
import taskman.backend.time.Clock;
import taskman.backend.user.OperationNotPermittedException;
import taskman.backend.user.User;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class is responsible for redirecting calls of the user interface to the responsible objects of the backend.
 * @author Alexander Braekevelt
 */
public class Controller {

    /**
     * Represents the system time.
     */
    private Clock clock;

    /**
     * Represents the user management system.
     */
    private UserManager userManager;

    /**
     * Represents the project management system.
     */
    private ProjectOrganizer projectOrganizer;
    
    /**
     * represents the resource manager
     */
    private ResourceManager resourceManager;
    
    /**
     * represents the simulation manager
     */
    private SimulationManager simulationManager;

    /**
     * Create a Controller for the given objects.
     * @param clock a Clock.
     * @param userManager a user management system.
     * @param projectOrganizer a project management system.
     * @throws NullPointerException if an argument is null.
     */
    public Controller(Clock clock, UserManager userManager, ProjectOrganizer projectOrganizer, ResourceManager resourceManager, SimulationManager simulationManager) throws NullPointerException{
        if (clock == null || userManager == null || projectOrganizer == null || resourceManager == null || simulationManager == null) {
            throw new NullPointerException("Arguments cannot be null!");
        }
        this.clock = clock;
        this.userManager = userManager;
        this.projectOrganizer = projectOrganizer;
        this.resourceManager = resourceManager;
        this.simulationManager = simulationManager;
    }

    /**
     * Return the time.
     * @return the time.
     */
    public LocalDateTime getTime() {
        return this.clock.getTime();
    }

    /**
     * Updates the time.
     * @param newTime the new time.
     * @throws IllegalArgumentException if the new time if before the old time.
     * @post the time of the system will be set to the given time.
     */
    public void updateTime(LocalDateTime newTime) throws IllegalArgumentException {
        this.clock.updateTime(newTime);
    }

    /**
     * Returns the active user.
     * @return a UserWrapper.
     * @throws OperationNotPermittedException if no user is logged in.
     */
    public UserWrapper getCurrentUser() throws OperationNotPermittedException {
        return this.userManager.getCurrentUser();
    }

    /**
     * Returns a list of all users.
     * @return a list of UserWrappers.
     */
    public List<UserWrapper> getUsers() {
        return new ArrayList<>(this.userManager.getUsers());
    }

    /**
     * Return the possible user types.
     * @return a collection of user types.
     */
    public Collection<String> getUserTypes() {
        return this.userManager.getUserTypes();
    }

    /**
     * Adds a new user to the system.
     * @param name the name of the user.
     * @param password the password of the user.
     * @param type the type of user.
     * @throws IllegalArgumentException if the type is not valid.
     * @post a new user is added to the system.
     */
    public void createUser(String name, String password, String type, LocalTime startBreak) throws IllegalArgumentException {
        this.userManager.createUser(name, password, type, startBreak, resourceManager);
    }

    /**
     * Removes a user from the system.
     * @param user the user wrapper.
     * @param password the password of the user.
     * @post a user is removed from the system.
     * @throws IllegalArgumentException if the password is incorrect.
     * @throws IllegalStateException if the resource for the user cannot be removed.
     */
    public void removeUser(UserWrapper user, String password) throws IllegalArgumentException, IllegalStateException {
        this.userManager.removeUser((User) user, password, resourceManager);
    }

    /**
     * Logs in with the given username and password.
     * @param name the name of the user to log in.
     * @param password the password of the user to log in with.
     * @throws IllegalArgumentException when the password for the user with the given name is incorrect.
     * @post the user is logged in and is now used in the system.
     */
    public void login(String name, String password) throws IllegalArgumentException {
        this.userManager.login(name, password);
    }

    /**
     * Logout the current user in the system.
     */
    public void logout() {
        this.userManager.logout();
    }

    /**
     * Returns all projects.
     * @return a List of ProjectWrappers.
     */
    public List<ProjectWrapper> getProjects() {
        return new ArrayList<>(this.projectOrganizer.getProjects(this.userManager.getCurrentUser()));
    }

    /**
     * Return the status (active, finished, failed) of the project with the given name.
     * @param project a ProjectWrapper.
     * @return a String.
     */
    public String getProjectStatus(ProjectWrapper project) throws IllegalArgumentException {
        return ((Project) project).getStatus(this.clock.getTime());
    }

    /**
     * Add a project with the properties.
     * @param name the project name.
     * @param description the project description.
     * @param dueTime the due time of the project. (dd/mm/yyyy hh:mm)
     * @throws DateTimeParseException if the dueTime cannot be parsed.
     * @throws IllegalArgumentException when one of the given parameters is not of a valid format.
     * @post a project with the given properties will be added to the ProjectOrganizer.
     */
    public void createProject(String name, String description, LocalDateTime dueTime) throws DateTimeParseException, IllegalArgumentException {
        this.projectOrganizer.createProject(name, description, clock.getTime(), dueTime, this.userManager.getCurrentUser());
    }

    /**
     * Returns the tasks of a project.
     * @param project the project.
     * @return a list of tasks.
     */
    public List<TaskWrapper> getTasks(ProjectWrapper project) {
        return new ArrayList<>(((Project) project).getTasks(this.userManager.getCurrentUser()));
    }

    /**
     * Adds a task with the given properties.
     * @param project the project wrapper.
     * @param taskName the name of the task.
     * @param description the description of the task.
     * @param estimatedDuration the estimated duration of the task as Long.
     * @param acceptableDeviation the acceptable deviation of the task as Double.
     * @throws OperationNotPermittedException if no user is logged in.
     * @throws OperationNotPermittedException when the user is not allowed to create tasks
     * @throws NumberFormatException if estimatedDuration is not a Long or acceptableDeviation is not a Double.
     * @post a new task is created and added to the project in the system.
     */
    public void createTask(ProjectWrapper project, String taskName, String description, long estimatedDuration, double acceptableDeviation) throws IllegalArgumentException, OperationNotPermittedException, NumberFormatException {
        ((Project) project).createTask(taskName, description, estimatedDuration, acceptableDeviation, this.userManager.getCurrentUser());
    }

    /**
     * Returns an iterator of the starting times for the given task.
     * @param task the task wrapper.
     */
    public Iterator<LocalDateTime> getStartingsTimes(TaskWrapper task) {
        Task t = (Task) task;
        return this.resourceManager.getStartingTimes(t.getPlan(), t.getEstimatedDuration(), this.clock.getTime()); // TODO: @Jeroen, via task?
    }

    /**
     * Initializes a plan for a task.
     * @param task a task.
     * @param startTime the start time for the plan.
     * @throws IllegalStateException if the state is not unavailable.
     */
    public void initializePlan(TaskWrapper task, LocalDateTime startTime) throws IllegalStateException {
        ((Task) task).initializePlan(this.resourceManager, startTime);
    }

    /**
     * Get the resources of the plan of a task.
     * @param task a task with a plan.
     * @return a list of resources.
     * @throws IllegalStateException if the state is not planned.
     */
    public List<ResourceWrapper> getPlannedResources(TaskWrapper task) throws IllegalStateException {
        return new ArrayList<>(((Task) task).getPlannedResources());
    }

    /**
     * Returns a list of resources as alternatives for the given resource.
     * @param task the task.
     * @param resource a resource wrapper to search alternatives for.
     * @return a list of resources as alternatives for the given resource and the given task.
     * @throws IllegalStateException if the state is not planned.
     */
    public List<ResourceWrapper> getAlternativeResources(TaskWrapper task, ResourceWrapper resource) throws IllegalStateException {
        return new ArrayList<>(((Task) task).getAlternativeResources(this.resourceManager, (Resource) resource));
    }

    /**
     * Change a resource of a plan of a task.
     * @param task a task with a plan.
     * @param oldResource the resource to change.
     * @param newResource the resource to change to.
     * @throws IllegalStateException if the state is not planned.
     */
    public void changeResource(TaskWrapper task, ResourceWrapper oldResource, ResourceWrapper newResource) throws IllegalStateException {
        ((Task) task).changeResource((Resource) oldResource, (Resource) newResource);
    }

    /**
     * Cancel the plan of a task.
     * @param task a task with a plan.
     * @throws IllegalStateException if the state is not planned.
     */
    public void cancelPlan(TaskWrapper task) throws IllegalStateException {
        ((Task) task).cancelPlan();
    }

    /**
     * Returns a list of the resource types.
     * @return a list of the resource types.
     */
    public List<ResourceTypeWrapper> getResourceTypes() {
        return new ArrayList<>(this.resourceManager.getResourceTypes());
    }

    /**
     * Creates and adds the resource type with the given name to the resource types.
     * @param name the name of the resource type
     * @post a resource type with given name is created and added to the resource types
     */
    public void createResourceType(String name) {
        this.resourceManager.createResourceType(name);
    }

    /**
     * Creates a constraint from a given string.
     * @param string a string which represents a constraint.
     * @post adds a constraint to the system.
     * @throws IllegalArgumentException if the string does not represent a valid constraint.
     * @throws NumberFormatException if a number in the string cannot be parsed to an integer.
     */
    public void createConstraint(String string) {
        this.resourceManager.createConstraint(string);
    }

    /**
     * Creates a new resource with given name.
     * @param type the type of the resource.
     * @param name the name of the resource.
     * @throws IllegalArgumentException when the name is null or already exists.
     */
    public void createResource(ResourceTypeWrapper type, String name) {
        ((ResourceType) type).createResource(name);
    }

    /**
     * Sets the alternative of the given task to the given alternative task
     * @param task the task.
     * @param alternative the alternative task.
     * @throws IllegalStateException the task must be failed to set the alternative task.
     * @throws IllegalArgumentException the alternative may not be the same task or its alternative or
     *                                  one of its dependencies or one of these alternatives recursively.
     * @post the alternative task of the task is set to the given task.
     */
    public void addAlternativeToTask(TaskWrapper task, TaskWrapper alternative) throws IllegalArgumentException, IndexOutOfBoundsException, IllegalStateException {
        ((Task) task).setAlternative((Task) alternative);
    }

    /**
     * Adds the given dependency to the given task
     * @param task the task.
     * @param dependency the dependency.
     * @throws IllegalArgumentException when the dependency is the task or its alternative or
     *                                  one of its dependencies or one of these alternatives recursively.
     * @throws IllegalStateException if the task is already finished or failed.
     * @post the dependency is added to the task.
     */
    public void addDependencyToTask(TaskWrapper task, TaskWrapper dependency) throws IllegalArgumentException, IndexOutOfBoundsException, IllegalStateException {
        ((Task) task).addDependency((Task) dependency);
    }

    /**
     * adds a requirement to a task
     * @param task the task.
     * @param resourceType the type of the resource.
     * @param amount the amount of resources needed.
     */
    public void addRequirementToTask(TaskWrapper task, ResourceTypeWrapper resourceType, int amount) {
        ((Task) task).addRequirement(resourceManager, (ResourceType) resourceType, amount);
    }

    /**
     * Ends the execution of the task.
     * @param task the task to update.
     * @param startTime the start time of the task.
     * @param endTime the end time of the task.
     * @param status the new status of the task.
     * @throws DateTimeParseException if the start or end time cannot be parsed.
     * @throws IllegalArgumentException if the status does not exist.
     * @throws IllegalArgumentException if the status is not FINISHED and not FAILED or if the start or end time is invalid.
     * @post the start time, end time and status of the task will be updated.
     */
    public void endTaskExecution(TaskWrapper task, LocalDateTime startTime, LocalDateTime endTime, String status) throws DateTimeParseException, IllegalArgumentException, IndexOutOfBoundsException {
        ((Task) task).endExecution(startTime, endTime, status, this.userManager.getCurrentUser());
    }

	/**
	 * Makes a task executing.
	 * @post the status of the task is changed to executing
	 * @throws IllegalArgumentException when the user is not assigned to the task.
	 * @throws IllegalArgumentException if the plan cannot be rescheduled to this time.
	 */
	public void makeExecuting(TaskWrapper task) throws IllegalArgumentException {
		((Task) task).makeExecuting(this.resourceManager, this.clock.getTime(), this.userManager.getCurrentUser());
	}

    /**
     * Save the status of the system to a file.
     * @param path a String with a location in the file system.
     * @post the system is saved to the file.
     * @throws ImportExportException if the system can't be saved to the file.
     */
    public void exportSystem(String path) throws ImportExportException {
        XmlObject xml = new XmlObject(this.projectOrganizer, this.userManager, this.resourceManager, this.clock);
        xml.saveToFile(path);
    }

    /**
     * Restore the status of a system from a file.
     * @param path a String with a location in the file system.
     * @post the system is restored from the file.
     * @throws ImportExportException if the system can't be restored from the file.
     */
    public void importSystem(String path) throws ImportExportException {
        XmlObject xml = XmlObject.restoreFromFile(path);
        this.projectOrganizer = xml.getProjectOrganizer();
        this.userManager = xml.getUserManager();
        this.resourceManager = xml.getResourceManager();
        this.clock = xml.getClock();
    }
    
    /**
     * starts the simulation
     * @throws ImportExportException 
     * @throws OperationNotPermittedException 
     */
    public void startSimulation() throws OperationNotPermittedException, ImportExportException {
    	simulationManager.startSimulation(projectOrganizer, userManager, resourceManager, clock, userManager.getCurrentUser());
    }

    /**
     * cancels the simulation
     * @throws IllegalStateException when the simulation can't be cancelled
     * @throws ImportExportException when the simulation can't cancelled
     */
	public void cancelSimulation() throws IllegalStateException, ImportExportException {
		XmlObject obj = simulationManager.cancelSimulation();
		setResourceManager(obj.getResourceManager());
		setProjectOrganizer(obj.getProjectOrganizer());
		setUserManager(obj.getUserManager());
		setClock(obj.getClock());
	}

	/**
	 * keeps the made simulation
	 */
	public void executeSimulation() {
		simulationManager.executeSimulation();
	}

	/**
	 * sets the clock to the given clock
	 * @param clock the clock of the controller
	 */
	private void setClock(Clock clock) {
		this.clock = clock;
	}

	/**
	 * sets the user manager to the given user manager
	 * @param userManager the userManager of the controller
	 */
	private void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * sets the project organizer to the given organizer
	 * @param projectOrganizer the projectOrganizer of the controller
	 */
	private void setProjectOrganizer(ProjectOrganizer projectOrganizer) {
		this.projectOrganizer = projectOrganizer;
	}

	/**
	 * sets the resourcemanager to the given resource manager
	 * @param resourceManager the resourceManager of the controller
	 */
	private void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

}
