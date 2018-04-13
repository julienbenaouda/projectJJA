package taskman.backend.wrappers;

import java.time.LocalDateTime;

public interface TimeSpanWrapper {

	/**
	 * returns the start time of this time span object
	 * @return the startTime of the timespan
	 */
	LocalDateTime getStartTime();

	/**
	 * @return the endTime the end time of the timespan
	 */
	LocalDateTime getEndTime();

}