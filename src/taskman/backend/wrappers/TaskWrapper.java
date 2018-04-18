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
	 * Returns the status this task can be updated to.
	 * @return a list of strings.
	 */
	List<String> getStatusTransitions();

}