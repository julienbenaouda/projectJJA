package taskman;

import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * This class is responsible for redirecting calls of the user interface to the responsible objects of the backend.
 * @author Alexander Braekevelt
 */
public class Controller {

    /**
     * Represents the clock.
     */
    private Clock clock;

    /**
     * Represents the user.
     */
    private User user;

    /**
     * Represents the projects with their unique ids.
     */
    private Map<String, Project> projects;

    /**
     * Create a Controller for the given objects.
     * @param clock a Clock.
     * @param user a User.
     * @param projects a Collection of Projects.
     */
    public Controller(Clock clock, User user, Collection<Project> projects) {
        this.clock = clock;
        this.user = user;
        this.projects = new HashMap<>();
        for (Project project: projects) {
            this.projects.put(project.getName(), project);
        }
    }

    /**
     * Returns a list with all project names.
     * @return a list of strings.
     */
    public List<String> getProjectNames() {
        return new ArrayList<>(this.projects.keySet());
    }

    /**
     * Returns if a project with the given name exists.
     * @param name the name to check
     * @return the boolean
     */
    public Boolean projectExists(String name) {
        return projects.containsKey(name);
    }

    /**
     * Returns the project with the given name.
     * @param name the name of the project.
     * @return a project.
     * @throws IllegalArgumentException if the project does not exist.
     */
    private Project getProject(String name) throws IllegalArgumentException {
        if (projectExists(name)) {
            return projects.get(name);
        }
        else {
            throw new IllegalArgumentException("The given project name does not exist!");
        }
    }

    /**
     * Adds a project to the controller.
     * @param name the name of the project
     * @param project the project to add
     * @post the project will be added to the system.
     * @throws IllegalArgumentException if a project with the given name already exist
     */
    private void addProject(String name, Project project) throws IllegalArgumentException {
        if (projectExists(name)) {
            throw new IllegalArgumentException("The given project name does already exist!");
        }
        else {
            projects.put(name, project);
        }
    }

    /**
     * This method generates a form containing all parameters needed to create a new project.
     * All values are empty and can be filled in, and then passed back to create a project.
     * @return A map containing all elements that need to be filled in to create a new project
     */
    public HashMap<String,String> getProjectCreationForm() {
        return Project.getCreationForm();
    }

    /**
     * Adds a project with the properties from a given form.
     * @param form the creation form for the project
     * @throws IllegalArgumentException when one of the parameters is abscent or not valid.
     * @post a project with the properties from a given form will be added to the controller.
     */
    public void addProject(HashMap<String, String> form) throws IllegalArgumentException {
        Project project = new Project(form);
        addProject(project.getName(), project);
    }

    /**
     * Returns the details of the given project.
     * @param name the project of which the details should be returned
     * @return the details of the given project
     * @throws IllegalArgumentException if the project does not exist.
     */
    public HashMap<String, String> getProjectDetails(String name) throws IllegalArgumentException {
        return getProject(name).getProjectDetails();
    }

    /**
     * Returns a list of ids of task that belong to a given project.
     * @param projectName the name of the project
     * @return a list of Integers
     * @throws IllegalArgumentException if the project does not exist.
     */
    public List<Integer> getTasksOfProject(String projectName) throws IllegalArgumentException {
        return getProject(projectName).getTaskIds();
    }

    /**
     * Returns the details of the given task
     * @param projectName the name of the project of the task
     * @param taskId      the id of the task of which the details should be returned.
     * @return the details of the given task
     * @throws IllegalArgumentException if the project does not exist.
     */
    public HashMap<String, String> getTaskDetails(String projectName, Integer taskId) throws IllegalArgumentException {
        return getProject(projectName).getTaskDetails(taskId);
    }

    /**
     * Returns a list of details of the available tasks of the given project
     * @param projectName the name of the project of the available tasks
     * @return the details of the available tasks of the given project
     * @throws IllegalArgumentException if the project does not exist
     */
    public ArrayList<HashMap<String, String>> getAvailableTaskDetails(String projectName) throws IllegalArgumentException {
        return getProject(projectName).getAvailableTaskDetails();
    }

    /**
     * This method generates a form containing all parameters needed to create a new taks.
     * All values are empty and can be filled in, and then passed back to create a task.
     * @return A map containing all elements that need to be filled in to create a new task
     */
    public HashMap<String,String> getTaskCreationForm() {
        return Task.getCreationForm();
    }

    /**
     * Adds a project with the properties from a given form.
     * @param projectName the project name
     * @param form        the creation form for the project
     * @throws IllegalArgumentException if the project does not exist.
     * @post a project with the properties from a given form will be added to the controller.
     */
    public void addTask(String projectName, HashMap<String, String> form) throws IllegalArgumentException {
        getProject(projectName).addTask(new Task(form));
    }

