package taskman.backend;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import taskman.backend.importExport.ImportExportException;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.task.Task;
import taskman.backend.time.Clock;
import taskman.backend.time.TimeParser;
import taskman.backend.user.OperationNotPermittedException;
import taskman.backend.user.User;
import taskman.backend.user.UserManager;
import taskman.backend.visitor.DetailVisitor;

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
     * Return the time of the system.
     * @return the time of the system.
     */
    public String getSystemTime() {
        return TimeParser.convertLocalDateTimeToString(clock.getTime());
    }

    /**
     * Updates the time of the system.
     * @param newTime the new time of the system.
     * @throws DateTimeParseException if the text cannot be parsed.
     * @throws IllegalArgumentException if the new time if before the old time.
     * @post the time of the system will be set to the given time
     */
    public void updateSystemTime(String newTime) throws DateTimeParseException, IllegalArgumentException {
        clock.updateSystemTime(TimeParser.convertStringToLocalDateTime(newTime));
    }

    /**
     * Returns the name of the active user.
     * @return a string.
     * @throws OperationNotPermittedException if no user is logged in.
     */
    public String getCurrentUserName() throws OperationNotPermittedException {
        return this.userManager.getCurrentUser().getName();
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
     * Returns all project names.
     * @return a List of Strings.
     */
    public List<String> getProjectNames() {
        return this.projectOrganizer.getProjectNames();
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
     * Returns the details of the project with the given name.
     * @param name a String with the name of the project.
     * @return a Map with the details of the project with the given name.
     * @throws IllegalArgumentException if no project is found with the given name.
     */
    public Map<String, String> getProjectDetails(String name) throws IllegalArgumentException {
    	DetailVisitor v = new DetailVisitor(this.clock.getTime());
    	projectOrganizer.getProject(name).accept(v);
    	return v.getDetails();
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
     * Return the number of tasks of a project.
     * @param projectName the name of the project.
     * @return an Integer.
     * @throws IllegalArgumentException if no project is found with the given name.
     */
    public Integer getNumberOfTasks(String projectName) {
        return this.projectOrganizer.getProject(projectName).getNumberOfTasks();
    }

    /**
     * Returns the details of the given task
     * @param projectName the name of the project of the task
     * @param taskIndex the index of the task of which the details should be returned.
     * @return the details of the given task
     * @throws IllegalArgumentException if the project does not exist.
     */
    public Map<String, String> getTaskDetails(String projectName, Integer taskIndex) throws IllegalArgumentException {
    	DetailVisitor v = new DetailVisitor(this.clock.getTime());
    	Task t = projectOrganizer.getProject(projectName).getTask(taskIndex);
    	t.accept(v);
    	return v.getDetails();
    }

    /**
     * Adds a project with the properties from a given form.
     * @param projectName the project name.
     * @param description the description of the task.
     * @param estimatedDuration the estimated duration of the task as Long.
     * @param acceptableDeviation the acceptable deviation of the task as Double.
     * @throws IllegalArgumentException if no project is found with the given name.
     * @throws OperationNotPermittedException if no user is logged in.
     * @throws OperationNotPermittedException when the user is not allowed to create tasks
     * @throws NumberFormatException if estimatedDuration is not a Long or acceptableDeviation is not a Double.
     * @post a new task is created and added to the project in the system.
     */
    public void createTask(String projectName, String description, String estimatedDuration, String acceptableDeviation) throws IllegalArgumentException, OperationNotPermittedException, NumberFormatException {
        this.projectOrganizer.getProject(projectName).createTask(
                description,
                Long.parseLong(estimatedDuration),
                Double.parseDouble(acceptableDeviation),
                this.userManager.getCurrentUser()
        );
    }

    /**
     * Sets the alternative of the given task to the given alternative task
     * @param projectName the name of the project which holds both tasks
     * @param taskIndex the index of the task
     * @param alternativeTaskIndex the index of the alternative task
     * @throws IllegalArgumentException if the project does not exist.
     * @throws IndexOutOfBoundsException if the project does not contain the task or its alternative.
     * @throws IllegalStateException the task must be failed to set the alternative task.
     * @throws IllegalArgumentException the alternative may not be the same task or its alternative or
     *                                  one of its dependencies or one of these alternatives recursively.
     * @post the alternative task of the task is set to the given task.
     */
    public void addAlternativeToTask(String projectName, Integer taskIndex, Integer alternativeTaskIndex) throws IllegalArgumentException, IndexOutOfBoundsException, IllegalStateException {
        Project project = this.projectOrganizer.getProject(projectName);
        project.getTask(taskIndex).setAlternative(project.getTask(alternativeTaskIndex));
    }

    /**
     * Adds the given dependency to the given task
     * @param projectName the name of the project which holds both tasks.
     * @param taskIndex the index of the task.
     * @param dependencyTaskIndex the index of the dependency.
     * @throws IllegalArgumentException if the project does not exist.
     * @throws IndexOutOfBoundsException if the project does not contain the task or the dependency.
     * @throws IllegalArgumentException when the dependency is the task or its alternative or
     *                                  one of its dependencies or one of these alternatives recursively.
     * @throws IllegalStateException if the task is already finished or failed.
     * @post the dependency is added to the task.
     */
    public void addDependencyToTask(String projectName, Integer taskIndex, Integer dependencyTaskIndex) throws IllegalArgumentException, IndexOutOfBoundsException, IllegalStateException {
        Project project = this.projectOrganizer.getProject(projectName);
        project.getTask(taskIndex).addDependency(project.getTask(dependencyTaskIndex));
    }

    /**
     * Return the status of the task.
     * @param projectName the project fo the task.
     * @param taskIndex the index of the task.
     * @return the status of the task.
     * @throws IllegalArgumentException if the project does not exist.
     * @throws IndexOutOfBoundsException if the project does not contain the task or the dependency.
     */
    public String getTaskStatus(String projectName, Integer taskIndex) {
        // TODO: @Jeroen
        Project project = this.projectOrganizer.getProject(projectName);
        Task task = project.getTask(taskIndex);
        return task.getState().getStatus();
    }

    /**
     * Updates the status of the given task.
     * @param projectName the name of the project of the task
     * @param taskIndex the index of the task to update
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @param status the new status of the task
     * @throws DateTimeParseException if the start or end time cannot be parsed.
     * @throws IllegalArgumentException if the status does not exist.
     * @throws IllegalArgumentException if the project does not exist.
     * @throws IndexOutOfBoundsException if the project does not contain the task index.
     * @throws IllegalArgumentException if the status is not FINISHED and not FAILED or if the start or end time is invalid
     * @post the start time, end time and status of the task will be updated
     */
    public void updateTaskStatus(String projectName, Integer taskIndex, String startTime, String endTime, String status) throws DateTimeParseException, IllegalArgumentException, IndexOutOfBoundsException {
        // TODO: check if developer is member of task team!
        LocalDateTime startTimeObject = TimeParser.convertStringToLocalDateTime(startTime);
        LocalDateTime endTimeObject = TimeParser.convertStringToLocalDateTime(endTime);
        Task task = this.projectOrganizer.getProject(projectName).getTask(taskIndex);
        task.updateStatus(startTimeObject, endTimeObject, status);
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
