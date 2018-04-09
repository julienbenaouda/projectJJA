package taskman.Backend.Time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

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
     * Convert a LocalDateTime to a String.
     *
     * @param time a LocalDateTime.
     * @return a String.
     */
    public static String convertLocalDateTimeToString(LocalDateTime time) {
        return time.format(DATE_TIME_FORMATTER);
    }

    /**
     * Convert a String to a LocalDateTime.
     *
     * @param time a String.
     * @return a LocalDateTime.
     * @throws DateTimeParseException if the text cannot be parsed.
     */
    public static LocalDateTime convertStringToLocalDateTime(String time) throws DateTimeParseException {
        return LocalDateTime.parse(time, DATE_TIME_FORMATTER);
    }

}
