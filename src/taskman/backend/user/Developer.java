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
	
}
