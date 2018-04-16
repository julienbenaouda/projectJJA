package taskman.backend.time;

import java.time.LocalDateTime;

import taskman.backend.visitor.Entity;
import taskman.backend.visitor.Visitor;
import taskman.backend.wrappers.ClockWrapper;

/**
 * This class is responsible for storing a consistent time for the system.
 */
public class Clock implements Entity, ClockWrapper {

    /**
     * Represents the time of the clock.
     */
    private LocalDateTime time = LocalDateTime.MIN;

    /* (non-Javadoc)
	 * @see taskman.backend.time.ClockWrapper#getTime()
	 */
    @Override
	public LocalDateTime getTime() {
        return time; // LocalDateTime is immutable!
    }

    /**
     * Updates the time of the clock to a later time.
     * @param newTime a LocalDateTime with the new time of the clock.
     * @throws IllegalArgumentException if the new time if before the old time.
     * @post the time of the clock will be set to the given time.
     */
    public void updateSystemTime(LocalDateTime newTime) throws IllegalArgumentException {
        if (newTime.isAfter(time)) {
            this.time = newTime;
        }
        else {
            throw new IllegalArgumentException("New system time must be after the current system time!");
        }
    }
}