    /**
     * Return the ID of the latest task.
     * @return the ID of the latest task
     */
    public Integer getLastTaskID() {
        return Task.getLastTaskID();
    }

    /**
     * Sets the alternative of the given task to the given alternative task
     * @param projectName       the name of the project which holds both tasks
     * @param taskId            the id of the task
     * @param alternativeTaskId the id of the alternative task
     * @throws IllegalArgumentException if the project does not exist.
     * @post the alternative task of the task is set to the given task
     */
    public void addAlternativeToTask(String projectName, Integer taskId, Integer alternativeTaskId) throws IllegalArgumentException {
        Project project = getProject(projectName);
        project.getTask(taskId).setAlternative(project.getTask(alternativeTaskId));
    }

    /**
     * Adds the given dependency to the given task
     * @param projectName      the name of the project which holds both tasks
     * @param taskId           the id of the task
     * @param dependencyTaskId the id of the dependency
     * @throws IllegalArgumentException if the project does not exist.
     * @throws AccessDeniedException    if the task is already finished or failed.
     * @post the dependency is added to the task.
     */
    public void addDependencyToTask(String projectName, Integer taskId, Integer dependencyTaskId) throws IllegalArgumentException, AccessDeniedException {
        Project project = getProject(projectName);
        project.getTask(taskId).addDependency(project.getTask(dependencyTaskId));
    }


    /**
     * This method generates a form containing all parameters needed to update a task status
     * All values are empty and can be filled in, and then passed back to update a task status.
     * @return A map containing all elements that need to be filled in to update a task status
     */
    public HashMap<String, String> getUpdateTaskStatusForm() {
        return Task.getUpdateStatusForm();
    }

    /**
     * Updates the status of the given task.
     * @param projectName the name of the project of the task
     * @param taskId      the id of the task to update
     * @param form        the HashMap containing the new values necessary to update the task status
     * @throws IllegalArgumentException if the project does not exist.
     * @throws AccessDeniedException    if the active user type cannot edit tasks
     * @post the start time, end time and status of the task will be updated
     */
    public void updateTaskStatus(String projectName, Integer taskId, HashMap<String, String> form) throws IllegalArgumentException, AccessDeniedException {
        if (User.canChangeTaskStatus()) {
            getProject(projectName).getTask(taskId).updateStatus(form);
        }
        else {
            throw new AccessDeniedException("The active user type cannot edit tasks!");
        }
    }

    /**
     * Return the delay between the end time and the estimated end time in minutes.
     * @param projectName the project name
     * @param taskId      the task id
     * @return a String with the time between the end time and the estimated end time in minutes.
     * @throws IllegalStateException if the task is not yet finished.
     */
    public String getDelay(String projectName, Integer taskId) throws IllegalStateException {
        return this.getProject(projectName).getTask(taskId).getDelay();
    }

    /**
     * Return the time of the system.
     * @return the time of the system.
     */
    public String getSystemTime() {
        return clock.getSystemTimeString();
    }

    /**
     * Updates the time of the system.
     * @param newTime the new time of the system.
     * @throws DateTimeParseException if the text cannot be parsed.
     * @throws IllegalArgumentException if the new time if before the old time.
     * @post the time of the system will be set to the given time
     */
    public void updateSystemTime(String newTime) throws DateTimeParseException, IllegalArgumentException {
        clock.updateSystemTime(newTime);
    }

    /**
     * Returns the name of the active user type.
     * @return a string.
     */
    public String getUserType() {
        return User.getUserType();
    }

    /**
     * Changes the active user type to the given type.
     * @param user the name of the user type to change to.
     * @throws IllegalArgumentException if the user type does not exist.
     * @post the user type will be set to the given user type.
     */
    public void setUserType(String user) throws IllegalArgumentException{
        User.setUserType(user);
    }

    /**
     * Save the status of the system to a file.
     * @param path a String with a location in the file system.
     * @throws ImportExportException if the system can't be saved to the file.
     */
    public void exportSystem(String path) throws ImportExportException {
        ImportExportHandler exporter = new ImportExportHandler();
        exporter.addClock(this.clock);
        exporter.addUser(this.user);
        exporter.addProjects(this.projects.values());
        exporter.exportToPath(path);
    }

    /**
     * Restore the status of a system from a file.
     * @param path a String with a location in the file system.
     * @return a new Controller with the restored system.
     * @throws ImportExportException if the system can't be restored from the file.
     */
    public static Controller importSystem(String path) throws ImportExportException {
        ImportExportHandler importer = new ImportExportHandler();
        importer.importFromPath(path);
        return new Controller(importer.getClock(), importer.getUser(), importer.getProjects());
    }

}
