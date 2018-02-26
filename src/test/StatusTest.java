package test;

import taskman.Status;
import org.junit.Assert;
import org.junit.Test;

public class StatusTest {

    @Test
    public void testStatus() {
        Assert.assertTrue("There should be at least one status!", 0 < Status.values().length);
    }

}