package taskman.backend.resource;

import taskman.backend.task.Task;
import taskman.backend.time.TimeSpan;
import taskman.backend.wrappers.ResourceWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a resource item.
 *
 * @author Jeroen Van Der Donckt, Julien Benaouda
 */
public class Resource implements ResourceWrapper {

    /**
     * Creates a new resource item with the given resource type.
     *
     * @param type the resource type of the resource item
     * @post a new resource item is created with given resource type
     */
    public Resource(String name, ResourceType type){
    	this.name = name;
        setType(type);
        reservations = new ArrayList<>();
    }

    private final String name;

	/**
	 * Returns the name of the resource.
	 * @return the name of the resource.
	 */
	@Override
	public String getName() {
    	return this.name;
    }

    /**
     * Represents the resource type of the resource item.
     */
    private ResourceType type;

    /**
	 * Returns the type of the resource.
	 *
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
	public void setType(ResourceType type) {
		if(type == null) {
			throw new IllegalArgumentException("A resource must have a type!");
		}
	    this.type = type;
    }


	/**
	 * represents the list of reservations
	 */
	private ArrayList<Reservation> reservations;

	/**
	 * Returns the list of reservations for this resource
	 *
	 * @return the list of reservations for this resource
	 */
	public List<Reservation> getReservations() {
		return (List<Reservation>) reservations.clone();
	}

	/**
	 * Adds a reservation to the list of reservations
	 *
	 * @param r the reservation to add
	 * @post the given reservation is added to this resource
	 */
	public void addReservation(Reservation r)
	{
		reservations.add(r);
	}
	
	/**
	 * Creates a new reservation for this resource.
	 *
	 * @param task the task to create a reservation for
	 * @param timeSpan the time span of the reservation
	 * @post a new reservation is created and added to this resource
	 */
	public void createReservation(Task task, TimeSpan timeSpan) {
		Reservation r = new Reservation(task, this, timeSpan);
		addReservation(r);
	}

	/**
	 * Checks if a resource is available at the given time span.
	 *
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
	 * Returns if the resource can be safely removed.
	 * @return if the resource can be safely removed.
	 */
	public boolean canRemove() {
		return this.reservations.isEmpty();
	}
}
