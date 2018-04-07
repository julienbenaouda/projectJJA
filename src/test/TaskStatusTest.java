package test;

import taskman.Backend.TaskStatus;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a test class for the Task class.
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 *
 */
public class TaskStatusTest {

    @Test
    public void testStatus() {
        Assert.assertTrue("There should be at least one status!", 0 < TaskStatus.values().length);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Incorrectly parsed status!", "FINISHED", TaskStatus.FINISHED.toString());
    }


}