package taskman.backend.project;

import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.user.OperationNotPermittedException;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents the project manager.
 * This class is responsible for creating, storing and retrieving projects of the system. Therefore it stores a list of all projects. While creating a project, it is not possible to create two projects with the same name.  
 */
public class ProjectManager {

    /**
     * Represents the projects in the system.
     */
    private Collection<Project> projects;

    /**
     * Construct an empty ProjectOrganizer.
     */
    public ProjectManager() {
        this.projects = new ArrayList<>();
    }

    /**
     * Returns all projects.
     * @return a list of Projects.
     */
    public List<Project> getProjects() {
        return new ArrayList<>(this.projects);
    }

	/**
	 * Returns all projects the user has access to.
	 * @param user the user.
	 * @return a list of projects.
	 */
	public List<Project> getProjects(User user) {
		return this.projects.stream().filter(p -> p.hasAccessTo(user)).collect(Collectors.toList());
	}

    /**
     * Returns the project with the given name.
     * @param name the name of the project as String.
     * @return a project.
     * @throws IllegalArgumentException if no project is found with the given name.
     */
    public Project getProject(String name) throws IllegalArgumentException {
        for (Project project: this.projects) {
            if (project.getName().equals(name)){
                return project;
            }
        }
        throw new IllegalArgumentException("A project with name '" + name + "' does not exist!");
    }

    /**
     * Add a project with the properties.
     * @param name the project name
     * @param description the project description
     * @param creationTime the creation time of the project. The creation time must be of the following format: dd/mm/yyyy hh:mm.
     * @param dueTime the due time of the project. The due time must be of the following format: dd/mm/yyyy hh:mm
     * @param user the current user.
     * @throws IllegalArgumentException when one of the given parameters is not of a valid format.
     * @throws OperationNotPermittedException when the user doesn't have access to create a project.
     * @post a project with the given properties will be added to the ProjectOrganizer.
     */
    public void createProject(String name, String description, LocalDateTime creationTime, LocalDateTime dueTime, User user) throws IllegalArgumentException, OperationNotPermittedException {
    	if(projectExists(name)) {
    		throw new IllegalArgumentException("A project with the given name already exists");
    	}
        this.projects.add(new Project(name, description, creationTime, dueTime, user));
    }
    
    /**
     * checks if a project with the given name exists
     * @param name the project to check for
     * @return true if a project with the given name exists, else false
     */
    private boolean projectExists(String name) {
    	for(Project p: projects) {
    		if(p.getName().equals(name)) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Created the delegated task with the given arguments.
     * @param requirements the requirements for the task to delegate
     * @param name the name of the task
     * @param description the description of the task
     * @param startTime the start time of the task
     * @param estimatedDuration the estimated duration of the task
     * @param acceptableDeviation the acceptable deviation of the task
     * @return the delegated task
     * @post the delegated task and its project are created with the given arguments
     */
    public Task createDelegatedTask(Map<ResourceType, Integer> requirements, String name, String description, LocalDateTime startTime, long estimatedDuration, double acceptableDeviation) {
    	String delegatedName = "delegated_" + name;
        Project p = new Project("project_delegated_" +name, description, startTime, startTime.plusMinutes(estimatedDuration));
    	projects.add(p);
    	p.createDelegatedTask(delegatedName, description, estimatedDuration, acceptableDeviation);
    	for (ResourceType rt : requirements.keySet()) {
            p.getTask(delegatedName).getPlan().addRequirement(rt, requirements.get(rt));
        }
    	return p.getTask(delegatedName);
    }

}
