package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.Clock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class ClockTest {

    private Clock clock;
    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
        LocalDateTime time = clock.getSystemTime();
        Assert.assertEquals("Initial SystemTime must be minimal!", LocalDateTime.MIN, time);
        LocalDateTime forceTime = LocalDateTime.of(0, 1, 1, 0, 0);
        time.adjustInto(forceTime);
        Assert.assertNotEquals("SystemTime can be adjusted outside clock!", forceTime, clock.getSystemTime());
    }

    @Test
    public void getSystemTimeString() {
        Assert.assertEquals("Initial SystemTimeString must be minimal!", LocalDateTime.MIN.format(dateFormatter), clock.getSystemTimeString());
    }

    @Test
    public void updateSystemTime() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);
        clock.updateSystemTime(time.format(dateFormatter));
        Assert.assertEquals("SystemTime is not updated!", time, clock.getSystemTime());
        try {
            clock.updateSystemTime(LocalDateTime.of(1999, 12, 31,23, 59).format(dateFormatter));
            Assert.fail("Cannot update clock to past!");
        } catch(Exception e){
            assertEquals("Wrong exception when updating time to past!", IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void saveToXML() {
        Assert.fail("Not implemented!");
    }

    @Test
    public void restoreFromXML() {
        Assert.fail("Not implemented!");
    }
}