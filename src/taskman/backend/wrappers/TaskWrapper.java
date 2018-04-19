package taskman.backend.wrappers;

import java.util.List;

public interface TaskWrapper {

	/**
	 * Return the name of the task.
	 * @return the name of the task
	 */
	String getName();

	/**
	 * Returns the task description.
	 * @return the task description
	 */
	String getDescription();

	/**
	 * Returns the estimated duration of the task in minutes.
	 * @return the estimated duration of the task in minutes
	 */
	long getEstimatedDuration();

	/**
	 * Returns the acceptable deviation of the task.
	 * @return the acceptable deviation of the task
	 */
	double getAcceptableDeviation();

	/**
	 * Returns the status of the task.
	 * @return the status of the task
	 */
	String getStatus();

	/**
	 * Returns if the task can be planned.
	 * @return if the task can be planned.
	 */
	boolean canBePlanned();

	/**
	 * Returns if the task can be updated.
	 * @return if the task can be updated.
	 */
	boolean canBeUpdated();

	/**
	 * Return the delay between the end time and the estimated end time in minutes.
	 * @return the time between the end time and the estimated end time in minutes.
	 */
	long getDelay() throws IllegalStateException;

	/**
	 * Returns the alternative task of the task.
	 * @return the alternative task.
	 */
	TaskWrapper getAlternative();

	/**
	 * Returns a list with all dependencies of the task.
	 * @return the dependencies of the task
	 */
	List<? extends TaskWrapper> getDependencies();

}