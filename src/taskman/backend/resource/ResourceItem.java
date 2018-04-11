package taskman.backend.resource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import taskman.backend.time.AvailabilityPeriod;

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
        availability = new HashMap<>();
    }

	@Override
	public LocalDateTime firstAvailableTime(LocalDateTime time, long duration) {
		LocalDateTime firstAvailableTime = time;
		LocalDateTime endTime = calculateEndTime(firstAvailableTime, duration);
		for(Reservation r: getReservations()) {
			if(r.getTimeSpan().getStartTime().isBefore(endTime) && r.getTimeSpan().getStartTime().isAfter(firstAvailableTime)) {
				firstAvailableTime = r.getTimeSpan().getEndTime();
				// TODO dit vereist dat de lijst gesorteerd is op tijd
				endTime = calculateEndTime(firstAvailableTime, duration);
			}
		}
		return firstAvailableTime;
	}
	
	private LocalDateTime calculateStartTime(LocalDateTime time) {
		int dayOfWeek = time.getDayOfWeek().getValue();
		AvailabilityPeriod p = getAvailability().get(dayOfWeek);
		if(p.getStartTime().atDate(time.toLocalDate()).isAfter(time)) {
			time = p.getStartTime().atDate(time.toLocalDate());
		}
		if(p.getEndTime().atDate(time.toLocalDate()).isBefore(time)) {
			for(int weekDay = 1; weekDay <= 7; weekDay++) {
				dayOfWeek = ((time.getDayOfWeek().getValue()+weekDay)%7)+1;
				p = getAvailability().get(dayOfWeek);
				if(p != null) {
					return p.getStartTime().atDate(time.toLocalDate()).plusDays(weekDay);
				}
			}
		}
		return time;
		// todo returns original start time if there is no available time in a week
	}
	
	private LocalDateTime calculateEndTime(LocalDateTime startTime, long duration) {
		return startTime.plusMinutes(duration);
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
	 * @param reservation the reservation to add
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
	
	/**
	 * return the availability of this resource
	 */
	private Map<Integer, AvailabilityPeriod> getAvailability() {
		return availability;
	}
	
	/**
	 * represents the availability for every week day
	 */
	private HashMap<Integer, AvailabilityPeriod> availability;
}
