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
public class ResourceItem implements Resource {

    /**
     * Creates a new resource item with the given resource type.
     *
     * @param type the resource type of the resource item
     * @post a new resource item is created with given resource type
     */
    public ResourceItem(ResourceType type){
        setType(type);
        reservations = new ArrayList<>();
    }
    /**
     * Represents the resource type of the resource item.
     */
    private ResourceType type;
	
    @Override
    public ResourceType getType() {
        return type;
    }

    /**
     * Sets the resource type of the resource item to the given type.
     *
     * @param type the resource type of the resource type
     * @post the resource type of the resource item is set to the given type
     */
	private void setType(ResourceType type) {
	    this.type = type;
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
	 * returns all reservations for this resource
	 */
	private ArrayList<Reservation> getReservations() {
		return reservations;
	}
	
	/**
	 * represents the list of reservations
	 */
	private ArrayList<Reservation> reservations;
	

	/* (non-Javadoc)
	 * @see taskman.backend.resource.Resource#isAvailable(taskman.backend.time.TimeSpan)
	 */
	@Override
	public boolean isAvailable(TimeSpan timeSpan) {
		for(Reservation r: Reservations) {
			if(r.overlaps(timeSpan)) {
				return false; 
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see taskman.backend.resource.Resource#getReservations()
	 */
	@Override
	public List<Reservation> getReservations() {
		return reservations.clone();
	}

	@Override
	public void createReservation(Task task, TimeSpan timeSpan) {
		Reservation r = new Reservation(task, this, timeSpan);
		addReservation(r);
	}
}
