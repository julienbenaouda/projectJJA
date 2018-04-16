package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.resource.Constraint;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.user.Developer;
import taskman.backend.user.ProjectManager;

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
		Task t = new Task("test", 30l, 5.5);
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
		Task task = new Task("test", 25l, 5.5);
		ResourceType type = new ResourceType("test");
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		AvailabilityPeriod always = new AvailabilityPeriod(start, end);
		for(int i = 0; i <= 6; i++) {
			type.addAvailability(i, always);
		}
		Resource resource = new Resource(type);
		task.addRequirement(type, 1);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		Map<ResourceType, List<Resource>> resources = resourceManager.getAvailableResources(task, startTime);
		assertTrue(resources.get(type).size() == 1);
	}

	@Test
	public void testGetAlternativeResources() {
		Task task = new Task("test", 25l, 5.5);
		ResourceType type = new ResourceType("test");
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		AvailabilityPeriod always = new AvailabilityPeriod(start, end);
		for(int i = 0; i <= 6; i++) {
			type.addAvailability(i, always);
		}
		Resource resource = new Resource(type);
		Resource alternative = new Resource(type);
		task.addRequirement(type, 1);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		List<Resource> list = resourceManager.getAlternativeResources(resource, task, startTime);
		assertTrue(list.size() == 1);
		assertEquals(alternative, list.get(0));
	}

	@Test
	public void testPlan() {
		Task task = new Task("test", 25l, 5.5);
		ResourceType type = new ResourceType("test");
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		AvailabilityPeriod always = new AvailabilityPeriod(start, end);
		for(int i = 0; i <= 6; i++) {
			type.addAvailability(i, always);
		}
		Resource resource = new Resource(type);
		Resource alternative = new Resource(type);
		ArrayList<Resource> resources = new ArrayList<>();
		resources.add(resource);
		resources.add(alternative);
		task.addRequirement(type, 2);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		resourceManager.plan(task, resources, startTime);
		Map<ResourceType, List<Resource>> map = resourceManager.getAvailableResources(task, startTime);
		assertEquals(0, map.get(type).size());
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
		ResourceType type1 = new ResourceType("type1");
		Constraint constraint = new Constraint(type1, AmountComparator.EQUALS, 5);
		resourceManager.addConstraint(constraint);
		Task task = new Task("test", 30l, 5.5);
		task.addRequirement(type1, 5);
		// assertTrue(resourceManager.)
		// finish this test
	}
}
