package test.backend.project;

import org.junit.Before;
import org.junit.Test;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectManager;
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

}
