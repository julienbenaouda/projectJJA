package taskman;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * This class is responsible for converting from and to time objects.
 * @author Alexander Braekevelt
 */
public final class TimeParser {

    /**
     * The notation used for the time.
     */
    private static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Convert a LocalDateTime to a String.
     *
     * @param time a LocalDateTime.
     * @return a String.
     */
    public static String convertLocalDateTimeToString(LocalDateTime time) {
        return time.format(dateFormatter);
    }

    /**
     * Convert a String to a LocalDateTime.
     *
     * @param time a String.
     * @return a LocalDateTime.
     */
    public static LocalDateTime convertStringToLocalDateTime(String time) {
        return LocalDateTime.parse(time, dateFormatter);
    }

    /**
     * Convert a Duration to a String.
     *
     * @param duration a Duration.
     * @return a String with the time in minutes.
     */
    public static String convertDurationToString(Duration duration) {
        return Long.toString(duration.toMinutes()) + " minutes";
    }

    /**
     * Convert a String to a Duration.
     *
     * @param duration a String in minutes.
     * @return a Duration.
     */
    public static Duration convertStringToDuration(String duration) {
        return Duration.ofMinutes(Long.parseLong(duration.replace("minutes", "").trim()));
    }

}
