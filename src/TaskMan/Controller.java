package TaskMan;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class Controller {



    //////////////////// Getters ////////////////////

    public ArrayList<Project> getProjects() {
        return null;
    }

    public String getProjectDetails(Project project) {
        return null;
    }

    public String getTaskDetails(Task task) {
        return null;
    }

    public Date getSystemTime() {
        return null;
    }


    //////////////////// Setters ////////////////////

    public void addNewProject(String name, String description, Date creatorTime, Date dueTime) {

    }

    public void addNewTask(String description, Duration estimatedDuration, Double acceptabledeviation, Date startime, Task alternative) {

    }

    public void updateTaskStatus(Task task, Date startTime, Date endTime, Status status) {

    }

    public void updateSystemTime(Date timestamp) {

    }

}
