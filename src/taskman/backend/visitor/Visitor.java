package taskman.backend.visitor;

import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.Clock;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.User;
import taskman.backend.user.UserManager;

/**
 * This interface represents a visitor.
 * The flow of the visit is determined by the visited entity.
 * @author Julien Benaouda, Alexander Braekevelt
 */
public interface Visitor {

	/**
	 * Visits a project organizer.
	 * @param p the project organizer to visit.
	 */
	void visitProjectOrganizer(ProjectOrganizer p);

	/**
	 * Visits a project.
	 * @param p the project to visit.
	 */
	void visitProject(Project p);

	/**
	 * Visits a task.
	 * @param t the task to visit.
	 */
	void visitTask(Task t);

	/**
	 * Visits a clock.
	 * @param c the clock.
	 */
	void visitClock(Clock c);

	/**
	 * Visits a time span.
	 * @param t the time span to visit.
	 */
	void visitTimeSpan(TimeSpan t);

	/**
	 * Visits a user manager.
	 * @param u the user manager to visit.
	 */
	void visitUserManager(UserManager u);

	/**
	 * Visits a user.
	 * @param u the user to visit.
	 */
	void visitUser(User u);

	/**
	 * visits an availability period
	 * @param availabilityPeriod the availability period to visit
	 */
	void visitAvailabilityPeriod(AvailabilityPeriod availabilityPeriod);

}
