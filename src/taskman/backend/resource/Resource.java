/**
 * 
 */
package taskman.backend.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This interface represents a resource
 * @author Julien Benaouda
 *
 */
public interface Resource {
	/**
	 * returns the first available time to start the task
	 * @param Time the time to start searching from
	 * @param resources the list of resources to search in
	 * @return The first available time to plan the task
	 */
	LocalDateTime firstAvailableTime(LocalDateTime time, ArrayList<Resource> resources);
	
	/**
	 * returns the type of the resource
	 * @return the type of the resource
	 */
	public ResourceType getType();
}
