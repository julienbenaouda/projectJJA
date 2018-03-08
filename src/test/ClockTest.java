package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.Clock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static org.junit.Assert.*;

public class ClockTest {

    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    @Test
    public void getSystemTime() {
        Assert.assertEquals("Initial SystemTime must be minimal!", LocalDateTime.MIN, Clock.getSystemTime());
    }

    @Test
    public void getSystemTimeString() {
        Assert.assertEquals("Initial SystemTimeString must be minimal!", LocalDateTime.MIN.format(dateFormatter), Clock.getSystemTimeString());
    }

    @Test
    public void updateSystemTime() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 0, 0);
        Clock.updateSystemTime(time.format(dateFormatter));
        Assert.assertEquals("SystemTime is not updated!", time, Clock.getSystemTime());
        try {
            Clock.updateSystemTime(LocalDateTime.of(1999, 12, 31,23, 59).format(dateFormatter));
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