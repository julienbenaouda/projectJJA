package test.backend.importExport;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.importexport.XmlObject;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.task.Task;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.ResourceWrapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class XmlObjectTest {
	private ProjectOrganizer po;
	private ResourceManager rm;
	private UserManager um;
	private Clock c;
	private Controller controller;
	private XmlObject o;

	@Before
	public void setUp() {
		po = new ProjectOrganizer();
		um = new UserManager();
		rm = new ResourceManager();
		c = new Clock();
		controller = new Controller(c, um, po, rm, new SimulationManager());
		o = new XmlObject(po, um, rm, c);
	}

	@Test
	public void testXmlObject() {
		assertEquals(po, o.getProjectOrganizer());
		assertEquals(rm, o.getResourceManager());
		assertEquals(um, o.getUserManager());
		assertEquals(c, o.getClock());
	}

	@Test
	public void testToXMLString() {
		controller.createUser("test", "test", "project manager", null);
		controller.login("test", "test");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 23, 0);
		po.createProject("test", "test", creationTime, dueTime, um.getCurrentUser());
		Project project = po.getProject("test");
		controller.createTask(project, "test", "test", 60L, 5.0);
		Task task = project.getTask("test");
		rm.createResourceType("type1");
		ResourceType resourceType = rm.getResourceType("type1");
		rm.getResourceType("type1").createResource("testResource");
		LocalTime startBreak = LocalTime.of(12, 0);
		um.createUser("testDeveloper", "test", "developer", startBreak, rm);
		ResourceType developerResourceType = rm.getResourceType("developer");
		controller.addRequirementToTask(task, resourceType, 1);
		controller.addRequirementToTask(task, developerResourceType, 1);
		ArrayList<ResourceWrapper> resources = new ArrayList<>();
		resources.add(rm.getResourceType("developer").getResource("testDeveloper"));
		resources.add(rm.getResourceType("type1").getResource("testResource"));
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		controller.initializePlan(po.getProject("test").getTask("test"), startTime);
		rm.createConstraint("== type1 1");
		try {
			assertNotNull(o.toXMLString());
			//System.out.println(o.toXMLString());
		} catch(Exception e) {
			fail("test failed " +e.getMessage());
		}
	}

	@Test
	public void testFromXMLString() {
		controller.createUser("test", "test", "project manager", null);
		controller.login("test", "test");
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 23, 0);
		po.createProject("test", "test", creationTime, dueTime, um.getCurrentUser());
		Project project = po.getProject("test");
		controller.createTask(project, "test", "test", 60L, 5.0);
		Task task = project.getTask("test");
		rm.createResourceType("type1");
		ResourceType resourceType = rm.getResourceType("type1");
		rm.getResourceType("type1").createResource("testResource");
		LocalTime startBreak = LocalTime.of(12, 0);
		um.createUser("testdeveloper", "test", "developer", startBreak, rm);
		ResourceType developerResourceType = rm.getResourceType("developer");
		controller.addRequirementToTask(task, resourceType, 1);
		controller.addRequirementToTask(task, developerResourceType, 1);
		ArrayList<ResourceWrapper> resources = new ArrayList<>();
		resources.add(rm.getResourceType("developer").getResource("testdeveloper"));
		resources.add(rm.getResourceType("type1").getResource("testResource"));
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 0, 0);
		controller.initializePlan(po.getProject("test").getTask("test"), startTime);
		rm.createConstraint("== type1 1");
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
		po = obj.getProjectOrganizer();
		um = obj.getUserManager();
		rm = obj.getResourceManager();
		c = obj.getClock();
		assertNotNull(po.getProject("test"));
		assertNotNull(po.getProject("test").getTask("test"));
		assertNotNull(um.getCurrentUser());
		assertNotNull(rm.getResourceType("type1"));
		assertNotNull(rm.getResourceType("type1").getResource("testResource"));
	}

}
