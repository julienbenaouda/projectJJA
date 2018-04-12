/**
 * 
 */
package taskman.backend.time;

import java.time.LocalTime;

import taskman.backend.visitor.Entity;
import taskman.backend.visitor.Visitor;

/**
 * This class represents a availability period containing an start time and an end time. IN this class, dates are not involved.
 * @author Julien Benaouda
 *
 */
public class AvailabilityPeriod implements Entity {

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
	 * returns the start time of this time span object
	 * @return the startTime of the availability period
	 */
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
	
	/**
	 * @return the endTime the end time of the availability period
	 */
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
	
	/**
	 * accepts a visitor
	 * @param v the visitor to accept
	 */
	@Override
	public void accept(Visitor v)
	{
		v.visitAvailabilityPeriod(this);
	}
}