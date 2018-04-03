/**
 * 
 */
package taskman;

import java.time.LocalDateTime;

/**
 * This class represents a time span containing a start time, end time and status
 * @author Julien Benaouda, Jeroen Van Der Donckt
 *
 */
public class TimeSpan {

	/**
	 * Creates a new timespan object with given status
	 *
	 * @param status the status of the timespan
	 */
	public TimeSpan(TaskStatus status){
		setStatus(status);
	}
	// TODO: kan dit er niet voor zorgen dat er null exception optreed bij updateStatus() im Task

	/**
	 * Creates a new timespan object with given starttime, endtime and status
	 *
	 * @param startTime the start time of the timespan
	 * @param endTime the end time of the timespan
	 * @param status the status of the timespan
	 * @post a new timespan is created with given parameters
	 */
	public TimeSpan(LocalDateTime startTime, LocalDateTime endTime, TaskStatus status) {
		setStartTime(startTime);
		setEndTime(endTime);
		setStatus(status);
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
	public void setStartTime(LocalDateTime startTime) {
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
	public void setEndTime(LocalDateTime endTime) throws IllegalArgumentException {
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
	 * @return the status of the timespan
	 */
	public TaskStatus getStatus() {
		return status;
	}

	/**
	 * sets the status of the timespan to the given status
	 * @param status the status of the timespan
	 * @post the status of this timespan is set to the given status
	 */
	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	/**
	 * represents the status of the time span
	 */
	private TaskStatus status;
}
