package taskman.backend;

import taskman.Pair;
import taskman.backend.importexport.ImportExportException;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.task.Task;
import taskman.backend.time.Clock;
import taskman.backend.time.TimeParser;
import taskman.backend.user.OperationNotPermittedException;
import taskman.backend.user.User;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.ProjectWrapper;
import taskman.backend.wrappers.ResourceWrapper;
import taskman.backend.wrappers.UserWrapper;

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
     * Create a Controller for the given objects.
     * @param clock a Clock.
     * @param userManager a user management system.
     * @param projectOrganizer a project management system.
     * @throws NullPointerException if an argument is null.
     */
    public Controller(Clock clock, UserManager userManager, ProjectOrganizer projectOrganizer, ResourceManager resourceManager) throws NullPointerException{
        if (clock == null || userManager == null || projectOrganizer == null || resourceManager == null) {
            throw new NullPointerException("Arguments cannot be null!");
        }
        this.clock = clock;
        this.userManager = userManager;
        this.projectOrganizer = projectOrganizer;
        this.resourceManager = resourceManager;
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
     * @throws DateTimeParseException if the text cannot be parsed.
     * @throws IllegalArgumentException if the new time if before the old time.
     * @post the time of the system will be set to the given time.
     */
    public void updateTime(LocalDateTime newTime) throws DateTimeParseException, IllegalArgumentException {
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
     * @post a new user is added to the system.
     * @throws IllegalArgumentException when an user with the given name can't be found.
     * @throws IllegalArgumentException if the password is incorrect.
     * @throws IllegalStateException if the resource for the user cannot be removed.
     */
    public void removeUser(String name, String password) throws IllegalArgumentException, IllegalStateException {
        this.userManager.removeUser(name, password, resourceManager);
    }

    /**
     * Logs in with the given username and password.
     * @param name the name of the user to log in.
     * @param password the password of the user to log in with.
     * @throws IllegalArgumentException when an user with the given name can't be found.
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
        return this.projectOrganizer.getProjects();
    }

    /**
     * Return the status (active, finished, failed) of the project with the given name.
     * @param projectname a String.
     * @return a String.
     * @throws IllegalArgumentException if no project is found with the given name.
     */
    public String getProjectStatus(String projectname) throws IllegalArgumentException {
        return this.projectOrganizer.getProject(projectname).getStatus(this.clock.getTime());
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
    public void createProject(String name, String description, String dueTime) throws DateTimeParseException, IllegalArgumentException {
        LocalDateTime creationTimeObject = clock.getTime();
        LocalDateTime dueTimeObject = TimeParser.convertStringToLocalDateTime(dueTime);
        User user = this.userManager.getCurrentUser();
        this.projectOrganizer.createProject(name, description, creationTimeObject, dueTimeObject, user);
    }

    /**
     * Adds a task with the given properties.
     * @param projectName the project name.
     * @param taskName the name of the task.
     * @param description the description of the task.
     * @param estimatedDuration the estimated duration of the task as Long.
     * @param acceptableDeviation the acceptable deviation of the task as Double.
     * @throws IllegalArgumentException if no project is found with the given name.
     * @throws OperationNotPermittedException if no user is logged in.
     * @throws OperationNotPermittedException when the user is not allowed to create tasks
     * @throws NumberFormatException if estimatedDuration is not a Long or acceptableDeviation is not a Double.
     * @post a new task is created and added to the project in the system.
     */
    public void createTask(String projectName, String taskName, String description, long estimatedDuration, double acceptableDeviation) throws IllegalArgumentException, OperationNotPermittedException, NumberFormatException {
        Project project = this.projectOrganizer.getProject(projectName);
        User user = this.userManager.getCurrentUser();
        project.createTask(taskName,description, estimatedDuration,acceptableDeviation, user);
    }

    /**
     * Returns an iterator of the starting times for the given task.
     * @param projectName the project of the task.
     * @param taskName the name of the task.
     * @throws IllegalArgumentException if no project is found with the given name.
     * @throws IllegalArgumentException if no task exists with the given name.
     */
    public Iterator<LocalDateTime> getStartingsTimes(String projectName, String taskName) {
        Task task = this.projectOrganizer.getProject(projectName).getTask(taskName);
        return this.resourceManager.getStartingTimes(task, this.clock.getTime());
    }

    /**
     * Returns a list of available resources for the given resource type at the given startTime for the given task.
     * @param taskName the name of the task to get the available resources for.
     * @param startTime the start time on which the resources needs to be planned.
     * @return a list of available resources for the given resource type at the given startTime for the given task.
     */
    public List<ResourceWrapper> getAvailableResources(String projectName, String taskName, LocalDateTime startTime) {
        Task task = this.projectOrganizer.getProject(projectName).getTask(taskName);
        return new ArrayList<>(resourceManager.getAvailableResources(task, startTime));
    }

    /**
     * Returns a list of resources as alternatives for the given resource.
     * @param projectName the name of the project of the task.
     * @param taskName the name of the task.
     * @param wrapper a resource wrapper to search alternatives for.
     * @param startTime the start time on which the resources needs to be planned.
     * @return a list of resources as alternatives for the given resource and the given task at the given time.
     */
    public List<? extends ResourceWrapper> getAlternativeResources(String projectName, String taskName, ResourceWrapper wrapper, LocalDateTime startTime) {
        Task task = this.projectOrganizer.getProject(projectName).getTask(taskName);
        Resource resource = (Resource) wrapper;
        return resourceManager.getAlternativeResources(resource, task, startTime);
    }

    /**
     * Plans a task for execution.
     * @param projectName the name of the project of the task.
     * @param taskName the name of the task.
     * @param resources a list of resource types and resource names.
     * @param startTime the planned start time.
     */
    public void plan(String projectName, String taskName, List<Pair<String, String>> resources, LocalDateTime startTime) {
        // TODO
    }

    /**
     * Creates a constraint from a given string.
     * @param string a string which represents a constraint.
     * @post adds a constraint to the system.
     * @throws IllegalArgumentException if the string does not represent a valid constraint.
     * @throws NumberFormatException if a number in the string cannot be parsed to an integer.
     */
    public void addConstraint(String string) {
        this.resourceManager.createConstraint(string);
    }

    /**
     * Sets the alternative of the given task to the given alternative task
     * @param projectName the name of the project which holds both tasks
     * @param taskName the name of the task
     * @param alternativeTaskName the name of the alternative task
     * @throws IllegalArgumentException if the project does not exist.
     * @throws IndexOutOfBoundsException if the project does not contain the task or its alternative.
     * @throws IllegalStateException the task must be failed to set the alternative task.
     * @throws IllegalArgumentException the alternative may not be the same task or its alternative or
     *                                  one of its dependencies or one of these alternatives recursively.
     * @post the alternative task of the task is set to the given task.
     */
    public void addAlternativeToTask(String projectName, String taskName, String alternativeTaskName) throws IllegalArgumentException, IndexOutOfBoundsException, IllegalStateException {
        Project project = this.projectOrganizer.getProject(projectName);
        project.getTask(taskName).setAlternative(project.getTask(alternativeTaskName));
    }

    /**
     * Adds the given dependency to the given task
     * @param projectName the name of the project which holds both tasks.
     * @param taskName the index of the task.
     * @param dependencyTaskName the index of the dependency.
     * @throws IllegalArgumentException if the project does not exist.
     * @throws IndexOutOfBoundsException if the project does not contain the task or the dependency.
     * @throws IllegalArgumentException when the dependency is the task or its alternative or
     *                                  one of its dependencies or one of these alternatives recursively.
     * @throws IllegalStateException if the task is already finished or failed.
     * @post the dependency is added to the task.
     */
    public void addDependencyToTask(String projectName, String taskName, String dependencyTaskName) throws IllegalArgumentException, IndexOutOfBoundsException, IllegalStateException {
        Project project = this.projectOrganizer.getProject(projectName);
        project.getTask(taskName).addDependency(project.getTask(dependencyTaskName));
    }

    /**
     * adds a requirement to a task
     * @param projectName the name of the project
     * @param taskName the name of the task
     * @param resourceType the type of the resource
     * @param amount the amount of resources needed
     */
    public void addRequirementToTask(String projectName, String taskName, String resourceType, int amount) {
        Project p = this.projectOrganizer.getProject(projectName);
        p.getTask(taskName).addRequirement(resourceManager.getResourceType(resourceType), amount);
        resourceManager.testRequirements(p.getTask(taskName).getRequirements());
    }

    /**
     * Updates the status of the given task.
     * @param projectName the name of the project of the task.
     * @param taskName the name of the task to update.
     * @param startTime the start time of the task.
     * @param endTime the end time of the task.
     * @param status the new status of the task?
     * @throws DateTimeParseException if the start or end time cannot be parsed.
     * @throws IllegalArgumentException if the status does not exist.
     * @throws IllegalArgumentException if the project does not exist.
     * @throws IndexOutOfBoundsException if the project does not contain the task.
     * @throws IllegalArgumentException if the status is not FINISHED and not FAILED or if the start or end time is invalid.
     * @post the start time, end time and status of the task will be updated
     */
    public void updateTaskStatus(String projectName, String taskName, LocalDateTime startTime, LocalDateTime endTime, String status) throws DateTimeParseException, IllegalArgumentException, IndexOutOfBoundsException {
        // TODO: check if developer is member of task team!
        Task task = this.projectOrganizer.getProject(projectName).getTask(taskName);
        task.updateStatus(startTime, endTime, status);
    }

    /**
     * Save the status of the system to a file.
     * @param path a String with a location in the file system.
     * @throws ImportExportException if the system can't be saved to the file.
     */
    public void exportSystem(String path) throws ImportExportException {
        /* TODO:
        ImportExportHandler exporter = new ImportExportHandler();
        exporter.addClock(this.clock);
        exporter.addUser(this.user);
        exporter.addProjects(this.projects.values());
        exporter.exportToPath(path);
        */
    }

    /**
     * Restore the status of a system from a file.
     * @param path a String with a location in the file system.
     * @return a new Controller with the restored system.
     * @throws ImportExportException if the system can't be restored from the file.
     */
    public static Controller importSystem(String path) throws ImportExportException {
        /* TODO:
        ImportExportHandler importer = new ImportExportHandler();
        importer.importFromPath(path);
        return new Controller(importer.getClock(), importer.getUser(), importer.getProjects());
        */
        return null;
    }

}
