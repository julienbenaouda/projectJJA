package taskman;

import org.w3c.dom.Element;

import java.time.LocalDateTime;

public class Clock {

    private static LocalDateTime systemTime = LocalDateTime.MIN;

    private static void setSystemTime(LocalDateTime newSystemTime) {
        if (newSystemTime.isAfter(systemTime)) {
            systemTime = newSystemTime;
        }
        else {
            throw new IllegalArgumentException("New system time must be after the current system time!");
        }
    }

    public static LocalDateTime getSystemTime() {
        return null;
    }

    public static String getSystemTimeString() {
        return null;
    }

    public static void updateSystemTime(String time) {

    }

    public static Element saveToXML() {
        return null;
    }

    public static void restoreFromXML(Element xml) {

    }
}
