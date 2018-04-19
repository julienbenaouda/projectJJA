package test.frontend;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.ProjectWrapper;
import taskman.backend.wrappers.ResourceWrapper;
import taskman.backend.wrappers.TaskWrapper;
import taskman.frontend.UserInterface;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class PlanTaskUseCaseTest {

	private ProjectOrganizer po;
	private UserManager um;
	private ResourceManager rm;
	private Clock clock;
	private SimulationManager sm;
	private Controller c;
	private UserInterface ui;
	private ByteArrayOutputStream outputStream;

	@Before
	public void setUp() {
		po = new ProjectOrganizer();
		um = new UserManager();
		rm = new ResourceManager();
		sm = new SimulationManager();
		clock = new Clock();
		c = new Controller(clock, um, po, rm, sm);
		um.createUser("test", "test", "project manager", null, rm);
		c.login("test", "test");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		c.createProject("testProject", "testDescription", creationTime);
		c.createTask(c.getProjects().get(0), "testTask", "test description", 1l, 4.5);
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void testPlan() {
		ProjectWrapper project = c.getProjects().get(0);
		TaskWrapper task = c.getTasks(project).get(0);
		assertTrue(task.canBePlanned());
		Iterator<LocalDateTime> iterator = c.getStartingsTimes(task);
		assertTrue(iterator.hasNext());
		LocalDateTime time = iterator.next();
		assertNotNull(time);
		assertTrue(iterator.hasNext());
		time = iterator.next();
		assertNotNull(time);
		assertTrue(iterator.hasNext());
		time = iterator.next();
		assertNotNull(time);
		c.initializePlan(task, time);
		List<ResourceWrapper> suggestion = c.getPlannedResources(task);
		c.getAlternativeResources(task, resourceToChange);
	}

}
