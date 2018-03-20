package taskman;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * The class is responsible for exporting and restoring objects from the backend to a textual representation.
 * @author Alexander Braekevelt
 */
public class ImportExportHandler {

    /**
     * Represents the XmlObject which holds all the added objects.
     */
    private XmlObject xml;

    /**
     * Create an empty ImportExportHandler.
     * @throws ImportExportException if the ImportExportHandler cannot be created.
     */
    public ImportExportHandler() throws ImportExportException {
        this.xml = new XmlObject();
    }

    /**
     * Export the added objects to the given path.
     * @param path a String with a location in the file system.
     * @throws ImportExportException if the export fails.
     */
    public void exportToPath(String path) throws ImportExportException {
        this.xml.exportTo(path);
    }

    /**
     * Import objects from the given path.
     * @param path a String with a location in the file system.
     * @post all previously added objects will be removed and the imported objects will be added.
     * @throws ImportExportException if the import fails.
     */
    public void importFromPath(String path) throws ImportExportException {
        this.xml = XmlObject.importFrom(path);
    }

    /**
     * Add a clock to an XmlObject.
     * @param clock a Clock
     * @post the Clock will be added.
     * @throws ImportExportException if the Clock cannot be added.
     */
    public void addClock(Clock clock) throws ImportExportException {
        this.xml.createXmlObject("clock").addAttribute("time", clock.getSystemTimeString());
    }

    /**
     * Restore a Clock.
     * @return the restored Clock.
     * @throws ImportExportException if the Clock can't be created.
     */
    public Clock getClock() throws ImportExportException {
        Clock clock =  new Clock();
        String time = this.xml.getXmlObject("clock").getAttribute("time");
        if (!time.equals(clock.getSystemTimeString())) {
            clock.updateSystemTime(time);
        }
        return clock;
    }

    /**
     * Add a User.
     * @post the User will be added.
     * @throws ImportExportException if the User cannot be added.
     */
    public void addUser(User user) throws ImportExportException {
        this.xml.createXmlObject("user").addAttribute("userType", user.getUserType());
    }

    /**
     * Restore a User.
     * @return the restored User.
     * @throws ImportExportException if the User can't be created.
     */
    public User getUser() throws ImportExportException {
        User user = new User();
        user.setUserType(this.xml.getXmlObject("user").getAttribute("userType"));
        return user;
    }

    /**
     * Add Projects.
     * @param projects a Collection of Projects.
     * @post the Projects will be added.
     * @throws ImportExportException if a Project cannot be added.
     */
    public void addProjects(Collection<Project> projects) throws ImportExportException {
        for (Project project: projects) {
            XmlObject project_xml = this.xml.createXmlObject("project");
            project_xml.addAttribute("name", project.getName());
            project_xml.addAttribute("description", project.getDescription());
            project_xml.addAttribute("creationTime", project.getCreationTime());
            project_xml.addAttribute("dueTime", project.getDueTime());
            this.addTasks(project.getTasks(), project_xml);
        }
    }

    /**
     * Restore Projects.
     * @return a Collection of restored Projects.
     * @throws ImportExportException if a Project can't be created.
     */
    public Collection<Project> getProjects() throws ImportExportException {
        ArrayList<Project> projects = new ArrayList<>();
        for (XmlObject project_xml: this.xml.getXmlObjects("project")) {
            String name = project_xml.getAttribute("name");
            String description = project_xml.getAttribute("description");
            String creationTime = project_xml.getAttribute("creationTime");
            String dueTime = project_xml.getAttribute("dueTime");
            List<Task> tasks = this.getTasks(project_xml);
            projects.add(new Project(name, description, creationTime, dueTime, tasks));
        }
        return projects;
    }

    /**
     * Add Tasks from a Project.
     * @param tasks a Collection of Tasks.
     * @param project_xml the XmlObject of the Project.
     * @post the Tasks will be added.
     * @throws ImportExportException if a Task cannot be added.
     */
    private void addTasks(Collection<Task> tasks, XmlObject project_xml) throws ImportExportException {
        for (Task task: tasks) {
            XmlObject task_xml = project_xml.createXmlObject("task");
            task_xml.addAttribute("lastTaskID", task.getLastTaskID());
            task_xml.addAttribute("id", task.getID());
            task_xml.addAttribute("description", task.getDescription());
            task_xml.addAttribute("estimatedDuration", task.getEstimatedDuration());
            task_xml.addAttribute("acceptableDeviation", task.getAcceptableDeviation());
            task_xml.addAttribute("startTime", task.getStartTime());
            task_xml.addAttribute("endTime", task.getEndTime());
            task_xml.addAttribute("status", task.getStatus());
            if (task.getAlternative() == null) {
                task_xml.addAttribute("alternative", "null");
            } else {
                task_xml.addAttribute("alternative", task.getAlternative());
            }
            for (Task dependency : task.getDependencies()) {
                task_xml.addText("dependency", dependency);
            }
        }
    }

    /**
     * Restore Tasks from a Project.
     * @param project_xml the XmlObject of the Project.
     * @return a Collection of restored Tasks.
     * @throws ImportExportException if a Task can't be created.
     */
    private Collection<Task> getTasks(XmlObject project_xml) throws ImportExportException {
        HashMap<String, Task> tasks = new HashMap<>();
        for (XmlObject task_xml: project_xml.getXmlObjects("task")) {
            String lastTaskID = task_xml.getAttribute("lastTaskID");
            String id = task_xml.getAttribute("id");
            String description = task_xml.getAttribute("description");
            String estimatedDuration = task_xml.getAttribute("estimatedDuration");
            String acceptableDeviation = task_xml.getAttribute("acceptableDeviation");
            String startTime = task_xml.getAttribute("startTime");
            String endTime = task_xml.getAttribute("endTime");
            String status = task_xml.getAttribute("status");
            tasks.put(id, new Task(lastTaskID, id, description, estimatedDuration, acceptableDeviation, startTime, endTime, status));
        }
        for (XmlObject task_xml: project_xml.getXmlObjects("task")) {
            Task task = tasks.get(task_xml.getAttribute("id"));
            String alternative_id = task_xml.getAttribute("alternative");
            if (!alternative_id.equals("null")) {
                task.setAlternative(tasks.get(alternative_id));
            }
            for (String dependency_id: task_xml.getTexts("dependency")) {
                task.addDependency(tasks.get(dependency_id));
            }
        }
        return tasks.values();
    }

}
