package taskman.backend;

import taskman.backend.importExport.ImportExportException;
import taskman.backend.importExport.XmlObject;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
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
        return this.projectOrganizer.getProjects();
    }

    /**
     * Return the status (active, finished, failed) of the project with the given name.
     * @param project a ProjectWrapper.
     * @return a String.
     * @throws IllegalArgumentException if no project is found with the given name.
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
        ((Project) project).createTask(taskName, description, estimatedDuration, acceptableDeviation, resourceManager, this.userManager.getCurrentUser());
    }

    /**
     * Returns an iterator of the starting times for the given task.
     * @param task the task wrapper.
     */
    public Iterator<LocalDateTime> getStartingsTimes(TaskWrapper task) {
        return this.resourceManager.getStartingTimes((Task) task, this.clock.getTime());
    }

    /**
     * Returns a list of available resources for the given resource type at the given startTime for the given task.
     * @param task the task to get the available resources for.
     * @param startTime the start time on which the resources needs to be planned.
     * @return a list of available resources for the given resource type at the given startTime for the given task.
     */
    public List<ResourceWrapper> getAvailableResources(TaskWrapper task, LocalDateTime startTime) {
        return new ArrayList<>(resourceManager.getAvailableResources((Task) task, startTime));
    }

    /**
     * Returns a list of resources as alternatives for the given resource.
     * @param task the task.
     * @param resource a resource wrapper to search alternatives for.
     * @param startTime the start time on which the resources needs to be planned.
     * @return a list of resources as alternatives for the given resource and the given task at the given time.
     */
    public List<ResourceWrapper> getAlternativeResources(TaskWrapper task, ResourceWrapper resource, LocalDateTime startTime) {
        return new ArrayList<>(resourceManager.getAlternativeResources((Resource) resource, (Task) task, startTime));
    }

    /**
     * Plans a task for execution.
     * @param task the task.
     * @param resources a list of resource types and resource names.
     * @param startTime the planned start time.
     */
    public void plan(TaskWrapper task, List<ResourceWrapper> resources, LocalDateTime startTime) {
        // TODO
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
     * Updates the status of the given task.
     * @param task the task to update.
     * @param startTime the start time of the task.
     * @param endTime the end time of the task.
     * @param status the new status of the task.
     * @throws DateTimeParseException if the start or end time cannot be parsed.
     * @throws IllegalArgumentException if the status does not exist.
     * @throws IllegalArgumentException if the status is not FINISHED and not FAILED or if the start or end time is invalid.
     * @post the start time, end time and status of the task will be updated.
     */
    public void updateTaskStatus(TaskWrapper task, LocalDateTime startTime, LocalDateTime endTime, String status) throws DateTimeParseException, IllegalArgumentException, IndexOutOfBoundsException {
        // TODO: check if developer is member of task team!
        ((Task) task).updateStatus(startTime, endTime, status, this.getCurrentUser());
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

}
