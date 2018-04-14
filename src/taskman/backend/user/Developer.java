package taskman.backend.user;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import taskman.backend.resource.DeveloperResource;
import taskman.backend.resource.Reservation;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;

/**
 * This class represents a developer in the taskman system.
 * @author Julien Benaouda
 *
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
	 * @return the Resource of the developer
	 */
	public DeveloperResource getResource()
	{
		return resource;
	}
	
	/**
	 * changes the Resource of the developer to thee given Resource
	 * @param Resource the Resource to change the developer to
	 * @throws IllegalArgumentException when the Resource is not "developer"
	 */
	public void changeResource(DeveloperResource resource) {
		setResource(resource);
	}
	
	/**
	 * sets the Resource of the developer to the given Resource
	 * @param Resource the Resource to set the developer to
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
