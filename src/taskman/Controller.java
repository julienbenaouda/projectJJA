package taskman;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class serves as a controller between the interface and the backend.
 * @author Alexander Braekevelt
 *
 */
public class Controller {

    /**
     * Represents the projects of the controller.
     */
    private ArrayList<Project> projects = new ArrayList<>();

    /**
     * Returns the projects of the controller.
     * @return the projects of the controller
     */
    public ArrayList<Project> getProjects() {
        return (ArrayList<Project>) this.projects.clone();
    }

    /**
     * Returns the details of the given project.
     * @param name the project of which the details should be returned
     * @return the details of the given project
     */
    public HashMap<String, String> getProjectDetails(String name) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Adds a new project to the controller.
     * @param name the name of the project
     * @param description the description of the project
     * @param creationTime the creation time of the project
     * @param dueTime the due time of the project
     * @post the controller will contain a new project
     */
    public void addNewProject(String name, String description, String creationTime, String dueTime) {
        this.projects.add(new Project(name, description, creationTime, dueTime));
    }

    /**
     * Returns the details of the given task
     * @param project the project of the task
     * @param task the task of which the details should be returned.
     * @return the details of the given task
     */
    public String getTaskDetails(Project project, Task task) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Adds a new task to the given project.
     * @param project the project to which the task will be added
     * @param description the description of the new task
     * @param estimatedDuration the estimated duration of the new task
     * @param acceptableDeviation the acceptable deviation of the new task
     * @param startime the start time of the new task
     * @param alternative the alternative task for the new task
     * @post the project will contain a new task with the given parameters
     */
    public void addNewTask(Project project, String description, Duration estimatedDuration, Double acceptableDeviation, LocalDateTime startime, Task alternative) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Updates the status of the given task.
     * @param project the project of the task
     * @param task the task to update
     * @param startTime the new start time of the task
     * @param endTime the new end time of the task
     * @param status the new status of the task
     * @post the start time, end time and status of the task will be updated
     */
    public void updateTaskStatus(Project project, Task task, LocalDateTime startTime, LocalDateTime endTime, Status status) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Return the time of the system
     * @return the time of the system
     */
    public LocalDateTime getSystemTime() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    /**
     * Updates the time of the system
     * @param timestamp the new time of the system
     * @post the time of the system will be set to the given time
     */
    public void updateSystemTime(LocalDateTime timestamp) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public void setUser(String regularuser) {
        throw new NotImplementedException();
    }
}
