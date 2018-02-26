package test;

import taskman.Status;
import org.junit.Assert;
import org.junit.Test;

public class StatusTest {

    @Test
    public void testStatus() {
        Status a = Status.AVAILABLE;
        Status b = Status.UNAVAILABLE;
        Status c = Status.FINISHED;
        Status d = Status.FAILED;
        Assert.assertNotEquals("All statuses should be unique!", a, b);
        Assert.assertNotEquals("All statuses should be unique!", a, c);
        Assert.assertNotEquals("All statuses should be unique!", a, d);
        Assert.assertNotEquals("All statuses should be unique!", b, a);
        Assert.assertNotEquals("All statuses should be unique!", b, c);
        Assert.assertNotEquals("All statuses should be unique!", b, d);
        Assert.assertNotEquals("All statuses should be unique!", c, a);
        Assert.assertNotEquals("All statuses should be unique!", c, b);
        Assert.assertNotEquals("All statuses should be unique!", c, d);
        Assert.assertNotEquals("All statuses should be unique!", d, a);
        Assert.assertNotEquals("All statuses should be unique!", d, b);
        Assert.assertNotEquals("All statuses should be unique!", d, c);
    }

}