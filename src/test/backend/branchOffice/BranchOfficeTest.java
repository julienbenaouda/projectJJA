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
	BranchOffice branchOffice2;


	@Before
	public void setUp() {
		projectManager = new ProjectManager();
		resourceManager = new ResourceManager();
		userManager = new UserManager();
		branchOffice1 = new BranchOffice("office 1");
		branchOffice2 = new BranchOffice("office 2", projectManager, resourceManager, userManager);
	}

	@After
	public void tearDown() {
		projectManager = null;
		resourceManager = null;
		userManager = null;
		branchOffice1 = null;
		branchOffice2 = null;
	}

	@Test
	public void getNameTest() {
		Assert.assertEquals("Incorrect name of office 1!", "office 1", branchOffice1.getName());
		Assert.assertEquals("Incorrect name of office 2!", "office 2", branchOffice2.getName());
	}

	@Test
	public void getProjectManagerTest() {
		Assert.assertNotNull("Project manager cannot be null!", branchOffice1.getProjectManager());
		Assert.assertNotNull("Project manager cannot be null!", branchOffice2.getProjectManager());
		Assert.assertEquals("Incorrect project manager!", projectManager, branchOffice2.getProjectManager());
	}

	@Test
	public void getResourceManagerTest() {
		Assert.assertNotNull("Resource manager cannot be null!", branchOffice1.getResourceManager());
		Assert.assertNotNull("Resource manager cannot be null!", branchOffice2.getResourceManager());
		Assert.assertEquals("Incorrect resource manager!", resourceManager, branchOffice2.getResourceManager());
	}

	@Test
	public void getUserManagerTest() {
		Assert.assertNotNull("User manager cannot be null!", branchOffice1.getUserManager());
		Assert.assertNotNull("User manager cannot be null!", branchOffice2.getUserManager());
		Assert.assertEquals("Incorrect user manager!", userManager, branchOffice2.getUserManager());
	}
}