package taskman;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * This class is responsible for storing a consistent system time.
 */
public class Clock {

    /**
     * Represents the time of the clock.
     */
    private LocalDateTime systemTime = LocalDateTime.MIN;

    /**
     * The notation used for the time.
     */
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Sets the time of the clock to the given time.
     * @param newSystemTime a LocalDatTime object.
     * @post the time will be set to the given time.
     */
    private void setSystemTime(LocalDateTime newSystemTime) {
        systemTime = newSystemTime;
    }

    /**
     * Returns the time of the clock.
     * @return a LocalDateTime object.
     */
    public LocalDateTime getSystemTime() {
        return systemTime; // LocalDateTime is immutable!
    }

    /**
     * Returns the time of the clock.
     * @return a String.
     */
    public String getSystemTimeString() {
        return systemTime.format(dateFormatter);
    }

    /**
     * Updates the time of the clock.
     * @param s the new time of the clock.
     * @throws DateTimeParseException   if the text cannot be parsed.
     * @throws IllegalArgumentException if the new time if before the old time.
     * @post the time of the clock will be set to the given time.
     */
    public void updateSystemTime(String s) throws DateTimeParseException, IllegalArgumentException {
        LocalDateTime newSystemTime = LocalDateTime.parse(s, dateFormatter);
        if (newSystemTime.isAfter(systemTime)) {
            setSystemTime(newSystemTime);
        }
        else {
            throw new IllegalArgumentException("New system time must be after the current system time!");
        }
    }
}
