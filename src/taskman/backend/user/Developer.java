package taskman.backend.user;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import taskman.backend.resource.Reservation;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceType;
import taskman.backend.time.AvailabilityPeriod;

/**
 * This class represents a developer in the taskman system.
 * @author Julien Benaouda
 *
 */
public class Developer extends User implements Resource {

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
	 * @return the breakTime
	 */
	public AvailabilityPeriod getBreakTime() {
		return breakTime;
	}

	/**
	 * @param breakTime the breakTime of the developer
	 */
	private void setBreakTime(AvailabilityPeriod breakTime) {
		this.breakTime = breakTime;
	}
	
	/**
	 * adds a new break time for this developer
	 * @param startTime the start time of his break
	 */
	public void addBreakTime(LocalTime startTime) {
		LocalTime startBreak = LocalTime.of(11, 0);
		LocalTime endBreak = LocalTime.of(13, 0);
		LocalTime endTime = startTime.plusHours(1);
		if(startTime.isBefore(startBreak) || endTime.isAfter(endBreak)) {
			throw new IllegalArgumentException("The break time must be tetween 11:00 end 13:00");
		}
		setBreakTime(new AvailabilityPeriod(startTime, endTime));
	}

	/**
	 * represents the break of the developer
	 */
	private AvailabilityPeriod breakTime;

	@Override
	public LocalDateTime firstAvailableTime(LocalDateTime time, long duration) {
		// TODO add implementation for this method
		return null;
	}

	@Override
	public ResourceType getType() {
		return type;
	}
	
	/**
	 * represents the resource type
	 */
	private ResourceType type;

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
}
