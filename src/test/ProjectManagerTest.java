/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskman.Developer;
import taskman.ProjectManager;

/**
 * @author Julien Benaouda
 *
 */
public class ProjectManagerTest {
	ProjectManager pm;

	@Before
	public void setUp() {
		pm = new ProjectManager("test", "myPassword");
	}

	@Test
	public void testProjectManager() {
		assertEquals("test", pm.getName());
		assertEquals("myPassword", pm.getPassword());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testProjectManagerName() {
		pm = new ProjectManager("", "myPassword");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testProjectManagerPassword() {
		pm = new ProjectManager("test", null);
	}

}
