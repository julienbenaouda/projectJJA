/**
 * 
 */
package taskman.backend.resource;

import java.time.LocalDateTime;

/**
 * This interface represents a resource
 * @author Julien Benaouda
 *
 */
public interface Resource {
	/**
	 * Checks if a resource is available during the given period
	 * @param startTime the start time of the period
	 * @param endTime the end time of the period
	 * @return true if the resource is available, else false.
	 */
	boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime);
	
	/**
	 * Checks if the resource is available at the given time
	 * @param time the time to check with
	 * @return true if thee resource is available, else false
	 */
	boolean isAvailable(LocalDateTime time);
}
