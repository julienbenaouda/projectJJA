/**
 * 
 */
package taskman.Backend;

import taskman.Backend.Task.Task;

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

	/* (non-Javadoc)
	 * @see taskman.Backend.Visitor#visitProject(taskman.Backend.Project)
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

	/* (non-Javadoc)
	 * @see taskman.Backend.Visitor#visitTask(taskman.Backend.Task.Task)
	 */
	@Override
	public void visitTask(Task t) {
		if(getDetails() == null) {
			createDetailsMap();
			getDetails().put("description", t.getDescription());
			getDetails().put("estimatedDuration", Long.toString(t.getEstimatedDuration()));
			getDetails().put("acceptableDeviation", Double.toString(t.getAcceptableDeviation()));
		}

	}

	/* (non-Javadoc)
	 * @see taskman.Backend.Visitor#visitTimeSpan(taskman.Backend.TimeSpan)
	 */
	@Override
	public void visitTimeSpan(TimeSpan t) {
		if(getDetails() != null) {
			getDetails().put("startTime", TimeParser.convertLocalDateTimeToString(t.getStartTime()));
			getDetails().put("endTime", TimeParser.convertLocalDateTimeToString(t.getEndTime()));
		} 

	}

	/* (non-Javadoc)
	 * @see taskman.Backend.Visitor#visitClock(taskman.Backend.Clock)
	 */
	@Override
	public void visitClock(Clock c) {
		if(getDetails() == null) {
			createDetailsMap();
			getDetails().put("systemTime", TimeParser.convertLocalDateTimeToString(c.getTime()));
		}

	}

	/* (non-Javadoc)
	 * @see taskman.Backend.Visitor#visitUser(taskman.Backend.User)
	 */
	@Override
	public void visitUser(User u) {
		if(getDetails() == null) {
			createDetailsMap();
			getDetails().put("userName", u.getName());
			getDetails().put("type", u.getClass().getName());
		}

	}

}
