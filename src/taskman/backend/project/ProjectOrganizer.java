package taskman.backend.project;

import taskman.backend.user.User;
import taskman.backend.wrappers.ProjectWrapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is responsible for creating, storing and retrieving projects of the system.
 */
public class ProjectOrganizer {

    /**
     * Represents the projects in the system.
     */
    private Collection<Project> projects;

    /**
     * Construct an empty ProjectOrganizer.
     */
    public ProjectOrganizer() {
        this.projects = new ArrayList<>();
    }

    /**
     * Returns all projects.
     * @return a List of ProjectWrappers.
     */
    public List<ProjectWrapper> getProjects() {
        return new ArrayList<>(this.projects);
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
     * @post a project with the given properties will be added to the ProjectOrganizer.
     */
    public void createProject(String name, String description, LocalDateTime creationTime, LocalDateTime dueTime, User user) throws IllegalArgumentException {
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
     * Accepts a visitor.
     * @param v the visitor to accept.
     */
}
