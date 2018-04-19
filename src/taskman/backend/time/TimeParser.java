package taskman.backend.time;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

/**
 * This class is responsible for converting from and to time objects.
 * @author Alexander Braekevelt
 */
public final class TimeParser {

    /**
     * The notation used for the time.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    /**
     * The notation used for the time.
     */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Convert a LocalDateTime to a String.
     * @param time a LocalDateTime.
     * @return a String.
     */
    public static String convertLocalDateTimeToString(LocalDateTime time) {
        return time.format(DATE_TIME_FORMATTER);
    }

    /**
     * Convert a String to a LocalDateTime.
     * @param time a String.
     * @return a LocalDateTime.
     * @throws DateTimeParseException if the text cannot be parsed.
     */
    public static LocalDateTime convertStringToLocalDateTime(String time) throws DateTimeParseException {
        return LocalDateTime.parse(time, DATE_TIME_FORMATTER);
    }

    /**
     * Convert a LocalTime to a String.
     * @param time a LocalTime.
     * @return a String.
     */
    public static String convertLocalTimeToString(LocalTime time) {
        return time.format(DATE_FORMATTER);
    }

    /**
     * Convert a String to a LocalTime.
     * @param time a String.
     * @return a LocalTime.
     * @throws DateTimeParseException if the text cannot be parsed.
     */
    public static LocalTime convertStringToLocalTime(String time) throws DateTimeParseException {
        return LocalTime.parse(time, DATE_FORMATTER);
    }

    /**
     * Return the given LocalDateTime rounded up.
     * @param time the LocalDateTime to round up.
     * @return the given LocalDateTime rounded up.
     */
    public static LocalDateTime roundUpLocalDateTime(LocalDateTime time) {
        if (time.getMinute() == 0 && time.getSecond() == 0) {
            return time;
        } else {
            return time.truncatedTo(ChronoUnit.HOURS).plusHours(1);
        }
    }

    /**
     * Return the given LocalTime rounded up.
     * @param time the LocalTime to round up.
     * @return the given LocalTime rounded up.
     */
    public static LocalTime roundUpLocalTime(LocalTime time) {
        if (time.getMinute() == 0 && time.getSecond() == 0) {
            return time;
        } else {
            return time.truncatedTo(ChronoUnit.HOURS).plusHours(1);
        }
    }

}
