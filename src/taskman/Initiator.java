package taskman;

import taskman.backend.Controller;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.frontend.UserInterface;

/**
 * This class is responsible for the program initialisation. It does the initial setup when the program starts up.
 * @author Julien Benaouda, Alexander Braekevelt
 */
public class Initiator {

	/**
	 * Starts the taskman program.
	 * @param args ignored arguments.
	 */
	public static void main(String[] args) {
		BranchOfficeManager branchOfficeManager = new BranchOfficeManager();
		branchOfficeManager.createBranchOffice("default branch office");
		branchOfficeManager.changeCurrentBranchOffice(branchOfficeManager.getBranchOffices().get(0));
		Controller controller = new Controller(new Clock(), branchOfficeManager, new SimulationManager());
		controller.createUser(controller.getBranchOffices().get(0), "admin", "admin", "project manager", null);
		UserInterface ui = new UserInterface(controller);
		ui.start();
	}
}
