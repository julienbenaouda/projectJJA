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
import taskman.frontend.UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertFalse;
import static test.frontend.StubbedInputStream.stubInputStream;

public class SimulationUseCaseTest {

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
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		c.createProject("test", "test", dueTime);
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void testNormalFlow() throws IOException {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("13").then("2").then("1").then("test").then("N").then("test").then("N").then("20").then("N").then("2.5").then("N").then("1").then("0").then("0").then("0").atSomePoint());
		ui.start();
		assertFalse(outputStream.toString().contains("error"));
	}


}
