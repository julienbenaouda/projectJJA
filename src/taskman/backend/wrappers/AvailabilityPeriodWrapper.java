package taskman.backend.wrappers;

import java.time.LocalTime;

public interface AvailabilityPeriodWrapper {

	/**
	 * returns the start time of this time span object
	 * @return the startTime of the availability period
	 */
	LocalTime getStartTime();

	/**
	 * @return the endTime the end time of the availability period
	 */
	LocalTime getEndTime();

}