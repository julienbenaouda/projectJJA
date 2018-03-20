package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.Clock;
import taskman.ImportExportException;
import taskman.XmlObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import static org.junit.Assert.assertEquals;

public class ClockTest {

    private Clock clock;
    private final static DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

    @Before
    public void beforeTest(){
        clock = new Clock();
    }

    @After
    public void afterTest(){
        clock = null;
    }

    @Test
    public void getSystemTime() {
        LocalDateTime time = clock.getTime();
        Assert.assertEquals("Initial SystemTime must be minimal!", LocalDateTime.MIN, time);
        LocalDateTime forceTime = LocalDateTime.of(0, 1, 1, 0, 0);
        time.adjustInto(forceTime);
        Assert.assertNotEquals("SystemTime can be adjusted outside clock!", forceTime, clock.getTime());
    }

    @Test
    public void getSystemTimeString() {
        Assert.assertEquals("Initial SystemTimeString must be minimal!", LocalDateTime.MIN.format(dateFormatter), clock.getSystemTimeString());
    }

    @Test
    public void updateSystemTime() {
        try {
            clock.updateSystemTime(clock.getSystemTimeString());
            Assert.fail("The clock can be updated to the same time!");
        } catch(Exception e){
            assertEquals("Wrong exception when updating time to same time! (" + e.getMessage() + ")", IllegalArgumentException.class, e.getClass());
        }
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);
        clock.updateSystemTime(time.format(dateFormatter));
        Assert.assertEquals("SystemTime is not updated!", time, clock.getTime());
        try {
            clock.updateSystemTime(LocalDateTime.of(1999, 12, 31,23, 59).format(dateFormatter));
            Assert.fail("The clock can be updated to the past!");
        } catch(Exception e){
            assertEquals("Wrong exception when updating time to past!", IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void xml() throws ImportExportException {
        String time = "13/10/1298 12:28";
        clock.updateSystemTime(time);
        XmlObject obj = new XmlObject();
        clock.addToXml(obj);
        Assert.assertEquals("Wrong time in XmlObject!", time, obj.getAttribute("systemTime"));
        clock = null;
        clock = Clock.getFromXml(obj);
        Assert.assertEquals("Wrong time when restoring from xml!", time, clock.getSystemTimeString());
    }
}