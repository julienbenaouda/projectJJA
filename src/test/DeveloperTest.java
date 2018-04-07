/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskman.Backend.Developer;

/**
 * @author Julien Benaouda
 *
 */
public class DeveloperTest {
	Developer d;

	@Before
	public void setUp() {
		d = new Developer("test", "myPassword");
	}

	@Test
	public void testDeveloper_legal() {
		assertEquals("test", d.getName());
		assertEquals("myPassword", d.getPassword());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDeveloper_illegalName() {
		d = new Developer("", "myPassword");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDeveloper_illegalPassword() {
		d = new Developer("test", null);
	}

}
