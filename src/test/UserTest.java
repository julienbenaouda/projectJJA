package test;

import org.junit.Assert;
import org.junit.Test;
import taskman.User;
import taskman.UserType;


/**
 * This is a test class for the User class.
 * @author Jeroen Van Der Donckt
 *
 */
public class UserTest {

    @Test
    public void testRegularUser(){
        User.setUserType(UserType.REGLULARUSER);
        Assert.assertFalse(User.canChangeTaskStatus());
    }

    @Test
    public void testDevelopper(){
        User.setUserType(UserType.DEVELOPER);
        Assert.assertTrue(User.canChangeTaskStatus());
    }
}
