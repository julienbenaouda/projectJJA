package test;

import taskman.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;


public class ProjectTest {
	private Project p;
	private LocalDateTime creation;
	private LocalDateTime due;

	@Before
	public void setUp() {
		creation = LocalDateTime.of(2018, Month.MARCH, 03, 12,0);
		due = LocalDateTime.of(2018, Month.MARCH, 9, 19, 0);
		p = new Project("test", "testdesc", creation, due);
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
		p = new Project("test", "testdesc", creation, due);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testProjectNoName()
	{
		p = new Project("", "testdesc", creation, due);
	}
	
	@Test
	public void testAddTask()
	{
		p.createTask("taskdesc", 20l, 5.0);
		Task added = p.getTasks().get(0);
		Assert.assertEquals("taskdesc", added.getDescription());
		Assert.assertEquals(20l, added.getEstimatedDuration());
		Assert.assertEquals(5.0, added.getAcceptableDeviation(), 0.0);
	}
	
	@Test
	public void testCreateProjectWithForm()
	{
		Assert.fail("not implemented");
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void testIllegalIndexGetTask() {
		p.getTask(25);
	}
	
	
	@Test
	public void testRemoveTask()
	{
		p.createTask("test", 5l, 5.0);
		p.removeTask(p.getTasks().get(0));
		Assert.assertTrue(p.getTasks().size() == 0);
	}
	
	@Test
	public void testIsFinishedTrue()
	{
		TimeSpan timespan = new TimeSpan(creation, due, TaskStatus.FINISHED);
		p.createTask("taskdesc", 20l, 5.0);
		Task t = p.getTasks().get(0);
		t.updateStatus(timespan);
		Assert.assertTrue(p.isFinished());
	}
	
	@Test
	public void testIsFinishedFalse()
	{
		TimeSpan timespan = new TimeSpan(creation, due, TaskStatus.FAILED);
		p.createTask("taskdesc", 20l, 5.0);
		Task t = p.getTasks().get(0);
		t.updateStatus(timespan);
		Assert.assertFalse(p.isFinished());
	}

}
