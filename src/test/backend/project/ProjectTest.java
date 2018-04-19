package test.backend.project;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import taskman.backend.project.Project;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.task.Task;
import taskman.backend.user.Developer;
import taskman.backend.user.OperationNotPermittedException;
import taskman.backend.user.ProjectManager;
import taskman.backend.user.User;


public class ProjectTest {
	private Project p;
	private LocalDateTime creation;
	private LocalDateTime due;
	private User u;

	@Before
	public void setUp() {
		u = new ProjectManager("test", "test");
		creation = LocalDateTime.of(2018, Month.MARCH, 03, 12,0);
		due = LocalDateTime.of(2018, Month.MARCH, 9, 19, 0);
		p = new Project("test", "testdesc", creation, due, u);
	}

	@Test
	public void testProjectcorrect() {
		Assert.assertEquals("The specified name is not equal to the given name", "test", p.getName());
		Assert.assertEquals("the descriptions are not equal", "testdesc", p.getDescription());
		Assert.assertEquals("The start dates are not equal", creation, p.getCreationTime());
		Assert.assertEquals("the due dates are not equal", due, p.getDueTime());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testProjectEarlyEndDate() {
		due = LocalDateTime.of(2018, Month.MARCH, 1, 14, 0);
		p = new Project("test", "testdesc", creation, due, u);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testProjectNoName()
	{
		p = new Project("", "testdesc", creation, due, u);
	}
	
	@Test(expected=OperationNotPermittedException.class)
	public void testProjectIllegalUser() {
		u = new Developer("test", "test");
		p = new Project("test", "testdesc", creation, due, u);
	}
	
	@Test
	public void testAddTask_legal()
	{
		p.createTask("task name", "taskdesc", 20l, 5.0, u);
		Task added = p.getTasks().get(0);
		Assert.assertEquals("taskdesc", added.getDescription());
		Assert.assertEquals(20l, added.getEstimatedDuration());
		Assert.assertEquals(5.0, added.getAcceptableDeviation(), 0.0);
	}
	
	@Test(expected=OperationNotPermittedException.class)
	public void testAddTask_illegalUser() {
		u = new Developer("test", "test");
		p.createTask("task name", "taskdesc", 20l, 5.0, u);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalNameGetTask() {
		p.getTask("123");
	}
	
	
	/*@Test
	public void testRemoveTask()
	{
		p.createTask("test", 5l, 5.0, u);
		p.removeTask(p.getTasks().get(0));
		Assert.assertTrue(p.getTasks().size() == 0);
	}*/
	
	@Test
	public void testGetStatusFinished()
	{
		p.createTask("task name", "taskdesc", 20l, 5.0, u);
		Task t = p.getTasks().get(0);
		Developer d = new Developer("test", "test");
		ProjectManager pm = new ProjectManager("test", "test");
		ResourceManager rm = new ResourceManager();
		rm.createResourceForUser(d, LocalTime.of(11, 0));
		Resource r = rm.getResourceType("developer").getResource("test");
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		t.initializePlan(rm, startTime);
		t.makeExecuting(rm, startTime, d);
		t.addRequirement(rm, rm.getResourceType("developer"), 1);
		t.endExecution(startTime, startTime.plusMinutes(60), "finished", d);
		Assert.assertEquals("active", p.getStatus(creation));
		Assert.assertEquals("finished", p.getStatus(due));
	}
	
	@Test
	public void testGetStatusFailed()
	{
		p.createTask("task name", "taskdesc", 20l, 5.0, u);
		Task t = p.getTasks().get(0);
		Developer d = new Developer("test", "test");
		ProjectManager pm = new ProjectManager("test", "test");
		ResourceManager rm = new ResourceManager();
		rm.createResourceForUser(d, LocalTime.of(11, 0));
		Resource r = rm.getResourceType("developer").getResource("test");
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 8, 0);
		List<Resource> resources = new ArrayList<>();
		resources.add(r);
		t.addRequirement(rm, rm.getResourceType("developer"), 1);
		t.initializePlan(rm, startTime);
		t.makeExecuting(rm, startTime, d);
		t.endExecution(startTime, startTime.plusMinutes(100), "failed", d);
		Assert.assertEquals("active", p.getStatus(creation));
		Assert.assertEquals("failed", p.getStatus(due));
	}

}
