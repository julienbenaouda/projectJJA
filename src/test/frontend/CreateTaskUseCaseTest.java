package test.frontend;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.frontend.UserInterface;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static test.frontend.StubbedInputStream.stubInputStream;

public class CreateTaskUseCaseTest {

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
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void testNormalFlow() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("3").then("1").then("N").then("testTask").then("N").then("description").then("N").then("60").then("N").then("5.5").then("N").then("1").then("0").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("Task created successfully!"));
	}
	
	@Test
	public void testCreateTaskCancel() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("3").then("1").then("N").then("testTask").then("N").then("description").then("N").then("60").then("N").then("5.5").then("Y").then("1").then("0").then("0").then("0").atSomePoint());
		ui.start();
		assertFalse(outputStream.toString().contains("successful"));
	}

	@Test
	public void testCreateTaskIllegalData() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("3").then("1").then("N").then("testTask").then("N").then("description").then("N").then("60").then("N").then("5,5").then("N").then("1").then("0").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("error"));
	}

}
