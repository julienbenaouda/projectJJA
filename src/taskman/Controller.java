package taskman;

import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
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
    private HashMap<String, Project> projects;

    /**
     * Create a controller (with minimum time and regular user type).
     */
    public Controller() {
        this.clock = new Clock();
        this.projects = new HashMap<>();
    }

    /**
     * Create a controller (with given time and regular user type).
     * @param initialTime the initial system time.
     * @post the time of the system will be set to the given time
     * @throws IllegalArgumentException if the new time if before the old time.
     */
    public Controller(String initialTime) throws IllegalArgumentException {
        this();
        this.updateSystemTime(initialTime);
    }

    /**
     * Create a controller (with given time and given user type).
     * @param initialTime the initial system time.
     * @param initialUserType the user that will be active.
     * @post the time of the system will be set to the given time
     * @post the user type will be set to the given user type.
     * @throws IllegalArgumentException if the new time if before the old time.
     * @throws IllegalArgumentException if the user type does not exist.
     */
    public Controller(String initialTime, String initialUserType) throws IllegalArgumentException {
        this();
        this.updateSystemTime(initialTime);
        this.setUserType(initialUserType);
    }

    /**
     * Create a controller with its private variables. (Used to restore from XML.)
     * @param projects a HashMap with Strings and Projects
     * @param clock a Clock
     */
    private Controller(HashMap<String, Project> projects, Clock clock) {
        this.projects = projects;
        this.clock = clock;
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
     * @post a project with the properties from a given form will be added to the controller.
     * @throws IllegalArgumentException when one of the parameters is abscent or not valid.
     * @throws IllegalArgumentException if a project with the given name already exist
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
     * @param taskId the id of the task of which the details should be returned.
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
     * @param form the creation form for the project
     * @post a project with the properties from a given form will be added to the controller.
     * @throws IllegalArgumentException if the project does not exist.
     */
    public void addTask(String projectName, HashMap<String, String> form) throws IllegalArgumentException {
        getProject(projectName).addTask(new Task(form));
    }

    /**
     * Return the ID of the latest task.
     * @return the ID of the latest task
     */
    public static Integer getLastTaskID() {
        return Task.getLastTaskID();
    }

    /**
     * Sets the alternative of the given task to the given alternative task
     * @param projectName the name of the project which holds both tasks
     * @param taskId the id of the task
     * @param alternativeTaskId the id of the alternative task
     * @post the alternative task of the task is set to the given task
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
     * @post the dependency is added to the task.
     * @throws IllegalArgumentException if the project does not exist.
     * @throws AccessDeniedException if the task is already finished or failed.
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
     * @param taskId the id of the task to update
     * @param form the HashMap containing the new values necessary to update the task status
     * @post the start time, end time and status of the task will be updated
     * @throws IllegalArgumentException if the project does not exist.
     * @throws AccessDeniedException if the active user type cannot edit tasks
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
     * @throws DateTimeParseException if the text cannot be parsed
     * @throws IllegalArgumentException if the new time if before the old time.
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
     * @post the user type will be set to the given user type.
     * @throws IllegalArgumentException if the user type does not exist.
     */
    public void setUserType(String user) throws IllegalArgumentException{
        User.setUserType(user);
    }

    /**
     * Save a controller to an XML file.
     * @param path the path of the XML file.
     * @throws XmlException if the object can't be written to the file.
     */
    public void exportToXML(String path) throws XmlException {
        XmlObject object = new XmlObject();
        this.clock.addToXml(object.addXmlObject("clock"));
        User.addToXml(object.addXmlObject("user"));
        for (Project project: this.projects.values()) {
            project.addToXml(object.addXmlObject("project"));
        }
        object.exportTo(path);
    }

    /**
     * Restore a controller from an XML file.
     * @param path the path of the XML file.
     * @return the restored controller.
     * @throws XmlException if the controller can't be created.
     */
    public static Controller importFromXML(String path) throws XmlException {
        XmlObject object = XmlObject.importFrom(path);
        Clock clock = Clock.getFromXml(object.getXmlObjects("clock").get(0));
        User.setFromXml(object.getXmlObjects("user").get(0));
        HashMap<String, Project> projects = new HashMap<>();
        for (XmlObject projectObject: object.getXmlObjects("project")) {
            Project project = Project.getFromXml(projectObject);
            projects.put(project.getName(), project);
        }
        return new Controller(projects, clock);
    }

}
