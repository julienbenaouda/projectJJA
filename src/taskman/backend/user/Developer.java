package taskman.backend.user;

import java.time.LocalDateTime;
import java.time.LocalTime;

import taskman.backend.resource.Resource;
import taskman.backend.time.AvailabilityPeriod;

/**
 * This class represents a developer in the taskman system.
 * @author Julien Benaouda
 *
 */
public class Developer extends User implements Resource {

	/**
	 * creates a new developer with given name and password
	 * @param name the name of the developer
	 * @param password the password of the developer
	 * @post a new developer is created with given name and password
	 */
	public Developer(String name, String password) {
		super(name, password);
	}
	
	/**
	 * @return the breakTime
	 */
	public AvailabilityPeriod getBreakTime() {
		return breakTime;
	}

	/**
	 * @param breakTime the breakTime of the developer
	 */
	private void setBreakTime(AvailabilityPeriod breakTime) {
		this.breakTime = breakTime;
	}

	/**
	 * represents the break of the developer
	 */
	private AvailabilityPeriod breakTime;

	@Override
	public boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime) {
		LocalTime morning = LocalTime.of(8, 0);
		LocalTime evening = LocalTime.of(17, 0);
		if(startTime.isBefore(morning.atDate(startTime.toLocalDate())) || startTime.isAfter(evening.atDate(startTime.toLocalDate()))) {
			return false;
		}
		if(endTime.isBefore(morning.atDate(endTime.toLocalDate())) || endTime.isAfter(evening.atDate(endTime.toLocalDate()))) {
			return false;
		}
		if(startTime.isAfter(getBreakTime().getStartTime().atDate(startTime.toLocalDate())) && startTime.isBefore(getBreakTime().getEndTime().atDate(startTime.toLocalDate()))) {
			return false;
		}
		if(endTime.isAfter(getBreakTime().getStartTime().atDate(endTime.toLocalDate())) && endTime.isBefore(getBreakTime().getEndTime().atDate(endTime.toLocalDate()))) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isAvailable(LocalDateTime time) {
		// TODO Auto-generated method stub
		return false;
	}

}
