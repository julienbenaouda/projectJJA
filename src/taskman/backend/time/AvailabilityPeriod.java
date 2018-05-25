/**
 * 
 */
package taskman.backend.time;

import taskman.backend.wrappers.AvailabilityPeriodWrapper;

import java.time.LocalTime;

/**
 * This class represents a availability period containing an start time and an end time. IN this class, dates are not involved.
 * It contains a start time and end time in hours and minutes. It is used for the availability of resources.
 * @author Julien Benaouda
 *
 */
public class AvailabilityPeriod implements AvailabilityPeriodWrapper {

	/**
	 * Creates a new availability period object with given starttime and endtime
	 *
	 * @param startTime the start time of the availability period
	 * @param endTime the end time of the availability period
	 * @post a new availability period is created with given parameters
	 */
	public AvailabilityPeriod(LocalTime startTime, LocalTime endTime) {
		setStartTime(startTime);
		setEndTime(endTime);
	}

	/**
	 * Return the start time.
	 * @return a LocalTime.
	 */
	@Override
	public LocalTime getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time to the given starttime
	 * @param startTime the startTime of the timeSpan
	 * @post The startTime is set to the given start time 
	 */
	private void setStartTime(LocalTime startTime) {
		if(startTime != null) {
			this.startTime = startTime;
		} else {
			throw new IllegalArgumentException("The start time can't be empty!");
		}
	}

	/**
	 * Represents the start time
	 */
	private LocalTime startTime;
	
	/* (non-Javadoc)
	 * @see taskman.backend.time.AvailabilityPeriodWrapper#getEndTime()
	 */
	@Override
	public LocalTime getEndTime() {
		return endTime;
	}

	/**
	 * sets the end time of the availability period to the given end time. 
	 * @param endTime the endTime of the availability period7@post the end time of the time span is set to the given end time.
	 * @throws IllegalArgumentException if the end time comes before the start time
	 */
	private void setEndTime(LocalTime endTime) throws IllegalArgumentException {
		if(canHaveAsEndTime(endTime)) {
			this.endTime = endTime;
		} else {
			throw new IllegalArgumentException("The end time can't be earlier than the start time");
		}
	}
	
	/**
	 * Checks if the given end time is valid for the given time span
	 * @param endTime the end time to check
	 * @return true if the end time is not null and comes after the start time, else false
	 */
	public boolean canHaveAsEndTime(LocalTime endTime)
	{
		if(endTime != null && endTime.isAfter(startTime)) {
			return true;
		}
		return false;
	}
	
	/**
	 * represents the end time of the availability period
	 */
	private LocalTime endTime;


	public boolean overlaps(TimeSpan timeSpan){
		if(timeSpan.getStartTime().toLocalTime().isAfter(getStartTime()) && timeSpan.getStartTime().toLocalTime().isBefore(getEndTime())) {
			return true;
		}
		if(timeSpan.getEndTime().toLocalTime().isAfter(getStartTime()) && timeSpan.getEndTime().toLocalTime().isBefore(getEndTime())) {
			return true;
		}
		if(!(timeSpan.getStartTime().toLocalTime().isAfter(getStartTime())) && !(timeSpan.getEndTime().toLocalTime().isBefore(getEndTime()))) {
			return true;
		}
		return false;
	}
}
