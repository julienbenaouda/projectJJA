
package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceType;
import taskman.backend.time.TimeSpan;

public class ResourceTypeTest {
	private ResourceType resourceType;

	@Before
	public void setUp() {
		resourceType = new ResourceType("test");
	}

	@Test
	public void testResourceType_legal() {
		assertEquals("test", resourceType.getName());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testResourceType_illegal() {
		resourceType = new ResourceType(null);
	}

	@Test
	public void testAddResource() {
		Resource r = new Resource(resourceType);
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 11, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.JULY, 26, 17, 0);
		TimeSpan timeSpan = new TimeSpan(start, end);
		assertTrue(resourceType.hasAvailableResources(timeSpan, 1));
	}

	@Test
	public void testHasAvailableResources_false() {
		Resource r = new Resource(resourceType);
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 11, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.JULY, 26, 17, 0);
		TimeSpan timeSpan = new TimeSpan(start, end);
		assertFalse(resourceType.hasAvailableResources(timeSpan, 5));
	}

	@Test
	public void testGetAvailableResources() {
		Resource r = new Resource(resourceType);
		LocalDateTime start = LocalDateTime.of(2018, Month.JULY, 26, 11, 0);
		LocalDateTime end = LocalDateTime.of(2018, Month.JULY, 26, 17, 0);
		TimeSpan timeSpan = new TimeSpan(start, end);
		List<Resource> i = resourceType.getAvailableResources(timeSpan);
		int number = i.size();
		assertEquals(1, number);
	}

}

