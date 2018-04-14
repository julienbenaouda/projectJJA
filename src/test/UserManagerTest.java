/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.resource.ResourceManager;
import taskman.backend.user.UserManager;

/**
 * @author Julien Benaouda
 *
 */
public class UserManagerTest {
	private UserManager u;
	private LocalTime startBreak;
	private ResourceManager resourceManager;

	@Before
	public void setUp() {
		u = new UserManager();
		startBreak = LocalTime.of(12, 0);
		resourceManager = new ResourceManager();
	}

	@Test
	public void testCreateDeveloper() {
		u.createUser("test", "myPassword", "developer", startBreak, resourceManager);
		u.login("test", "myPassword");
		assertEquals("test", u.getCurrentUser().getName());
		assertEquals("myPassword", u.getCurrentUser().getPassword());
	}

	@Test
	public void testCreateProjectManager() {
		u.createUser("test", "myPassword", "projectmanager", null, resourceManager);
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
		u.createUser("test", "myPassword", "developer", startBreak, resourceManager);
		u.login("test", "test");
	}
}
