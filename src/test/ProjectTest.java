package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.Backend.*;

import java.time.LocalDateTime;
import java.time.Month;


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
		p.createTask("taskdesc", 20l, 5.0, u);
		Task added = p.getTasks().get(0);
		Assert.assertEquals("taskdesc", added.getDescription());
		Assert.assertEquals(20l, added.getEstimatedDuration());
		Assert.assertEquals(5.0, added.getAcceptableDeviation(), 0.0);
	}
	
	@Test(expected=OperationNotPermittedException.class)
	public void testAddTask_illegalUser() {
		u = new Developer("test", "test");
		p.createTask("taskdesc", 20l, 5.0, u);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void testIllegalIndexGetTask() {
		p.getTask(25);
	}
	
	
	@Test
	public void testRemoveTask()
	{
		p.createTask("test", 5l, 5.0, u);
		p.removeTask(p.getTasks().get(0));
		Assert.assertTrue(p.getTasks().size() == 0);
	}
	
	@Test
	public void testGetStatusFinished()
	{
		p.createTask("taskdesc", 20l, 5.0, u);
		Task t = p.getTasks().get(0);
		t.updateStatus(creation, due, TaskStatus.FINISHED);
		Assert.assertEquals("active", p.getStatus(creation));
		Assert.assertEquals("finished", p.getStatus(due));
	}
	
	@Test
	public void testGetStatusFailed()
	{
		p.createTask("taskdesc", 20l, 5.0, u);
		Task t = p.getTasks().get(0);
		t.updateStatus(creation, due, TaskStatus.FAILED);
		Assert.assertEquals("active", p.getStatus(creation));
		Assert.assertEquals("failed", p.getStatus(due));
	}

}
