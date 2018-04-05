package test;

import taskman.TaskStatus;
import org.junit.Assert;
import org.junit.Test;

public class TaskStatusTest {

    @Test
    public void testStatus() {
        Assert.assertTrue("There should be at least one status!", 0 < TaskStatus.values().length);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Incorrectly parsed status!", "AVAILABLE", TaskStatus.AVAILABLE.toString());
    }


}