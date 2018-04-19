package test.frontend;

import static org.junit.Assert.*;
import static test.frontend.StubbedInputStream.stubInputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.Controller;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.frontend.UserInterface;

public class CreateProjectUseCaseTest {

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
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
	}
	
	@Test
	public void testCreateProjectNormalFlow() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("2").then("testProject").then("N").then("test").then("N").then("26/07/1996 12:00").then("N").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("successful"));
	}
	
	@Test
	public void testCreateProjectCancel() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("2").then("testProject").then("N").then("test").then("N").then("26/07/1996 12:00").then("Y").then("0").then("0").atSomePoint());
		ui.start();
		assertFalse(outputStream.toString().contains("successful"));
	}
	
	@Test
	public void testCreateProjectIllegalCase() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("2").then("testProject").then("N").then("test").then("N").then("26/07/1996 12:00y").then("N").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(outputStream.toString().contains("error"));
	}

}
