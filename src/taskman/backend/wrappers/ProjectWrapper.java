package taskman.backend.wrappers;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectWrapper {

	/**
	 * Returns a list with all tasks of a project.
	 * @return the tasks of the project.
	 */
	List<? extends TaskWrapper> getTasks();

	/**
	 * Returns the name of the project.
	 * @return the name of the project.
	 */
	String getName();

	/**
	 * Returns the project description.
	 * @return the project description.
	 */
	String getDescription();

	/**
	 * Returns the creation time of the project
	 * @return the creationTime of the project.
	 */
	LocalDateTime getCreationTime();

	/**
	 * Returns the due time of the project.
	 * @return the dueTime of the project
	 */
	LocalDateTime getDueTime();

}