package test.backend.importExport;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.constraint.ConstraintParser;
import taskman.backend.importexport.XmlObject;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.task.Task;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.ResourceTypeWrapper;
import taskman.backend.wrappers.ResourceWrapper;
import taskman.backend.wrappers.TaskWrapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class XmlObjectTest {
	private ProjectManager po;
	private ResourceManager rm;
	private UserManager um;
	private Clock c;
	private Controller controller;

	private XmlObject o;
	private BranchOfficeManager branchOfficeManager;
	private BranchOffice b;

	@Before
	public void setUp() {
		po = new ProjectManager();
		um = new UserManager();
		rm = new ResourceManager();
		c = new Clock();
		branchOfficeManager = new BranchOfficeManager();
		branchOfficeManager.createBranchOffice("test");
		b = branchOfficeManager.getBranchOffices().get(0);
		um = b.getUserManager();
		po = b.getProjectManager();
		rm = b.getResourceManager();
		controller = new Controller(c, branchOfficeManager, new SimulationManager());
		o = new XmlObject(branchOfficeManager, c);
	}

	@Test
	public void testXmlObject() {
		assertEquals(branchOfficeManager, o.getBranchOfficeManager());
		assertEquals(c, o.getClock());
	}

	private void addRequirementToTask(TaskWrapper task, ResourceTypeWrapper resourceType, int amount){
		((Task) task).getPlan().addRequirement((ResourceType) resourceType, amount);
	}

	@Test
	public void testToXMLString() {
		controller.createUser(b, "test", "test", "project manager", null);
		controller.login(b, "test", "test");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 23, 0);
		po.createProject("test", "test", creationTime, dueTime, um.getCurrentUser());
		Project project = po.getProject("test");
		HashMap<ResourceTypeWrapper, Integer> empty = new HashMap<>();
		controller.createTask(project, "test", "test", 60L, 5.0, empty);
		Task task = project.getTask("test");
		rm.createResourceType("type1");
		ResourceType resourceType = rm.getResourceType("type1");
		rm.getResourceType("type1").createResource("testResource");
		LocalTime startBreak = LocalTime.of(12, 0);
		um.createUser("testDeveloper", "test", "developer", startBreak, rm);
		ResourceType developerResourceType = rm.getResourceType("developer");
		addRequirementToTask(task, resourceType, 1);
		addRequirementToTask(task, developerResourceType, 1);
		ArrayList<ResourceWrapper> resources = new ArrayList<>();
		resources.add(rm.getResourceType("developer").getResource("testDeveloper"));
		resources.add(rm.getResourceType("type1").getResource("testResource"));
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		controller.initializePlan(po.getProject("test").getTask("test"), startTime);
		rm.addConstraint(ConstraintParser.parse("== type1 1", rm));
		try {
			assertNotNull(o.toXMLString());
			//System.out.println(o.toXMLString());
		} catch(Exception e) {
			fail("test failed " +e.getMessage());
		}
	}

	@Test
	public void testFromXMLString() {
		controller.createUser(b, "test", "test", "project manager", null);
		controller.login(b, "test", "test");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 23, 0);
		po.createProject("test", "test", creationTime, dueTime, um.getCurrentUser());
		Project project = po.getProject("test");
		HashMap<ResourceTypeWrapper, Integer> empty = new HashMap<>();
		controller.createTask(project, "test", "test", 60L, 5.0, empty);
		Task task = project.getTask("test");
		rm.createResourceType("type1");
		ResourceType resourceType = rm.getResourceType("type1");
		rm.getResourceType("type1").createResource("testResource");
		LocalTime startBreak = LocalTime.of(12, 0);
		um.createUser("testdeveloper", "test", "developer", startBreak, rm);
		ResourceType developerResourceType = rm.getResourceType("developer");
		addRequirementToTask(task, resourceType, 1);
		addRequirementToTask(task, developerResourceType, 1);
		ArrayList<ResourceWrapper> resources = new ArrayList<>();
		resources.add(rm.getResourceType("developer").getResource("testdeveloper"));
		resources.add(rm.getResourceType("type1").getResource("testResource"));
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		controller.initializePlan(po.getProject("test").getTask("test"), startTime);
		rm.addConstraint(ConstraintParser.parse("== type1 1", rm));
		String xml = "";
		try {
			xml = o.toXMLString();
		} catch(Exception e) {
			fail("test failed " +e.getMessage());
		}
		XmlObject obj = null;
		try {
			obj = XmlObject.fromXMLString(xml);
		} catch (Exception e) {
			fail("test failed: " +e.getMessage());
		}
		BranchOfficeManager bm = obj.getBranchOfficeManager();
		BranchOffice b = (BranchOffice)bm.getBranchOffices().get(0);
		po = b.getProjectManager();
		um = b.getUserManager();
		rm = b.getResourceManager();
		c = obj.getClock();
		assertNotNull(po.getProject("test"));
		assertNotNull(po.getProject("test").getTask("test"));
		assertNotNull(um.getCurrentUser());
		assertNotNull(rm.getResourceType("type1"));
		assertNotNull(rm.getResourceType("type1").getResource("testResource"));
	}

}
