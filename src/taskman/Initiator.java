package taskman;

import taskman.backend.Controller;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.frontend.UserInterface;

import java.time.LocalDateTime;
import java.time.Month;

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
		Controller controller = new Controller(new Clock(), branchOfficeManager, new SimulationManager());
		// controller.createBranchOffice("Main office");
		// controller.createUser(controller.getBranchOffices().get(0), "admin", "admin", "project manager", null);
		controller.updateTime(LocalDateTime.of(2000, Month.JANUARY, 1, 8, 0));
		UserInterface ui = new UserInterface(controller);
		ui.start();
	}
}
