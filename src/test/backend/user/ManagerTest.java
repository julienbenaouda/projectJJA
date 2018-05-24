package test.backend.user;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.user.Manager;

import static org.junit.Assert.assertEquals;

/**
 * @author Julien Benaouda
 *
 */
public class ManagerTest {
	Manager pm;

	@Before
	public void setUp() {
		pm = new Manager("test", "myPassword");
	}

	@Test
	public void testManager() {
		assertEquals("test", pm.getName());
		assertEquals("myPassword", pm.getPassword());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testManagerName() {
		pm = new Manager("", "myPassword");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testManagerPassword() {
		pm = new Manager("test", null);
	}

}
