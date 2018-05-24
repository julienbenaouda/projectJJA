package test.frontend;

import static org.junit.Assert.*;
import static test.frontend.StubbedInputStream.stubInputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

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

public class DelegateUseCaseTest {

	private ProjectManager po;
	private UserManager um;
	private ResourceManager rm;
	private Clock clock;
	private SimulationManager sm;
	private Controller c;
	private UserInterface ui;
	private ByteArrayOutputStream outputStream;
	private BranchOffice bo2;

	@Before
	public void setUp() {
		po = new ProjectManager();
		um = new UserManager();
		rm = new ResourceManager();
		sm = new SimulationManager();
		clock = new Clock();
		BranchOfficeManager branchOfficeManager = new BranchOfficeManager();
		branchOfficeManager.createBranchOffice("test");
		BranchOffice b = branchOfficeManager.getBranchOffices().get(0);
		um = b.getUserManager();
		po = b.getProjectManager();
		rm = b.getResourceManager();
		c = new Controller(clock, branchOfficeManager, new SimulationManager());
		um.createUser("test", "test", "project manager", null, rm);
		c.login(b, "test", "test");
		c.createResourceType("car");
		c.createResource(rm.getResourceType("car"), "bmw");
		c.createResource(rm.getResourceType("car"), "mercedes");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		c.createProject("testProject", "testDescription", creationTime);
		Map<ResourceTypeWrapper, Integer> requirements = new HashMap<>();
		requirements.put(rm.getResourceType("car"), 2);
		c.createTask(c.getProjects().get(0), "testTask", "test description", 1l, 4.5, requirements);
		c.createBranchOffice("BO2");
		bo2 = branchOfficeManager.getBranchOffices().get(1);
		if(bo2 == b) {
			bo2 = branchOfficeManager.getBranchOffices().get(0);
		}
		bo2.getResourceManager().createResourceType("car");
		bo2.getResourceManager().getResourceType("car").createResource("car1");
		bo2.getResourceManager().getResourceType("car").createResource("car2");
		ui = new UserInterface(c);
		outputStream = new ByteArrayOutputStream();
		c.logout();
	}

	@Test
	public void testDelegateTask() {		
		System.setOut(new PrintStream(outputStream));
		System.setIn(
				stubInputStream()
						.then("1")
						.then("1")
						.then("1")
						.then("test")
						.then("8")
						.then("1")
						.then("1")
						.then("2")
						.then("0")
						.then("0")
						.atSomePoint()
					);
		assertTrue(outputStream.toString().contains("delegates"));
		assertEquals(1, bo2.getProjectManager().getProjects().size());
	}

}
