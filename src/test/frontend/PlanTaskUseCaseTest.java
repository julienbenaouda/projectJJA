package test.frontend;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.ResourceTypeWrapper;
import taskman.frontend.UserInterface;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

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
		c.createResourceType("car");
		c.createResource(c.getResourceTypes().get(0), "bmw");
		c.createResource(c.getResourceTypes().get(0), "mercedes");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		c.createProject("testProject", "testDescription", creationTime);
		Map<ResourceTypeWrapper, Integer> requirements = new HashMap<>();
		requirements.put(c.getResourceTypes().get(0), 2);
		c.createTask(c.getProjects().get(0), "testTask", "test description", 1l, 4.5, requirements);
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
		c.logout();
	}

	@Test
	public void testPlan() {
		/*System.setOut(new PrintStream(outputStream));
		System.setIn(
				stubInputStream()
						.then("1")
						.then("test")
						.then("test")
						.then("4")
						// TODO
						.atSomePoint()
		);
		ui.start();*/
	}

}
