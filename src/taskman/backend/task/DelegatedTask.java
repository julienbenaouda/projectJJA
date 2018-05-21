package taskman.backend.task;

import java.time.LocalDateTime;

/**
 * This class represents a delegated task. That is, a task which is delegated from another branch office.
 * It doesn't contain any data, but restricts the execution of several options which can be executed on a normal task.
 * @author Julien Benaouda
 *
 */
public class DelegatedTask extends Task {

	/**
	 * creates a new delegated task
	 * @param name
	 * @param description
	 * @param estimatedDuration
	 * @param acceptableDeviation
	 */
	public DelegatedTask(String name, String description, long estimatedDuration, double acceptableDeviation) {
		super(name, description, estimatedDuration, acceptableDeviation);
	}
	
	/**
	 * adds a dependency to this task
	 * @throws IllegalStateException because noo dependencies can be added to a delegated task
	 */
	@Override
	public void addDependency(Task task) throws IllegalStateException {
		throw new IllegalStateException("It is not possible to add dependencies to a delegated task!");
	}
	
	/**
	 * removes a dependency from this task
	 * @throws IllegalStateException because removing dependencies from a delegated task is not possible
	 */
	@Override
	public void removeDependency(Task task) throws IllegalStateException {
		throw new IllegalStateException("It is not possible to remove dependencies from a delegated task!");
	}
	
	
	/**
	 * sets the altornative to the given task
	 * @throws IllegalStateException because setting the alternative of a delegated task is not possible
	 */
	@Override
	public void setAlternative(Task task) throws IllegalStateException {
		throw new IllegalStateException("It is not possible to change the alternative of a delegated task!");
	}

}
