package Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import Domain.Project;
import Domain.Task;
import junit.framework.Assert;

import java.time.*;
import java.util.ArrayList;

public class ProjectTest {
	private Project p;
	private LocalDateTime creation;
	private LocalDateTime due;
	private ArrayList<Task> tasks;
	
	@Before
	public void setUp() {
		creation = LocalDateTime.of(2018, Month.FEBRUARY, 24, 10, 0);
		due = LocalDateTime.of(2018, Month.MARCH, 15, 12, 30);
		tasks = new ArrayList<>();
		p = new Project("test", "test desc", creation, due);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testProject() {
		Assert.assertEquals("The specified name is not equal to the given name", "test", p.getName());
		Assert.assertEquals("the descriptions are not equal", "test desc", p.getDescription());
		Assert.assertEquals("The start dates are not equal", creation, p.getCreationTime());
		Assert.assertEquals("the due dates are not equal", due, p.getDueTime());
		Assert.assertEquals("the list of tasks is not equal", tasks, p.getTasks());
	}

	@Test
	public void testAddTask() {
		p.addTask("test", d, 20, startTime, endTime, dependencies, a);
	}

	@Test
	public void testGetTask() {
		fail("Not yet implemented");
	}

}
