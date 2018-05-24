package test.frontend;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.Controller;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.frontend.UserInterface;
import static test.frontend.StubbedInputStream.stubInputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AdvanceSystemTimeUseCaseTest {
	private Clock clock;
	private UserInterface ui;
	private ByteArrayOutputStream outputStream;

	@Before
	public void setUp() {
		clock = new Clock();
		BranchOfficeManager bm = new BranchOfficeManager();
		Controller c = new Controller(clock, bm, new SimulationManager());
		c.createBranchOffice("test");
		bm.changeCurrentBranchOffice(bm.getBranchOffices().get(0));
		c.createUser(bm.getCurrentBranchOffice(), "test", "test", "project manager", null);
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
	}

	@Test
	public void testAdvanceSystemTime() {
		System.setOut(new PrintStream(outputStream));
		System.setIn(stubInputStream().then("1").then("1").then("1").then("test").then("13").then("28/06/2018 15:00").then("0").then("0").atSomePoint());
		ui.start();
		assertTrue(clock.getTime().getYear() == 2018);
	}

}
