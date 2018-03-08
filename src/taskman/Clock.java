package taskman;

import org.w3c.dom.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Clock {

    private static LocalDateTime systemTime = LocalDateTime.MIN;
    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    private static void setSystemTime(LocalDateTime newSystemTime) {
        if (newSystemTime.isAfter(systemTime)) {
            systemTime = newSystemTime;
        }
        else {
            throw new IllegalArgumentException("New system time must be after the current system time!");
        }
    }

    public static LocalDateTime getSystemTime() {
        return systemTime.plusNanos(0);
    }

    public static String getSystemTimeString() {
        return systemTime.format(dateFormatter);
    }

    public static void updateSystemTime(String time) {
        setSystemTime(LocalDateTime.parse(time, dateFormatter));
    }

    public static Element saveToXML() {
        throw new NotImplementedException();
    }

    public static void restoreFromXML(Element xml) {
        throw new NotImplementedException();
    }
}
