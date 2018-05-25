package taskman.backend;

import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.importexport.ImportExportException;
import taskman.backend.importexport.XmlObject;
import taskman.backend.constraint.ConstraintParser;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectManager;
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
import java.util.*;

/**
 * This class represents the facade controller. It is responsible for redirecting calls of the user interface to the responsible objects of the backend.
 * It contains references to the clock, the branch office manager and the simulation manager.
 * @author Alexander Braekevelt, Julien Benaouda
 */
public class Controller {

	/**
	 * Create a Controller for the given objects.
	 * @param clock a Clock.
	 * @param branchOfficeManager a branch office management system.
	 * @throws NullPointerException if an argument is null.
	 */
	public Controller(Clock clock, BranchOfficeManager branchOfficeManager, SimulationManager simulationManager) throws NullPointerException {
		if (clock == null || branchOfficeManager == null || simulationManager == null) {
			throw new NullPointerException("Arguments cannot be null!");
		}
		setClock(clock);
		setBranchOfficeManager(branchOfficeManager);
		setSimulationManager(simulationManager);
	}

    /**
     * Represents the system time.
     */
    private Clock clock;

	/**
	 * Returns the clock.
	 * @return a Clock.
	 */
	private Clock getClock() {
		return this.clock;
	}

	/**
	 * Sets the clock to the given clock.
	 * @param clock the clock of the controller.
	 */
	private void setClock(Clock clock) {
		this.clock = clock;
	}

    /**
     * Represents the branch office manager.
     */
    private BranchOfficeManager branchOfficeManager;

	/**
	 * Returns the BranchOfficeManager.
	 * @return a BranchOfficeManager.
	 */
	private BranchOfficeManager getBranchOfficeManager() {
		return this.branchOfficeManager;
	}

	/**
	 * Sets the BranchOfficeManager to the given BranchOfficeManager.
	 * @param branchOfficeManager the BranchOfficeManager of the controller.
	 */
	private void setBranchOfficeManager(BranchOfficeManager branchOfficeManager) {
		this.branchOfficeManager = branchOfficeManager;
	}
    
    /**
     * Represents the simulation manager.
     */
    private SimulationManager simulationManager;

	/**
	 * Returns the SimulationManager.
	 * @return a SimulationManager.
	 */
	private SimulationManager getSimulationManager() {
    	return this.simulationManager;
    }

	/**
	 * Sets the SimulationManager to the given SimulationManager.
	 * @param simulationManager the given SimulationManager.
	 * @post the SimulationManager is set to the given SimulationManager.
	 */
	private void setSimulationManager(SimulationManager simulationManager) {
		this.simulationManager = simulationManager;
    }

	/**
	 * Returns the user manager of the current branch.
	 * @return a UserManager.
	 * @throws IllegalStateException if no current branch office is found.
	 */
	private UserManager getCurrentUserManager() throws IllegalStateException {
		return getBranchOfficeManager().getCurrentBranchOffice().getUserManager();
	}

	/**
	 * Returns the project manager of the current branch office.
	 * @return a ProjectManager.
	 */
	private ProjectManager getCurrentProjectManager() {
		return getBranchOfficeManager().getCurrentBranchOffice().getProjectManager();
	}

	/**
	 * Returns the resource manager of the current branch office.
	 * @return a ResourceManager.
	 */
	private ResourceManager getCurrentResourceManager() {
		return getBranchOfficeManager().getCurrentBranchOffice().getResourceManager();
	}

    /**
     * Return the time.
     * @return the time.
     */
    public LocalDateTime getTime() {
        return getClock().getTime();
    }

    /**
     * Updates the time.
     * @param newTime the new time.
     * @throws IllegalArgumentException if the new time if before the old time.
     * @post the time of the system will be set to the given time.
     */
    public void updateTime(LocalDateTime newTime) throws IllegalArgumentException {
        getClock().updateTime(newTime);
    }

	/**
	 * Returns a list of branch offices.
	 * @return a list of branch offices.
	 */
	public List<BranchOfficeWrapper> getBranchOffices() {
    	return new ArrayList<>(getBranchOfficeManager().getBranchOffices());
    }

	/**
	 * Creates a new branch office with the given name.
	 * @param name the name of the branch office.
	 * @post adds the branch office to the list of branch offices.
	 * @throws IllegalArgumentException if a branch office with the given name already exists.
	 */
    public void createBranchOffice(String name) {
		getBranchOfficeManager().createBranchOffice(name);
    }

    /**
     * Returns the active user.
     * @return a UserWrapper.
     * @throws OperationNotPermittedException if no user is logged in.
     * @throws IllegalStateException if no current branch office is found.
     */
    public UserWrapper getCurrentUser() throws OperationNotPermittedException, IllegalStateException {
        return getCurrentUserManager().getCurrentUser();
    }

