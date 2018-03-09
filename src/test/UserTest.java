package test;

import org.junit.Assert;
import org.junit.Test;
import taskman.User;


/**
 * This is a test class for the User class.
 * @author Jeroen Van Der Donckt
 *
 */
public class UserTest {

    @Test
    public void testUserType() {
        for (String s: new String[]{"REGULARUSER", "DEVELOPER"}) {
            User.setUserType(s);
            Assert.assertEquals(s + " is not recognized!", s, User.getUserType());
        }
        for (String s: new String[]{"", "ldksfjqlkjf"}) {
            try {
                User.setUserType(s);
                Assert.fail("Exception expected when passing '" + s + "'");
            } catch (Exception e) {
                Assert.assertEquals(e.getClass(), IllegalArgumentException.class);
            }
        }
    }

    @Test
    public void testRegularUser(){
        User.setUserType("REGULARUSER");
        Assert.assertFalse(User.canChangeTaskStatus());
    }

    @Test
    public void testDevelopper(){
        User.setUserType("DEVELOPER");
        Assert.assertTrue(User.canChangeTaskStatus());
    }
}
