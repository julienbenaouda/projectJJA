/**
 * 
 */
package taskman.backend.time;

import java.time.LocalDateTime;

import taskman.backend.visitor.Entity;
import taskman.backend.visitor.Visitor;

/**
 * This class represents a time span containing a start time, end time and status
 * @author Julien Benaouda, Jeroen Van Der Donckt
 *
 */
public class TimeSpan implements Entity {

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
	
	/**
	 * returns the start time of this time span object
	 * @return the startTime of the timespan
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time to the given starttime
	 * @param startTime the startTime of the timeSpan
	 * @post The startTime is set to the given start time 
	 */
	private void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Represents the start time
	 */
	private LocalDateTime startTime;
	
	/**
	 * @return the endTime the end time of the timespan
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * sets the end time of the timespan to the given end time. 
	 * @param endTime the endTime of the timespan7@post the end time of the time span is set to the given end time.
	 * @throws IllegalArgumentException if the end time comes before the start time
	 */
	private void setEndTime(LocalDateTime endTime) throws IllegalArgumentException {
		if(canHaveAsEndTime(endTime)) {
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
	 * accepts a visitor
	 * @param v the visitor to accept
	 */
	@Override
	public void accept(Visitor v)
	{
		v.visitTimeSpan(this);
	}

}
