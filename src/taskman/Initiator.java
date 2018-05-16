package taskman;

import taskman.backend.Controller;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.backend.user.UserManager;
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
		Controller controller = new Controller(new Clock(), new BranchOfficeManager(), new SimulationManager());
		UserInterface ui = new UserInterface(controller);
		ui.start();
	}
}
