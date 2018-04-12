package taskman.backend.project;

import taskman.backend.user.User;
import taskman.backend.visitor.Entity;
import taskman.backend.visitor.Visitor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is responsible for creating, storing and retrieving projects of the system.
 */
public class ProjectOrganizer implements Entity {

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
     * Returns all project names.
     * @return a List of Strings.
     */
    public List<String> getProjectNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Project project: this.projects) {
            names.add(project.getName());
        }
        return names;
    }

    /**
     * Returns if a project with the given name exists.
     * @param name a String with a possible name of a project.
     * @return a Boolean.
     */
    public Boolean projectExists(String name) {
        for (Project project: this.projects) {
            if (project.getName().equals(name)){
                return true;
            }
        }
        return false;
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
     * @throws IllegalArgumentException when one of the given parameters is not of a valid format. TODO: is dit nodig?
     * @post a project with the given properties will be added to the ProjectOrganizer.
     */
    public void createProject(String name, String description, LocalDateTime creationTime, LocalDateTime dueTime, User user) throws IllegalArgumentException {
        this.projects.add(new Project(name, description, creationTime, dueTime, user));
    }

    /**
     * Accepts a visitor.
     * @param v the visitor to accept.
     */
    @Override
    public void accept(Visitor v) {
        v.visitProjectOrganizer(this);
    }
}