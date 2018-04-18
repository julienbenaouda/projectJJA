package taskman.backend.wrappers;

import java.time.LocalDateTime;

public interface ProjectWrapper {

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