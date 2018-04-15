/**
 * 
 */
package taskman.backend.resource;

import java.time.LocalTime;

import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;

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
	public DeveloperResource(ResourceType type, LocalTime startBreak) {
		super(type);
		addBreakTime(startBreak);
	}

	/**
	 * @return the breakTime
	 */
	public AvailabilityPeriod getBreakTime() {
		return breakTime;
	}

	/**
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
	 * adds a new break time for this developer
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
			throw new IllegalArgumentException("The break time must be tetween 11:00 end 13:00");
		}
		setBreakTime(new AvailabilityPeriod(startTime, endTime));
	}

	/**
	 * represents the break of the developer
	 */
	private AvailabilityPeriod breakTime;


	@Override
	public boolean isAvailable(TimeSpan timeSpan) {
		if(overlapsWithBreak(timeSpan)) {
			return false;
		}
		return super.isAvailable(timeSpan);
	}
	
	/**
	 * checks if the given time span overlaps with the break time of the developer
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

}