    /**
     * Returns a list of all users of a branch office.
     * @param office a branch office.
     * @return a list of UserWrappers.
     */
    public List<UserWrapper> getUsers(BranchOfficeWrapper office) {
        return new ArrayList<>(((BranchOffice) office).getUserManager().getUsers());
    }

    /**
     * Return the possible user types.
     * @param office a branch office.
     * @return a collection of user types.
     */
    public Collection<String> getUserTypes(BranchOfficeWrapper office) {
	    return ((BranchOffice) office).getUserManager().getUserTypes();
    }

    /**
     * Adds a new user to the system.
     * @param office the branch office of the new user.
     * @param name the name of the user.
     * @param password the password of the user.
     * @param type the type of user.
     * @throws IllegalArgumentException if the type is not valid.
     * @post a new user is added to the system.
     */
    public void createUser(BranchOfficeWrapper office, String name, String password, String type, LocalTime startBreak) throws IllegalArgumentException {
	    ((BranchOffice) office).getUserManager().createUser(name, password, type, startBreak, ((BranchOffice) office).getResourceManager());
    }

    /**
     * Removes a user from the system.
     * @param office the branch office of the user.
     * @param user the user wrapper.
     * @param password the password of the user.
     * @post a user is removed from the system.
     * @throws IllegalArgumentException if the password is incorrect.
     * @throws IllegalStateException if the resource for the user cannot be removed.
     */
    public void removeUser(BranchOfficeWrapper office, UserWrapper user, String password) throws IllegalArgumentException, IllegalStateException {
	    ((BranchOffice) office).getUserManager().removeUser((User) user, password, ((BranchOffice) office).getResourceManager());
    }

    /**
     * Logs in with the given username and password.
     * @param name the name of the user to log in.
     * @param password the password of the user to log in with.
     * @throws IllegalArgumentException when the password for the user with the given name is incorrect.
     * @post the user is logged in and is now used in the system.
     */
    public void login(BranchOfficeWrapper office, String name, String password) throws IllegalArgumentException {
    	try {
		    getBranchOfficeManager().changeCurrentBranchOffice((BranchOffice) office);
		    ((BranchOffice) office).getUserManager().login(name, password);
	    } catch (IllegalArgumentException e) {
		    ((BranchOffice) office).getUserManager().logout();
		    getBranchOfficeManager().deactivateCurrentBranchOffice();
    		throw e;
	    }
    }

    /**
     * Logout the current user in the system.
     */
    public void logout() {
        getBranchOfficeManager().getCurrentBranchOffice().getUserManager().logout();
	    getBranchOfficeManager().deactivateCurrentBranchOffice();
    }

    /**
     * Add a project with the properties.
     * @param name the project name.
     * @param description the project description.
     * @param dueTime the due time of the project. (dd/mm/yyyy hh:mm)
     * @throws DateTimeParseException if the dueTime cannot be parsed.
     * @throws IllegalArgumentException when one of the given parameters is not of a valid format.
     * @throws OperationNotPermittedException when the user doesn't have access to create a project.
     * @post a project with the given properties will be added to the ProjectOrganizer.
     */
    public void createProject(String name, String description, LocalDateTime dueTime) throws DateTimeParseException, IllegalArgumentException, OperationNotPermittedException {
	    getCurrentProjectManager().createProject(name, description, getClock().getTime(), dueTime, getCurrentUserManager().getCurrentUser());
    }

	/**
	 * Returns all projects.
	 * @return a List of ProjectWrappers.
	 */
	public List<ProjectWrapper> getProjects() {
		return new ArrayList<>(getCurrentProjectManager().getProjects(getCurrentUserManager().getCurrentUser()));
	}

	/**
	 * Return the status of the project with the given name.
	 * @param project a ProjectWrapper.
	 * @return a String.
	 */
	public String getProjectStatus(ProjectWrapper project) throws IllegalArgumentException {
		return ((Project) project).getStatus(getClock().getTime());
	}

    /**
     * Returns the tasks of a project.
     * @param project the project.
     * @return a list of tasks.
     */
    public List<TaskWrapper> getTasks(ProjectWrapper project) {
        return new ArrayList<>(((Project) project).getTasks(getCurrentUserManager().getCurrentUser()));
    }

    /**
     * Adds a task with the given properties.
     * @param projectWrapper the project wrapper.
     * @param taskName the name of the task.
     * @param description the description of the task.
     * @param estimatedDuration the estimated duration of the task as Long.
     * @param acceptableDeviation the acceptable deviation of the task as Double.
     * @param requirements the requirements of the task.
     * @throws IllegalArgumentException if no task exists with the given name.
     * @throws OperationNotPermittedException if no user is logged in.
     * @throws OperationNotPermittedException when the user is not allowed to create tasks.
     * @post a new task is created and added to the project in the system.
     */
    public void createTask(ProjectWrapper projectWrapper, String taskName, String description, long estimatedDuration, double acceptableDeviation, Map<ResourceTypeWrapper, Integer> requirements) throws IllegalArgumentException, OperationNotPermittedException {
        Project project = ((Project) projectWrapper);
        project.createTask(taskName, description, estimatedDuration, acceptableDeviation, getCurrentUserManager().getCurrentUser());
        Task task = project.getTask(taskName);
        try {
	        for (ResourceTypeWrapper rtw : requirements.keySet()) {
		        addRequirementToTask(task, rtw, requirements.get(rtw));
	        }
        } catch (Exception e) {
        	project.removeTask(task);
        	throw e;
        }
    }

