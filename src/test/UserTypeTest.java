package test;

import org.junit.Assert;
import org.junit.Test;
import taskman.UserType;

public class UserTypeTest {

    @Test
    public void testUserType() {
        Assert.assertTrue("There should be at least one user type!", 0 < UserType.values().length);
    }

    @Test
    public void testFromString() {
        Assert.assertEquals("Incorrectly parsed user type!", "DEVELOPER", UserType.DEVELOPER.toString());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Incorrectly parsed user type!", UserType.DEVELOPER, UserType.valueOf("DEVELOPER"));
    }
}