/**
 * 
 */
package taskman.backend.time;

import taskman.backend.wrappers.TimeSpanWrapper;

import java.time.LocalDateTime;

/**
 * This class represents a time span containing a start time and end time.
 * It contains also a method to check if it overlaps with another time span.
 * @author Julien Benaouda, Jeroen Van Der Donckt
 *
 */
public class TimeSpan implements TimeSpanWrapper {

	/**
	 * Creates a new timespan object with given starttime and endtime
	 *
	 * @param startTime the start time of the timespan
	 * @param endTime the end time of the timespan
	 * @post a new timespan is created with given parameters
	 */
	public TimeSpan(LocalDateTime startTime, LocalDateTime endTime) {
		setStartTime(startTime);
		setEndTime(endTime);
	}
	
	/* (non-Javadoc)
	 * @see taskman.backend.time.TimeSpanWrapper#getStartTime()
	 */
	@Override
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time to the given starttime
	 * @param startTime the startTime of the timeSpan
	 * @post The startTime is set to the given start time
	 * @throws IllegalStateException if the starttime is null
	 */
	private void setStartTime(LocalDateTime startTime) throws IllegalStateException{
		if (startTime == null) { // TODO: hopelijk violate dit de rest niet
			throw new IllegalArgumentException("The start time can't be null");
		}
		this.startTime = startTime;
	}

	/**
	 * Represents the start time
	 */
	private LocalDateTime startTime;

	/**
	 * Returns the end time of the time span.
	 * @return the end time of the time span
	 */
	@Override
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * sets the end time of the timespan to the given end time. 
	 * @param endTime the endTime of the timespan7@post the end time of the time span is set to the given end time.
	 * @throws IllegalArgumentException if the end time comes before the start time or the end time is null
	 */
	private void setEndTime(LocalDateTime endTime) throws IllegalArgumentException {
		if(endTime == null || canHaveAsEndTime(endTime)) {
			this.endTime = endTime;
		} else {
			throw new IllegalArgumentException("The end time can't be earlier than the start time");
		}
	}
	
	/**
	 * Checks if the given end time is valid for the given time span
	 * @param endTime the end time to check
	 * @return true if the end time comes after the start time, else false
	 */
	public boolean canHaveAsEndTime(LocalDateTime endTime)
	{
		if(endTime == null || endTime.isAfter(startTime)) {
			return true;
		}
		return false;
	}
	
	/**
	 * represents the end time of the timespan
	 */
	private LocalDateTime endTime;

	/**
	 * Returns if the timespan is equal to this timespan.
	 * @param t a timespan.
	 * @return if the timespan is equal to this timespan.
	 */
	public boolean equals(TimeSpan t) {
		return getStartTime().equals(t.getStartTime()) && getEndTime().equals(t.getEndTime());
	}

}
