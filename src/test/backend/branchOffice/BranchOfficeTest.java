package test.backend.branchOffice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.Task;
import taskman.backend.user.UserManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class BranchOfficeTest {

	ProjectManager projectManager;
	ResourceManager resourceManager;
	UserManager userManager;
	BranchOffice branchOffice1;


	@Before
	public void setUp() {
		projectManager = new ProjectManager();
		resourceManager = new ResourceManager();
		userManager = new UserManager();
		branchOffice1 = new BranchOffice("office 1");
	}

	@After
	public void tearDown() {
		projectManager = null;
		resourceManager = null;
		userManager = null;
		branchOffice1 = null;
	}

	@Test
	public void getNameTest() {
		Assert.assertEquals("Incorrect name of office 1!", "office 1", branchOffice1.getName());
	}

	@Test
	public void getProjectManagerTest() {
		Assert.assertNotNull("Project manager cannot be null!", branchOffice1.getProjectManager());
	}

	@Test
	public void getResourceManagerTest() {
		Assert.assertNotNull("Resource manager cannot be null!", branchOffice1.getResourceManager());
	}

	@Test
	public void getUserManagerTest() {
		Assert.assertNotNull("User manager cannot be null!", branchOffice1.getUserManager());
	}

	@Test
	public void delegateTest() {
		BranchOffice branchOffice = new BranchOffice("branchoffice 1234546678");
		Map<ResourceType, Integer> requirements = new HashMap<>();
		Assert.assertEquals("There is no project in the branchoffice.", 0, branchOffice.getProjectManager().getProjects().size());
		branchOffice.executeDelegation(requirements, "taskname", "task description", LocalDateTime.now(), 23, 0.4);
		Assert.assertEquals("There is a project in the branchoffice.", 1, branchOffice.getProjectManager().getProjects().size());
		Project project = branchOffice.getProjectManager().getProjects().get(0);
		Assert.assertEquals("The project name is not correct", "project_delegated_taskname", project.getName());
		Assert.assertEquals("The project contains 1 task", 1, project.getTasks().size());
		Task task = project.getTasks().get(0);
		Assert.assertEquals("The task name is not correct", "delegated_taskname", task.getName());
		Assert.assertEquals("The task description is not correct", "task description", task.getDescription());
		Assert.assertEquals("The task its estimated duration is not correct", 23, task.getEstimatedDuration());
		Assert.assertEquals("The task its acceptable deviation is not correct", 0.4, task.getAcceptableDeviation(), 0);
	}

	@Test (expected = IllegalArgumentException.class)
	public void delegateTestIllegal(){
		BranchOffice branchOffice = new BranchOffice("branchoffice 1234546678");
		Map<ResourceType, Integer> requirements = new HashMap<>();
		ResourceType developer= branchOffice.getResourceManager().getResourceType("developer");
		requirements.put(developer, 1);
		branchOffice.executeDelegation(requirements, "taskname", "task description", LocalDateTime.now(), 23, 0.4);
	}
}