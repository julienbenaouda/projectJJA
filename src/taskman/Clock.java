package taskman;

import org.w3c.dom.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Clock {

    private LocalDateTime systemTime = LocalDateTime.MIN;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private void setSystemTime(LocalDateTime newSystemTime) {
        systemTime = newSystemTime;
    }

    public LocalDateTime getSystemTime() {
        return systemTime.plusNanos(0);
    }

    public String getSystemTimeString() {
        return systemTime.format(dateFormatter);
    }

    public void updateSystemTime(String s) {
        LocalDateTime newSystemTime = LocalDateTime.parse(s, dateFormatter);
        if (newSystemTime.isAfter(systemTime)) {
            setSystemTime(newSystemTime);
        }
        else {
            throw new IllegalArgumentException("New system time must be after the current system time!");
        }
    }

    public Element saveToXML() {
        throw new NotImplementedException();
    }

    public static void restoreFromXML(Element xml) {
        throw new NotImplementedException();
    }
}
