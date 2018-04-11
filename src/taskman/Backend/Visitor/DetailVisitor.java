package taskman.Backend.Visitor;

import taskman.Backend.Project.Project;
import taskman.Backend.Project.ProjectOrganizer;
import taskman.Backend.Task.Task;
import taskman.Backend.Time.Clock;
import taskman.Backend.Time.TimeParser;
import taskman.Backend.Time.TimeSpan;
import taskman.Backend.User.User;
import taskman.Backend.User.UserManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is responsible for giving the details of the first visited entity.
 * @author Julien Benaouda, Alexander Braekevelt
 */
public class DetailVisitor implements Visitor {

	/**
	 * Represents the current system time.
	 */
	private final LocalDateTime time;

	/**
	 * Represents the details of the first visited entity.
	 */
	private Map<String, String> details;

	/**
	 * Creates a new visitor with the given time.
	 * @param time the time.
	 */
	public DetailVisitor(LocalDateTime time) {
		this.time = time;
	}

	/**
	 * Returns the details of the first visited entity.
	 * @throws IllegalStateException if the visitor has no details, because it hasn't visited yet.
	 */
	public HashMap<String, String> getDetails() throws IllegalStateException {
		if (!hasDetails()) {
			throw new IllegalStateException("No details found!");
		}
		return new HashMap<>(this.details);
	}

	/**
	 * Creates a new empty map.
	 */
	private void createDetailsMap() {
		this.details = new LinkedHashMap<>();
	}

	/**
	 * Return if the visitor has already visited an element.
	 * @return a boolean.
	 */
	public boolean hasDetails() {
		return this.details == null;
	}

	/**
	 * Visits a project organizer.
	 * @param p the project organizer to visit.
	 */
	@Override
	public void visitProjectOrganizer(ProjectOrganizer p) {
		if (!hasDetails()) {
			createDetailsMap();
			this.details.put("projects", String.join(", ", p.getProjectNames()));
		}
	}

	/**
	 * Visits a project.
	 * @param p the project to visit.
	 */
	@Override
	public void visitProject(Project p) {
		if (!hasDetails()) {
			createDetailsMap();
			this.details.put("name", p.getName());
			this.details.put("description", p.getDescription());
			this.details.put("creation time", TimeParser.convertLocalDateTimeToString(p.getCreationTime()));
			this.details.put("due time", TimeParser.convertLocalDateTimeToString(p.getDueTime()));
			this.details.put("status", p.getStatus(this.time));
		}
	}

	/**
	 * Visits a task.
	 * @param t the task to visit.
	 */
	@Override
	public void visitTask(Task t) {
		if (!hasDetails()) {
			createDetailsMap();
			this.details.put("description", t.getDescription());
			this.details.put("estimated duration", Long.toString(t.getEstimatedDuration()));
			this.details.put("acceptable deviation", Double.toString(t.getAcceptableDeviation()));
			// TODO: alternative en dependency toevoegen!
		}
	}

	/**
	 * Visits a clock.
	 * @param c the clock.
	 */
	@Override
	public void visitClock(Clock c) {
		if (!hasDetails()) {
			createDetailsMap();
			this.details.put("clock time", TimeParser.convertLocalDateTimeToString(c.getTime()));
		}
	}

	/**
	 * Visits a time span.
	 * @param t the time span to visit.
	 */
	@Override
	public void visitTimeSpan(TimeSpan t) {
		if (!hasDetails()) {
			createDetailsMap();
			this.details.put("start time", TimeParser.convertLocalDateTimeToString(t.getStartTime()));
			this.details.put("end time", TimeParser.convertLocalDateTimeToString(t.getEndTime()));
		}
	}

	/**
	 * Visits a user manager.
	 * @param u the user manager to visit.
	 */
	@Override
	public void visitUserManager(UserManager u) {
		if (!hasDetails()) {
			createDetailsMap();
			this.details.put("current user", u.getCurrentUser().getName());
		}
	}

	/**
	 * Visits a user.
	 * @param u the user to visit.
	 */
	@Override
	public void visitUser(User u) {
		if (!hasDetails()) {
			createDetailsMap();
			this.details.put("user name", u.getName());
			this.details.put("type", u.getClass().getName());
		}
	}

}
