package test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import taskman.User;


/**
 * This is a test class for the User class.
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 *
 */
public class UserTest {

    private static User regularUser, developper, projectManager;

    @BeforeClass
    public static void setUp(){
        regularUser = new User(User.UserType.REGULARUSER);
        developper = new User(User.UserType.DEVELOPPER);
        projectManager = new User(User.UserType.PROJECTMANAGER);
    }

    @Test
    public void testUserType() {
        Assert.assertTrue("There should be at least one user type!", 0 < User.UserType.values().length);
    }

    @Test
    public void testUser() {
        Assert.assertEquals("Usertype does not match", User.UserType.REGULARUSER, regularUser.getUserType());
        Assert.assertEquals("Usertype does not match", User.UserType.DEVELOPPER, developper.getUserType());
        Assert.assertEquals("Usertype does not match", User.UserType.PROJECTMANAGER, projectManager.getUserType());
    }

    @Test
    public void testRegularUser(){
        Assert.assertFalse(regularUser.canChangeTaskStatus());
    }

    @Test
    public void testDevelopper(){
        Assert.assertTrue(developper.canChangeTaskStatus());
    }

    @Test
    public void testProjectManager(){
        Assert.assertFalse(projectManager.canChangeTaskStatus());
    }

    // TODO: test XML
}