	/**
	 * Delegates a task to a given branch office.
	 * @param task the task to delegate.
	 * @param office the branch office to delegate to.
	 */
	public void delegateTask(TaskWrapper task, BranchOfficeWrapper office) {
	    ((Task) task).delegate((BranchOffice) office, getClock().getTime());
    }

    /**
     * Returns an iterator of the starting times for the given task.
     * @param task the task wrapper.
     */
    public Iterator<LocalDateTime> getStartingTimes(TaskWrapper task) {
        Task t = (Task) task;
        return getCurrentResourceManager().getStartingTimes(t.getPlan(), t.getEstimatedDuration(), getClock().getTime());
    }

    /**
     * Initializes a plan for a task.
     * @param task a task.
     * @param startTime the start time for the plan.
     * @throws IllegalStateException if the state is not unavailable.
     */
    public void initializePlan(TaskWrapper task, LocalDateTime startTime) throws IllegalStateException {
        ((Task) task).initializePlan(startTime);
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
        return new ArrayList<>(((Task) task).getAlternativeResources((Resource) resource));
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
        return new ArrayList<>(this.getCurrentResourceManager().getResourceTypes());
    }

    /**
     * Creates and adds the resource type with the given name to the resource types.
     * @param name the name of the resource type
     * @post a resource type with given name is created and added to the resource types
     */
    public void createResourceType(String name) {
        this.getCurrentResourceManager().createResourceType(name);
    }

    /**
     * Adds the given constraint.
     * @param string the constraint to parse add.
     * @post the string is parsed and then added to the system.
     * @throws IllegalArgumentException if the string does not represent a valid constraint
     * @throws NumberFormatException if a number in the string cannot be parsed to an integer
     */
    public void addConstraint(String string) {
        this.getCurrentResourceManager().addConstraint(ConstraintParser.parse(string, getCurrentResourceManager()));
    }

    /**
     * Creates a new resource with given name.
     * @param type the type of the resource.
     * @param name the name of the resource.
     * @throws IllegalArgumentException when the name is null or already exists.
     */
    public void createResource(ResourceTypeWrapper type, String name) {
    	if (type.getName().equals("developer")) {
    		throw new IllegalArgumentException("Cannot create a resource for a developer!");
	    }
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
    private void addRequirementToTask(TaskWrapper task, ResourceTypeWrapper resourceType, int amount) {
        ((Task) task).addRequirement(getCurrentResourceManager(), (ResourceType) resourceType, amount);
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
        ((Task) task).endExecution(startTime, endTime, status, getCurrentUserManager().getCurrentUser());
    }

	/**
	 * Makes a task executing.
	 * @post the status of the task is changed to executing
	 * @throws IllegalArgumentException when the user is not assigned to the task.
	 * @throws IllegalArgumentException if the plan cannot be rescheduled to this time.
	 */
	public void makeExecuting(TaskWrapper task) throws IllegalArgumentException {
		((Task) task).makeExecuting(getCurrentResourceManager(), getClock().getTime(), getCurrentUserManager().getCurrentUser());
	}

    /**
     * Save the status of the system to a file.
     * @param path a String with a location in the file system.
     * @post the system is saved to the file.
     * @throws ImportExportException if the system can't be saved to the file.
     */
    public void exportSystem(String path) throws ImportExportException {
        XmlObject xml = new XmlObject(getBranchOfficeManager(), getClock());
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
        setBranchOfficeManager(xml.getBranchOfficeManager());
        setClock(xml.getClock());
    }
    
    /**
     * Starts the simulation.
     * @throws OperationNotPermittedException if no user is logged in.
     * @throws ImportExportException if there occurs an error while starting the simulation.
     */
    public void startSimulation() throws OperationNotPermittedException, ImportExportException {
    	getSimulationManager().startSimulation(getBranchOfficeManager(), getClock(), getCurrentUserManager().getCurrentUser());
    }

    /**
     * Cancels the simulation.
     * @throws IllegalStateException when the simulation can't be cancelled.
     * @throws ImportExportException when the simulation can't cancelled.
     */
	public void cancelSimulation() throws IllegalStateException, ImportExportException {
		XmlObject obj = getSimulationManager().cancelSimulation();
		setBranchOfficeManager(obj.getBranchOfficeManager());
		setClock(obj.getClock());
	}

	/**
	 * Executes the simulated actions in the real system.
	 */
	public void executeSimulation() {
		getSimulationManager().executeSimulation();
	}

}
