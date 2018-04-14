/**
 * 
 */
package taskman.backend.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import taskman.backend.task.Task;
import taskman.backend.time.TimeSpan;

/**
 * This interface represents a resource
 * @author Julien Benaouda
 *
 */
public interface Resource {

	/**
	 * Returns the type of the resource.
	 *
	 * @return the type of the resource
	 */
	ResourceType getType();
	
	/**
	 * checks if the resource is available during the specified timespan
	 * @param timeSpan the time span to check with
	 * @return true if the resource is available, else false
	 */
	boolean isAvailable(TimeSpan timeSpan);
	
	/**
	 * returns a list of all reservations for the resource
	 */
	List<Reservation> getReservations();
	
	/**
	 * creates a new reservation for this resource
	 * @param task the task to link the reservation to
	 * @param timeSpan the time span of the reservation
	 */
	void createReservation(Task task, TimeSpan timeSpan);
}
