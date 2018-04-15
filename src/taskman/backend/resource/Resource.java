package taskman.backend.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;

/**
 * Class representing a resource item.
 *
 * @author Jeroen Van Der Donckt, Julien Benaouda
 */
public class Resource {

    /**
     * Creates a new resource item with the given resource type.
     *
     * @param type the resource type of the resource item
     * @post a new resource item is created with given resource type
     */
    public Resource(ResourceType type){
        setType(type);
        reservations = new ArrayList<>();
    }
    /**
     * Represents the resource type of the resource item.
     */
    private ResourceType type;
	
    /**
     * @return the type of the resource
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Sets the resource type of the resource item to the given type.
     *
     * @param type the resource type of the resource type
     * @post the resource type of the resource item is set to the given type
     * @throws IllegalArgumentException when the type is null
     */
	private void setType(ResourceType type) {
		if(type == null) {
			throw new IllegalArgumentException("A resource must have a type!");
		}
	    this.type = type;
	    type.addResource(this);
    }
	
	/**
	 * adds a reservation to the list of reservations
	 * @param r the reservation to add
	 */
	public void addReservation(Reservation r)
	{
		reservations.add(r);
	}
	
	
	/**
	 * represents the list of reservations
	 */
	private ArrayList<Reservation> reservations;
	

	/**
	 * checks if a resource is available at the given timespan
	 * @param timeSpan the time span to check with
	 * @return true if there is no overlapping reservation for the time span
	 */
	public boolean isAvailable(TimeSpan timeSpan) {
		for (Reservation r: getReservations()) {
			if (r.overlaps(timeSpan)) {
				return false; 
			}
		}
		return true;
	}

	/**
	 * @return the list of reservations for this resource
	 */
	public List<Reservation> getReservations() {
		return (List<Reservation>) reservations.clone();
	}
	
	/**
	 * creates a new reservation for this resource
	 * @param task the task to create a reservation for
	 * @param timeSpan the time span of the reservation
	 */
	public void createReservation(Task task, TimeSpan timeSpan) {
		Reservation r = new Reservation(task, this, timeSpan);
		addReservation(r);
	}
}
