/**
 * 
 */
package taskman.backend.resource;

import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.Developer;

import java.time.LocalTime;

/**
 * @author Julien Benaouda
 *
 */
public class DeveloperResource extends Resource {

	/**
	 * creates a new DeveloperResource with the given parameters
	 * @param type the type of the userResource
	 * @param startBreak the time when the break of the developer starts
	 */
	public DeveloperResource(String name, ResourceType type, LocalTime startBreak, Developer d) {
		super(name, type);
		addBreakTime(startBreak);
		setDeveloper(d);
	}


	/**
	 * Represents the break of the developer.
	 */
	private AvailabilityPeriod breakTime;

	/**
	 * Returns the break time of the developer.
	 *
	 * @return the break time of the developer
	 */
	public AvailabilityPeriod getBreakTime() {
		return breakTime;
	}

	/**
	 * Sets the break time of the developer to the given break time
	 *
	 * @param breakTime the breakTime of the developer
	 * @throws IllegalArgumentException when the break time is null
	 */
	private void setBreakTime(AvailabilityPeriod breakTime) {
		if(breakTime == null) {
			throw new IllegalArgumentException("A developer must have a break time!");
		}
		this.breakTime = breakTime;
	}
	
	/**
	 * aAds a new break time for this developer.
	 *
	 * @param startTime the start time of his break
	 * @throws IllegalArgumentException when the start time is null
	 */
	public void addBreakTime(LocalTime startTime) {
		if(startTime == null) {
			throw new IllegalArgumentException("the start time can't be null!");
		}
		LocalTime startBreak = LocalTime.of(11, 0);
		LocalTime endBreak = LocalTime.of(13, 0);
		LocalTime endTime = startTime.plusHours(1);
		if(startTime.isBefore(startBreak) || endTime.isAfter(endBreak)) {
			throw new IllegalArgumentException("The break time must be between 11:00 end 13:00");
		}
		setBreakTime(new AvailabilityPeriod(startTime, endTime));
	}

	/**
	 * Returns if the developer is available at the given time span.
	 *
	 * @param timeSpan the time span to check with
	 * @return true if the developer is available at the given time span
	 */
	@Override
	public boolean isAvailable(TimeSpan timeSpan) {
		if(overlapsWithBreak(timeSpan)) {
			return false;
		}
		return super.isAvailable(timeSpan);
	}
	
	/**
	 * Checks if the given time span overlaps with the break time of the developer.
	 *
	 * @param timeSpan the timespan to check with
	 * @return true if the time span overlaps, else false
	 */
	private boolean overlapsWithBreak(TimeSpan timeSpan)
	{
		if(timeSpan.getStartTime().toLocalTime().isAfter(getBreakTime().getStartTime()) && timeSpan.getStartTime().toLocalTime().isBefore(getBreakTime().getEndTime())) {
			return true;
		}
		if(timeSpan.getEndTime().toLocalTime().isAfter(getBreakTime().getStartTime()) && timeSpan.getEndTime().toLocalTime().isBefore(getBreakTime().getEndTime())) {
			return true;
		}
		if(timeSpan.getStartTime().toLocalTime().isBefore(getBreakTime().getStartTime()) && timeSpan.getEndTime().toLocalTime().isAfter(getBreakTime().getEndTime())) {
			return true;
		}
		return false;
	}
	
	/**
	 * represents the developer linked to this resource
	 */
	private Developer developer;
	
	/**
	 * sets the developer to the given developer
	 * @param d the developer of the resource
	 * @throws IllegalArgumentException when the developer is null
	 */
	private void setDeveloper(Developer d) {
		if(d == null) {
			throw new IllegalArgumentException("The developer can't be null!");
		}
		this.developer = d;
	}
	
	/**
	 * @return the developer of the resource
	 */
	public Developer getDeveloper() {
		return developer;
	}

}
