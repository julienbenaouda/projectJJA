package taskman.Backend;

import java.time.LocalDateTime;

/**
 * This class is responsible for storing a consistent time for the system.
 */
public class Clock implements Entity {

    /**
     * Represents the time of the clock.
     */
    private LocalDateTime time = LocalDateTime.MIN;

    /**
     * Returns the time of the clock.
     * @return a LocalDateTime object.
     */
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
    
    /**
     * accepts a visitor
     * @param v the visitor to accept
     */
    @Override
    public void accept(Visitor v) {
    	v.visitClock(this);
    }
}
