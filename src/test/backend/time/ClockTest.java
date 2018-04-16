package test.backend.time;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import taskman.backend.time.Clock;

import java.time.LocalDateTime;

public class ClockTest {

    private Clock clock;

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
        Assert.assertEquals("Initial SystemTime must be minimal!", LocalDateTime.MIN, clock.getTime());
        LocalDateTime newTime = LocalDateTime.of(2000, 1, 1, 0, 0);
        clock.updateTime(newTime);
        Assert.assertEquals("Wrong result of getSystemTime!", newTime, clock.getTime());
    }

    @Test
    public void getSystemTimeExternalAdjustment() {
        LocalDateTime time = clock.getTime();
        LocalDateTime forceTime = LocalDateTime.of(0, 1, 1, 0, 0);
        time.adjustInto(forceTime);
        Assert.assertNotEquals("SystemTime can be adjusted outside clock!", forceTime, clock.getTime());
    }

    @Test
    public void updateSystemTime() {
        LocalDateTime firstTime = LocalDateTime.of(2000, 1, 1, 0, 0);
        clock.updateTime(firstTime);
        Assert.assertEquals("SystemTime is not updated!", firstTime, clock.getTime());
        LocalDateTime secondTime = LocalDateTime.of(54321, 12, 31, 23, 59);
        clock.updateTime(secondTime);
        Assert.assertEquals("SystemTime is not updated!", secondTime, clock.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateSystemTimeToMinimal() {
        clock.updateTime(LocalDateTime.MIN);
        Assert.fail("The clock can be updated to the minimal time!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateSystemTimeToPast() {
        clock.updateTime(LocalDateTime.of(2000, 1, 1, 0, 0));
        clock.updateTime(LocalDateTime.of(1999, 12, 31,23, 59));
        Assert.fail("The clock can be updated to the past!");
    }

}