package test.frontend;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.ResourceTypeWrapper;
import taskman.backend.wrappers.TaskWrapper;
import taskman.frontend.UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static test.frontend.StubbedInputStream.stubInputStream;

public class UpdateStatusUseCaseTest {

	private ProjectManager po;
	private UserManager um;
	private ResourceManager rm;
	private Clock clock;
	private SimulationManager sm;
	private Controller c;
	private UserInterface ui;
	private ByteArrayOutputStream outputStream;

	private void addRequirementToTask(TaskWrapper task, ResourceTypeWrapper resourceType, int amount){
		((Task) task).getPlan().addRequirement((ResourceType) resourceType, amount);
	}

	@Before
	public void setUp() {
		po = new ProjectManager();
		um = new UserManager();
		rm = new ResourceManager();
		sm = new SimulationManager();
		clock = new Clock();
		BranchOfficeManager branchOfficeManager = new BranchOfficeManager();
		branchOfficeManager.createBranchOffice("test");
		BranchOffice b = (BranchOffice)branchOfficeManager.getBranchOffices().get(0);
		um = b.getUserManager();
		po = b.getProjectManager();
		rm = b.getResourceManager();
		c = new Controller(clock, branchOfficeManager, new SimulationManager());
		um.createUser("test", "test", "developer", LocalTime.of(12, 0), rm);
		um.createUser("admin2", "admin", "project manager", null, rm);
		c.login(b, "admin2", "admin");
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
		addRequirementToTask(po.getProject("testProject").getTask("testTask"), rm.getResourceType("testType"), 1);
		addRequirementToTask(po.getProject("testProject").getTask("testTask"), rm.getResourceType("developer"), 1);
		c.initializePlan(po.getProject("testProject").getTask("testTask"), creationTime);
		c.login(b, "test", "test");
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
		c.logout();
	}

	@Test
	public void testUpdateTaskStatusNormalFlow() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("1").then("1").then("test").then("5").then("1").then("1").then("26/07/2018 08:00").then("N").then("27/07/2018 10:00").then("N").then("0").then("0").atSomePoint());
		ui.start();
		assertFalse(outputStream.toString().contains("error"));
	}

	@Test
	public void testUpdateTaskStatusIllegalData() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("1").then("1").then("admin2").then("5").then("1").then("1").then("26/07/2018 08:00").then("N").then("27/07/2018 10:00").then("N").then("0").then("0").atSomePoint());
		ui.start();
		System.out.println(outputStream.toString());
		assertTrue(outputStream.toString().contains("error"));
	}

	@Test
	public void testUpdateTaskStatusCancel() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("1").then("1").then("test").then("5").then("1").then("1").then("26/07/2018 08:00").then("N").then("27/07/2018 10:00").then("Y").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("cancel"));
	}

}
