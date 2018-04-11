package taskman.Backend.Visitor;

import taskman.Backend.Project.Project;
import taskman.Backend.Task.Task;
import taskman.Backend.Time.AvailabilityPeriod;
import taskman.Backend.Time.Clock;
import taskman.Backend.Time.TimeParser;
import taskman.Backend.Time.TimeSpan;
import taskman.Backend.User.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Julien Benaouda
 *
 */
public class DetailVisitor implements Visitor {
	/**
	 * creates a new detail visitor with the given system time
	 * @param currentSystemTime the current system time
	 */
	public DetailVisitor(LocalDateTime currentSystemTime) {
		this.currentSystemTime = currentSystemTime;
	}

	/**
	 * returns the list of details
	 */
	public HashMap<String, String> getDetails() {
		return details;
	}
	
	/**
	 * creates a new empty map
	 */
	private void createDetailsMap() {
		details = new LinkedHashMap<>();
	}
	
	
	/**
	 * Represents the list of details from an entity
	 */
	private HashMap<String, String> details;

	/**
	 * returns the current system time
	 */
	private LocalDateTime getCurrentSystemTime() {
		return currentSystemTime;
	}

	/**
	 * Represents the current system time
	 */
	private final LocalDateTime currentSystemTime;

	/**
	 * visits a project
	 * @param p the project to visit
	 */
	@Override
	public void visitProject(Project p) {
		if(getDetails() == null) {
			createDetailsMap();
			getDetails().put("name", p.getName());
			getDetails().put("description", p.getDescription());
			getDetails().put("creationTime", TimeParser.convertLocalDateTimeToString(p.getCreationTime()));
			getDetails().put("dueTime", TimeParser.convertLocalDateTimeToString(p.getDueTime()));
			getDetails().put("status", p.getStatus(getCurrentSystemTime()));
		}

	}

	/**
	 * visits a task
	 * @param t the task to visit
	 */
	@Override
	public void visitTask(Task t) {
		if(getDetails() == null) {
			createDetailsMap();
			getDetails().put("description", t.getDescription());
			getDetails().put("estimatedDuration", Long.toString(t.getEstimatedDuration()));
			getDetails().put("acceptableDeviation", Double.toString(t.getAcceptableDeviation()));
			// TODO: alternative en dependency toevoegen. Tasks moeten hun index weten!
		}

	}

	/**
	 * visits a timeSpan
	 * @param t the time span to visit
	 */
	@Override
	public void visitTimeSpan(TimeSpan t) {
		if(getDetails() != null) {
			getDetails().put("startTime", TimeParser.convertLocalDateTimeToString(t.getStartTime()));
			getDetails().put("endTime", TimeParser.convertLocalDateTimeToString(t.getEndTime()));
		} 

	}

	/**
	 * visits the system clock
	 * @param c the system clock
	 */
	@Override
	public void visitClock(Clock c) {
		if(getDetails() == null) {
			createDetailsMap();
			getDetails().put("systemTime", TimeParser.convertLocalDateTimeToString(c.getTime()));
		}

	}

	/**
	 * visits a user
	 * @param u the user to visit
	 */
	@Override
	public void visitUser(User u) {
		if(getDetails() == null) {
			createDetailsMap();
			getDetails().put("userName", u.getName());
			getDetails().put("type", u.getClass().getName());
		}

	}
	
	/**
	 * Visits an availability period
	 * @param availabilityPeriod the availability period to visit
	 */
	@Override
	public void visitAvailabilityPeriod(AvailabilityPeriod availabilityPeriod) {
		// TODO Auto-generated method stub
		
	}

}
