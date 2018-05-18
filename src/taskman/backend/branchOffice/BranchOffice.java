package taskman.backend.branchOffice;

import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.BranchOfficeWrapper;

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
	 * creates a new branch office manager with the given name
	 * @param name the name of the new branch office
	 */
	public BranchOffice(String name, ProjectManager projectManager, ResourceManager resourceManager, UserManager userManager) {
		setName(name);
		setProjectManager(projectManager);
		setResourceManager(resourceManager);
		setUserManager(userManager);
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

}
