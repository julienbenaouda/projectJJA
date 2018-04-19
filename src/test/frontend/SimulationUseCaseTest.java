package test.frontend;

import static org.junit.Assert.*;
import static test.frontend.StubbedInputStream.stubInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

public class SimulationUseCaseTest {

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
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		c.createProject("test", "test", dueTime);
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void testNormalFlow() throws IOException {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("test").then("test").then("13").then("2").then("1").then("test").then("N").then("test").then("N").then("20").then("N").then("2.5").then("N").then("0").then("0").then("0").atSomePoint());
		ui.start();
		assertFalse(outputStream.toString().contains("error"));
	}


}
