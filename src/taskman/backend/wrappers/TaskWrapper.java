package taskman.backend.wrappers;

public interface TaskWrapper {

	/**
	 * Returns the task description.
	 *
	 * @return the task description
	 */
	String getDescription();

	/**
	 * Returns the estimated duration of the task in minutes.
	 *
	 * @return the estimated duration of the task in minutes
	 */
	long getEstimatedDuration();

	/**
	 * Returns the acceptable deviation of the task.
	 *
	 * @return the acceptable deviation of the task
	 */
	double getAcceptableDeviation();

	/**
	 * Returns the status of the task.
	 *
	 * @return the status of the task
	 */
	String getStatus();

}