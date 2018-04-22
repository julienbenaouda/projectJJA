package test.frontend;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.frontend.UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static test.frontend.StubbedInputStream.stubInputStream;

public class UpdateStatusUseCaseTest {

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
		um.createUser("test", "test", "developer", LocalTime.of(12, 0), rm);
		um.createUser("admin2", "admin", "project manager", null, rm);
		c.login("admin2", "admin");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		c.createProject("testProject", "testDescription", creationTime);
		c.createTask(c.getProjects().get(0), "testTask", "test description", 1l, 4.5, new HashMap<>());
		rm.createResourceType("testType");
		rm.getResourceType("testType").createResource("r1");
		AvailabilityPeriod always = new AvailabilityPeriod(LocalTime.of(0, 0), LocalTime.of(23, 59));
		for(int i = 1; i < 8; i++) {
			rm.getResourceType("testType").addAvailability(i, always);
			rm.getResourceType("developer").addAvailability(i, always);
		}
		c.addRequirementToTask(po.getProject("testProject").getTask("testTask"), rm.getResourceType("testType"), 1);
		c.addRequirementToTask(po.getProject("testProject").getTask("testTask"), rm.getResourceType("developer"), 1);
		c.initializePlan(po.getProject("testProject").getTask("testTask"), creationTime);
		c.login("test", "test");
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
		c.logout();
	}

	@Test
	public void testUpdateTaskStatusNormalFlow() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("5").then("1").then("1").then("26/07/2018 08:00").then("N").then("27/07/2018 10:00").then("N").then("0").then("0").atSomePoint());
		ui.start();
		assertFalse(outputStream.toString().contains("error"));
	}

	@Test
	public void testUpdateTaskStatusIllegalData() {
		// TODO
		/*System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("5").then("1").then("1").then("26/07/2018 08:00").then("N").then("27/12/2018 10:00").then("N").then("5").then("1").then("2").then("26/07/2018 08:00").then("N").then("27/12/2018 10:00aa").then("N").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("error"));*/
	}

	@Test
	public void testUpdateTaskStatusCancel() {
		// TODO
		/*System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("5").then("1").then("1").then("26/07/2018 08:00").then("N").then("27/12/2018 10:00").then("N").then("5").then("1").then("2").then("26/07/2018 08:00").then("N").then("27/12/2018 10:00aa").then("Y").then("0").then("0").atSomePoint());
		ui.start();
		assertFalse(outputStream.toString().contains("error"));*/
	}

}
