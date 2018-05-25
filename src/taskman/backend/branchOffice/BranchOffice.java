package taskman.backend.branchOffice;

import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.BranchOfficeWrapper;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * This class represents a branch office.
 * A branch office contains a name, project userManager, resource userManager and user userManager. It is also responsible for accepting a task which is delegated from another branch office.
 * @author Julien Benaouda
 *
 */
public class BranchOffice implements BranchOfficeWrapper {

	/**
	 * creates a new branch office manager with the given name
	 * @param name the name of the new branch office
	 */
	public BranchOffice(String name) {
		setName(name);
		setProjectManager(new ProjectManager());
		setResourceManager(new ResourceManager());
		setUserManager(new UserManager());
	}
	
	/**
	 * represents the name of the branch office
	 */
	private String name;
	
	/**
	 * returns the name of the branch office
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the name of the branch office to the given name
	 * @param name the name of the branch office
	 * @throws IllegalArgumentException when the name is empty
	 */
	private void setName(String name) throws IllegalArgumentException {
		if(name == null || name.isEmpty()) {
			throw new IllegalArgumentException("the given name is invalid. Please try again!");
		}
		this.name = name;
	}

	/**
	 * represents the project userManager
	 */
	private ProjectManager projectManager;

	/**
	 * returns the projectManager of the branch office
	 */
	public ProjectManager getProjectManager() {
		return projectManager;
	}

	/**
	 * sets the project userManager of the branch office to the given project userManager
	 * @param projectManager the projectManager to set for the branch office
	 * @throws IllegalArgumentException when the given project manager is null
	 */
	private void setProjectManager(ProjectManager projectManager) throws IllegalArgumentException {
		if(projectManager == null) {
			throw new IllegalArgumentException("the project userManager can't be null.");
		}
		this.projectManager = projectManager;
	}
	
	/**
	 * represents the resource userManager
	 */
	private ResourceManager resourceManager;

	/**
	 * returns the resourceManager
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * sets the resource userManager to the given resource userManager
	 * @param resourceManager the resourceManager to set for this branch office
	 */
	private void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	/**
	 * represents the user userManager
	 */
	private UserManager userManager;

	/**
	 * returns the userManager of the branch office
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * sets the user userManager to the given user userManager
	 * @param userManager the userManager to set for this branch office
	 */
	private void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * Executes the delegation of the task to this branch office.
	 * @param requirements the requirements for the task to delegate
	 * @param name the name of the task
	 * @param description the description of the task
	 * @param startTime the start time of the task
	 * @param estimatedDuration the estimated duration of the task
	 * @param acceptableDeviation the acceptable deviation of the task
	 * @return the delegated task
	 * @throws IllegalArgumentException if the branchoffice has not enough resources available
	 * @post a new delegated task is created
	 */
	public Task executeDelegation(Map<ResourceType, Integer> requirements, String name, String description, LocalDateTime startTime, long estimatedDuration, double acceptableDeviation) throws IllegalArgumentException {
		if(getResourceManager().checkRequirements(requirements)) {
			return getProjectManager().createDelegatedTask(name, description, startTime, estimatedDuration, acceptableDeviation);
		} else {
			throw new IllegalArgumentException("This branch office had not enough resources available to accept this task!");
		}
	}

}
