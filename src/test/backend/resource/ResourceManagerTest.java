package test.backend.resource;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.constraint.ConstraintParser;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.Developer;
import taskman.backend.user.Manager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class ResourceManagerTest {
	private ResourceManager resourceManager;

	@Before
	public void setUp() {
		resourceManager = new ResourceManager();
	}

	@Test
	public void testAddResourceType() {
		resourceManager.createResourceType("test");
		assertNotNull(resourceManager.getResourceType("test"));
	}

	@Test
	public void testGetStartingTimes() {
		Task t = new Task("task", "test", 30l, 5.5);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		Iterator<LocalDateTime> it = resourceManager.getStartingTimes(t.getPlan(), t.getEstimatedDuration(), startTime);
		for(int i = 0; i < 3; i++) {
			LocalDateTime next = it.next();
			assertFalse(next.isBefore(startTime));
			startTime = next;
		}
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
		Manager d = new Manager("test", "test");
		LocalTime startBreak = LocalTime.of(12, 0);
		resourceManager.createResourceForUser(d, startBreak);
		assertEquals(0, resourceManager.getResourceType("developer").getNbOfResources());
	}
	
	@Test
	public void testCheckRequirements_true() {
		resourceManager.createResourceType("type123");
		ResourceType type1 = resourceManager.getResourceType("type123");
		type1.createResource("r1");
		type1.createResource("r2");
		type1.createResource("r3");
		type1.createResource("r4");
		type1.createResource("r5");
		String string = "== type123 5";
		resourceManager.addConstraint(ConstraintParser.parse(string, resourceManager));
		Task task = new Task("task", "test", 30l, 5.5);
		task.addRequirement(resourceManager, type1, 5);
		assertEquals(1, task.getPlan().getRequirements().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCheckRequirements_false() {
		resourceManager.createResourceType("type1");
		ResourceType type1 = resourceManager.getResourceType("type1");
		type1.createResource("r1");
		type1.createResource("r2");
		type1.createResource("r3");
		type1.createResource("r4");
		String string = "== type1 5";
		resourceManager.addConstraint(ConstraintParser.parse(string, resourceManager));
		Task task = new Task("task", "test", 30l, 5.5);
		task.addRequirement(resourceManager, type1, 4);
	}
	
	@Test
	public void testMultipleResources() {
		Task t = new Task("test", "test", 20l, 5.5);
		resourceManager.createResourceType("type89");
		ResourceType type = resourceManager.getResourceType("type89");
		type.createResource("r1");
		type.createResource("r2");
		type.createResource("r3");
		t.addRequirement(resourceManager, type, 2);
		AvailabilityPeriod always = new AvailabilityPeriod(LocalTime.of(0, 0), LocalTime.of(23, 59));
		for(int j = 1; j < 8; j++) {
			type.addAvailability(j, always);
		}
		Manager user = new Manager("test", "test");
		ArrayList<Resource> resources = new ArrayList<>();
		resources.add(type.getResource("r1"));
		resources.add(type.getResource("r2"));
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		t.initializePlan(startTime);
		Resource test = t.getPlannedResources().get(0);
		Task t2 = new Task("test", "test2", 60l, 5.5);
		t2.addRequirement(resourceManager, type, 2);
		TimeSpan ts = new TimeSpan(startTime, startTime.plusMinutes(t2.getEstimatedDuration()));
		List<Resource> r = t.getAlternativeResources(test);
		Iterator<LocalDateTime> i = resourceManager.getStartingTimes(t2.getPlan(), t2.getEstimatedDuration(), startTime);
		assertEquals(1, r.size());
	}
}
