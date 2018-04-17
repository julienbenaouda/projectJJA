package test.backend.resource;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.user.Developer;
import taskman.backend.user.ProjectManager;

import static org.junit.Assert.*;

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
		Task t = new Task("task", "test", 30l, 5.5, resourceManager);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		Iterator<LocalDateTime> it = resourceManager.getStartingTimes(t, startTime);
		for(int i = 0; i < 3; i++) {
			LocalDateTime next = it.next();
			assertFalse(next.isBefore(startTime));
			startTime = next;
		}
	}

	@Test
	public void testGetAvailableResources() {
		Task task = new Task("task", "test", 25l, 5.5, resourceManager);
		ResourceType type = new ResourceType("test");
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		AvailabilityPeriod always = new AvailabilityPeriod(start, end);
		for(int i = 0; i <= 6; i++) {
			type.addAvailability(i, always);
		}
		type.createResource("resource3");
		task.addRequirement(resourceManager, type, 1);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		List<Resource> resources = resourceManager.getAvailableResources(task, startTime);
		assertTrue(resources.size() == 1);
	}

	@Test
	public void testGetAlternativeResources() {
		Task task = new Task("task", "test", 25l, 5.5, resourceManager);
		ResourceType type = new ResourceType("test");
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		AvailabilityPeriod always = new AvailabilityPeriod(start, end);
		for(int i = 0; i <= 6; i++) {
			type.addAvailability(i, always);
		}
		type.createResource("resource1");
		type.createResource("resource2");
		task.addRequirement(resourceManager, type, 1);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		List<Resource> list = resourceManager.getAlternativeResources(type.getResource("resource1"), task, startTime);
		assertTrue(list.size() == 1);
		assertEquals(type.getResource("resource2"), list.get(0));
	}

	@Test
	public void testPlan() {
		Task task = new Task("task", "test", 25l, 5.5, resourceManager);
		ResourceType type = new ResourceType("test");
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		AvailabilityPeriod always = new AvailabilityPeriod(start, end);
		for(int i = 0; i <= 6; i++) {
			type.addAvailability(i, always);
		}
		Resource resource = new Resource("resource4", type);
		Resource alternative = new Resource("resource5", type);
		ArrayList<Resource> resources = new ArrayList<>();
		resources.add(resource);
		resources.add(alternative);
		task.addRequirement(resourceManager, type, 2);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		resourceManager.plan(task, resources, startTime);
		List<Resource> list = resourceManager.getAvailableResources(task, startTime);
		assertEquals(0, list.size());
	}

	@Test
	public void testCreateResourceForUser_legal() {
		Developer d = new Developer("test", "test");
		LocalTime startBreak = LocalTime.of(12, 0);
		resourceManager.createResourceForUser(d, startBreak);
		ResourceType user = resourceManager.getResourceType("developer");
		assertEquals(1, user.getNbOfResources());
	}

	@Test
	public void testCreateResourceForUser_illegal() {
		ProjectManager d = new ProjectManager("test", "test");
		LocalTime startBreak = LocalTime.of(12, 0);
		resourceManager.createResourceForUser(d, startBreak);
		assertEquals(0, resourceManager.getResourceType("developer").getNbOfResources());
	}
	
	@Test
	public void testCheckRequirements_true() {
		resourceManager.addResourceType("type1");
		ResourceType type1 = resourceManager.getResourceType("type1");
		String string = "== type1 5";
		resourceManager.createConstraint(string);
		Task task = new Task("task", "test", 30l, 5.5, resourceManager);
		task.addRequirement(resourceManager, type1, 5);
		assertEquals(1, resourceManager.getPlan(task).getRequirements().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCheckRequirements_false() {
		resourceManager.addResourceType("type1");
		ResourceType type1 = resourceManager.getResourceType("type1");
		String string = "== type1 5";
		resourceManager.createConstraint(string);
		Task task = new Task("task", "test", 30l, 5.5, resourceManager);
		task.addRequirement(resourceManager, type1, 4);
	}
}
