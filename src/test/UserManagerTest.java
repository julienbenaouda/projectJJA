/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskman.UserManager;

/**
 * @author Julien Benaouda
 *
 */
public class UserManagerTest {
	UserManager u;

	@Before
	public void setUp() {
		u = new UserManager();
	}

	@Test
	public void testCreateDeveloper() {
		u.createDeveloper("test", "myPassword");
		u.login("test", "myPassword");
		assertEquals("test", u.getCurrentUser().getName());
		assertEquals("myPassword", u.getCurrentUser().getPassword());
	}

	@Test
	public void testCreateProjectManager() {
		u.createProjectManager("test", "myPassword");
		u.login("test", "myPassword");
		assertEquals("test", u.getCurrentUser().getName());
		assertEquals("myPassword", u.getCurrentUser().getPassword());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testLogin_illegalName() {
		u.login("test", "myPassword");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLogin_illegalPassword() {
		u.createDeveloper("test", "myPassword");
		u.login("test", "test");
	}

}
