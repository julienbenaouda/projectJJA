package test.backend.branchOffice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.user.UserManager;

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
}