package taskman;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public static class Parser {

    /**
     * The notation used for the time.
     */
    private static final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);


    public static String LocalDateTimeToString(LocalDateTime time) {
        return time.format(dateFormatter);
    }

    public static LocalDateTime StringToLocalDateTime(String time) {
        return LocalDateTime.parse(time, dateFormatter);
    }

    public static LongToMinutesString(Long minutes) {
        
    }

}
