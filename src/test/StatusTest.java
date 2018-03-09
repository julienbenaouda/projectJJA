package test;

import taskman.Status;
import org.junit.Assert;
import org.junit.Test;

public class StatusTest {

    @Test
    public void testStatus() {
        Assert.assertTrue("There should be at least one status!", 0 < Status.values().length);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Incorrectly parsed status!", "AVAILABLE", Status.AVAILABLE.toString());
    }

    @Test
    public void testFromString() {
        Assert.assertEquals("Incorrectly parsed status!", Status.AVAILABLE, Status.fromString("AVAILABLE"));
    }

}