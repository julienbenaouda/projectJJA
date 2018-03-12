package test;

import org.junit.Assert;
import org.junit.Test;
import taskman.User;


/**
 * This is a test class for the User class.
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 *
 */
public class UserTest {

    @Test
    public void testUserType() {
        Assert.assertNotNull("Initial user type cannot be null!", User.getUserType());
        for (String s: new String[]{"", "ldksfjqlkjf"}) {
            try {
                User.setUserType(s);
                Assert.fail("Exception expected when passing '" + s + "'!");
            } catch (Exception e) {
                Assert.assertEquals(e.getClass(), IllegalArgumentException.class);
            }
        }
        for (String s: new String[]{"REGULARUSER", "DEVELOPER"}) {
            User.setUserType(s);
            Assert.assertEquals(s + " is not recognized!", s, User.getUserType());
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

    // TODO: test XML
}
