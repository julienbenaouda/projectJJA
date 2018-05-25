package test.backend.project;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectManager;
import taskman.backend.task.Task;
import taskman.backend.user.Manager;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class ProjectManagerTest {
	private ProjectManager organizer;

	@Before
	public void setUp() {
		organizer = new ProjectManager();
	}
	
	@Test
	public void testProjectOrganizer() {
		assertEquals(0, organizer.getProjects().size());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateProject_alreadyExists() {
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 19, 0);
		Manager user = new Manager("test", "test");
		organizer.createProject("test", "testdesc", creationTime, dueTime, user);
		organizer.createProject("test", "testdesc", creationTime, dueTime, user);
	}
	


	@Test
	public void testCreateProject() {
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 19, 0);
		Manager user = new Manager("test", "test");
		organizer.createProject("test", "testdesc", creationTime, dueTime, user);
		Project p = organizer.getProject("test");
		assertEquals("test", p.getName());
		assertEquals("testdesc", p.getDescription());
		assertEquals(creationTime, p.getCreationTime());
		assertEquals(dueTime, p.getDueTime());
	}


	@Test
	public void testCreateDelegatedTask(){
		ProjectManager manager = new ProjectManager();
		manager.createDelegatedTask("taskname", "task description", LocalDateTime.now(), 123, 0.3);
		Assert.assertEquals("There is a project in the branchoffice.", 1, manager.getProjects().size());
		Project project = manager.getProjects().get(0);
		Assert.assertEquals("The project name is not correct", "project_delegated_taskname", project.getName());
		Assert.assertEquals("The project contains 1 task", 1, project.getTasks().size());
		Task task = project.getTasks().get(0);
		Assert.assertEquals("The task name is not correct", "delegated_taskname", task.getName());
		Assert.assertEquals("The task description is not correct", "task description", task.getDescription());
		Assert.assertEquals("The task its estimated duration is not correct", 123, task.getEstimatedDuration());
		Assert.assertEquals("The task its acceptable deviation is not correct", 0.3, task.getAcceptableDeviation(), 0);
	}

}
