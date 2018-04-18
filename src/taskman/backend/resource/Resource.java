package taskman.backend.resource;

import taskman.backend.time.TimeSpan;
import taskman.backend.wrappers.ResourceWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a resource item.
 *
 * @author Jeroen Van Der Donckt, Julien Benaouda, Alexander Braekevelt
 */
public class Resource implements ResourceWrapper {

    /**
     * Creates a new resource item with the given name and resource type.
     *
	 * @param name the name of the resource item
     * @param type the resource type of the resource item
     * @post a new resource item is created with given resource type and reservations is initialized to an empty ArrayList
     */
    public Resource(String name, ResourceType type){
    	this.name = name;
        setType(type);
        reservations = new ArrayList<>();
    }


	/**
	 * Represents the name of he resource.
	 */
	private final String name;

	/**
	 * Returns the name of the resource.
	 *
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
	 * Represents the list of reservations.
	 */
	private ArrayList<Reservation> reservations;

	/**
	 * Returns the list of reservations for this resource.
	 *
	 * @return the list of reservations for this resource
	 */
	public List<Reservation> getReservations() {
		return new ArrayList<>(reservations);
	}

	/**
	 * Adds a reservation to the list of reservations.
	 *
	 * @param r the reservation to add
	 * @post the given reservation is added to this resource
	 */
	public void addReservation(Reservation r)
	{
		reservations.add(r);
	}

	/**
	 * Deletes the given reservation from the resource.
	 *
	 * @param r the reservation to delete
	 * @post the reservation is deleted from the list of reservations
	 */
	public void deleteReservation(Reservation r){
		reservations.remove(r);
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
