package taskman;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class serves as a controller between the interface and the backend.
 * @author Alexander Braekevelt
 */
public class Controller {

    /**
     * Represents the clock with the system time.
     */
    private Clock clock;

    /**
     * Represents the projects in the system.
     */
    private HashMap<String, Project> projects = new HashMap<>();

    /**
     * Create a controller.
     */
    public Controller() {
        this.clock = new Clock();
    }

    /**
     * Create a controller.
     * @param initialTime the initial system time;
     */
    public Controller(String initialTime) {
        this.clock = new Clock();
        this.clock.updateSystemTime(initialTime);
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
    private Project getProject(String name) throws IllegalArgumentException{
        if (projects.containsKey(name)) {
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
     * @throws IllegalArgumentException if a project with the given name already exist
     */
    private void addProject(String name, Project project) throws IllegalArgumentException{
        if (projects.containsKey(name)) {
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
     * @post a project with the properties from a given form will be added to the controller.
     * @throws IllegalArgumentException if the form doesn't contain a key 'name'.
     */
    public void addProject(HashMap<String, String> form) throws IllegalArgumentException{
        if (form.containsValue("name")){
            String name = form.get("name");
            Project project = new Project(form);
            addProject(name, project);
        }
        else {
            throw new IllegalArgumentException("The form must contain a name for the project!");
        }
    }

    /**
     * Returns the details of the given project.
     * @param name the project of which the details should be returned
     * @return the details of the given project
     * @throws IllegalArgumentException if the project does not exist.
     */
    public HashMap<String, String> getProjectDetails(String name) throws IllegalArgumentException{
        return getProject(name).getProjectDetails();
    }

    /**
     * Returns a list of ids of task that belong to a given project.
     * @param projectName the name of the project
     * @return a list of Integers
     * @throws IllegalArgumentException if the project does not exist.
     */
    public List<Integer> getTasksOfProject(String projectName) throws IllegalArgumentException{
        return getProject(projectName).getTaskIds();
    }

    /**
     * Returns the details of the given task
     * @param projectName the name of the project of the task
     * @param taskId the id of the task of which the details should be returned.
     * @return the details of the given task
     * @throws IllegalArgumentException if the project does not exist.
     */
    public HashMap<String, String> getTaskDetails(String projectName, Integer taskId) throws IllegalArgumentException{
        return getProject(projectName).getTaskDetails(taskId);
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
     * @param form the creation form for the project
     * @post a project with the properties from a given form will be added to the controller.
     * @throws IllegalArgumentException if the project does not exist.
     */
    public void addTask(String projectName, HashMap<String, String> form) throws IllegalArgumentException{
        getProject(projectName).addTask(new Task(form));
    }

    /**
     * Sets the alternative of the given task to the given alternative task
     * @param projectName the name of the project which holds both tasks
     * @param taskId the id of the task
     * @param alternativeTaskId the id of the alternative task
     * @throws IllegalArgumentException if the project does not exist.
     */
    public void addAlternativeToTask(String projectName, Integer taskId, Integer alternativeTaskId) throws IllegalArgumentException {
        Project project = getProject(projectName);
        project.getTask(taskId).setAlternative(project.getTask(alternativeTaskId));
    }

    /**
     * Adds the given dependency to the given task
     * @param projectName the name of the project which holds both tasks
     * @param taskId the id of the task
     * @param dependencyTaskId the id of the dependency
     * @throws IllegalArgumentException if the project does not exist.
     */
    public void addDependencyToTask(String projectName, Integer taskId, Integer dependencyTaskId) throws IllegalArgumentException {
        Project project = getProject(projectName);
        project.getTask(taskId).addDependency(project.getTask(dependencyTaskId));
    }


    /**
     * Updates the status of the given task.
     * @param projectName the name of the project of the task
     * @param taskId the id of the task to update
     * @param startTime the new start time of the task
     * @param endTime the new end time of the task
     * @param status the new status of the task
     * @post the start time, end time and status of the task will be updated
     * @throws IllegalArgumentException if the project does not exist.
     */
    public void updateTaskStatus(String projectName, Integer taskId, String startTime, String endTime, String status) throws IllegalArgumentException{
        getProject(projectName).getTask(taskId).updateStatus(startTime, endTime, status);
    }

    /**
     * Return the time of the system
     * @return the time of the system
     */
    public String getSystemTime() {
        return clock.getSystemTimeString();
    }

    /**
     * Updates the time of the system
     * @param newTime the new time of the system
     * @post the time of the system will be set to the given time
     */
    public void updateSystemTime(String newTime) {
        clock.updateSystemTime(newTime);
    }

    /**
     * Changes the active user to the given user.
     * @param user the name of the user to change to.
     * @throws IllegalArgumentException if the user type does not exist.
     */
    public void setUser(String user) throws IllegalArgumentException{
        User.setUserType(user);
    }

    public void importXML(String path) {
        throw new NotImplementedException();
    }

    public void exportToXML(String path) {
        throw new NotImplementedException();

    }

}
