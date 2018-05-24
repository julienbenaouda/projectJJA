package test.frontend;
import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.ResourceTypeWrapper;
import taskman.frontend.UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static test.frontend.StubbedInputStream.stubInputStream;

/**
 * @author Julien Benaouda
 *
 */
public class ShowProjectsUseCaseTest {
	private ProjectManager po;
	private UserManager um;
	private ResourceManager rm;
	private Clock clock;
	private SimulationManager sm;
	private Controller c;
	private UserInterface ui;
	private ByteArrayOutputStream outputStream;

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
		um.createUser("test", "test", "project manager", null, rm);
		c.login(b, "test", "test");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		c.createProject("testProject", "testDescription", creationTime);
		HashMap<ResourceTypeWrapper, Integer> empty = new HashMap<>();
		c.createTask(c.getProjects().get(0), "testTask", "test description", 1l, 4.5, empty);
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
		c.logout();
	}

	@Test
	public void testNormalFlow() throws IOException {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("1").then("1").then("test").then("1").then("1").then("").then("1").then("").then("0").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("testProject (status: active)"));
		assertTrue(outputStream.toString().contains("2018"));
		assertTrue(outputStream.toString().contains("testTask"));
	}

}
