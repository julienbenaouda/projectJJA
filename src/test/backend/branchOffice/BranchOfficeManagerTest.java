package test.backend.branchOffice;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.branchOffice.BranchOfficeManager;

import java.util.List;

public class BranchOfficeManagerTest {

	private static BranchOfficeManager branchOfficeManager;

	@BeforeClass
	public static void setUp() {
		branchOfficeManager = new BranchOfficeManager();
	}

	@Test
	public void createBranchOfficeTest() {
		branchOfficeManager.createBranchOffice("office 1");
		branchOfficeManager.createBranchOffice("office 2");
		branchOfficeManager.createBranchOffice("office 3");
	}

	@Test
	public void getBranchOfficesTest() {
		List<BranchOffice> bs = branchOfficeManager.getBranchOffices();
		Assert.assertEquals("Wrong number of branch offices!", 3, branchOfficeManager.getBranchOffices().size());
		for (int i = 0; i < bs.size(); i++) {
			Assert.assertEquals("Branch office has wrong name!", "office " + (i + 1), bs.get(i).getName());
			for (int j = i + 1; j < bs.size(); j++) {
				Assert.assertNotEquals("Duplicate branch office!", bs.get(i), bs.get(j));
			}
		}

	}

	@Test(expected=IllegalStateException.class)
	public void getCurrentBranchOfficeTest() {
		branchOfficeManager.getCurrentBranchOffice();
		Assert.fail("No exception thrown if no current BranchOffice is set!");
	}

	@Test
	public void changeBranchOfficeTest() {
		List<BranchOffice> bs = branchOfficeManager.getBranchOffices();
		for (BranchOffice b: bs) {
			branchOfficeManager.changeCurrentBranchOffice(b);
			Assert.assertEquals("Changed to wrong current branch office!", b, branchOfficeManager.getCurrentBranchOffice());
		}

	}
}