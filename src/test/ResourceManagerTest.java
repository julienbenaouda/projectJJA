package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.resource.ResourceManager;

public class ResourceManagerTest {
	private ResourceManager resourceManager;

	@Before
	public void setUp() {
		resourceManager = new ResourceManager();
	}

	@Test
	public void testAddResourceType() {
		resourceManager.addResourceType("test");
		assertNotNull(resourceManager.getResourceType("test"));
	}

	@Test
	public void testGetStartingTimes() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAvailableResources() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAlternativeResources() {
		fail("Not yet implemented");
	}

	@Test
	public void testPlan() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateResourceForUser() {
		fail("Not yet implemented");
	}

}
