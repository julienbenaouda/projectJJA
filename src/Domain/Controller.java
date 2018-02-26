package Domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class serves as a controller between the interface and the backend.
 * @author Alexander Braekevelt
 *
 */
public class Controller {



    public ArrayList<Project> getProjects() {
        return null;
    }

    public String getProjectDetails(Project project) {
        return null;
    }

    public void addNewProject(String name, String description, Date creatorTime, Date dueTime) {

    }


    public String getTaskDetails(Task task) {
        return null;
    }

    public void addNewTask(String description, Duration estimatedDuration, Double acceptabledeviation, Date startime, Task alternative) {

    }

    public void updateTaskStatus(Task task, Date startTime, Date endTime, Status status) {

    }


    public Date getSystemTime() {
        return null;
    }

    public void updateSystemTime(Date timestamp) {

    }

}
