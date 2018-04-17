package taskman.backend.user;

import taskman.backend.resource.DeveloperResource;

/**
 * This class represents a developer in the taskman system.
 * @author Julien Benaouda
 */
public class Developer extends User {

	/**
	 * creates a new developer with given name and password
	 * @param name the name of the developer
	 * @param password the password of the developer
	 * @post a new developer is created with given name and password
	 */
	public Developer(String name, String password) {
		super(name, password);
	}

	/**
	 * Returns the type of the user.
	 * @return a String.
	 */
	@Override
	public String getUserType() {
		return "developer";
	}

	/**
	 * @return the Resource of the developer
	 */
	public DeveloperResource getResource()
	{
		return resource;
	}
	
	/**
	 * changes the Resource of the developer to thee given Resource
	 * @param resource the Resource to change the developer to
	 * @throws IllegalArgumentException when the Resource is not "developer" // TODO: gebeurt nooit? + check null?
	 */
	public void changeResource(DeveloperResource resource) {
		setResource(resource);
	}
	
	/**
	 * sets the Resource of the developer to the given Resource
	 * @param resource the Resource to set the developer to
	 */
	private void setResource(DeveloperResource resource)
	{
		this.resource = resource;
	}
	
	/**
	 * represents the Resource of the developer
	 */
	private DeveloperResource resource;

}
