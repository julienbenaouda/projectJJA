package test.backend.project;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.user.ProjectManager;

public class ProjectOrganizerTest {
	private ProjectOrganizer organizer;

	@Before
	public void setUp() {
		organizer = new ProjectOrganizer();
	}
	
	@Test
	public void testProjectOrganizer() {
		assertEquals(0, organizer.getProjects().size());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCreateProject_alreadyExists() {
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 19, 0);
		ProjectManager user = new ProjectManager("test", "test");
		organizer.createProject("test", "testdesc", creationTime, dueTime, user);
		organizer.createProject("test", "testdesc", creationTime, dueTime, user);
	}
	


	@Test
	public void testCreateProject() {
		LocalDateTime creationTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		LocalDateTime dueTime = LocalDateTime.of(2018, Month.JULY, 26, 19, 0);
		ProjectManager user = new ProjectManager("test", "test");
		organizer.createProject("test", "testdesc", creationTime, dueTime, user);
		Project p = organizer.getProject("test");
		assertEquals("test", p.getName());
		assertEquals("testdesc", p.getDescription());
		assertEquals(creationTime, p.getCreationTime());
		assertEquals(dueTime, p.getDueTime());
	}

}
