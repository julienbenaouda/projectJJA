package test.frontend;

import static org.junit.Assert.*;
import static test.frontend.StubbedInputStream.stubInputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

public class CreateProjectUseCaseTest {

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
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void testCreateProjectNormalFlow() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("1").then("1").then("test").then("2").then("testProject").then("N").then("test").then("N").then("26/07/1996 12:00").then("N").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("successful"));
	}

	@Test
	public void testCreateProjectCancel() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("1").then("1").then("test").then("2").then("testProject").then("N").then("test").then("N").then("26/07/1996 12:00").then("Y").then("0").then("0").atSomePoint());
		ui.start();
		assertFalse(outputStream.toString().contains("successful"));
	}

	@Test
	public void testCreateProjectIllegalCase() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("1").then("1").then("test").then("2").then("testProject").then("N").then("test").then("N").then("26/07/1996 12:00y").then("N").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("error"));
	}

}
