package taskman;

import taskman.backend.Controller;
import taskman.backend.project.ProjectOrganizer;
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
		Controller controller = new Controller(new Clock(), new UserManager(), new ProjectOrganizer());
		UserInterface ui = new UserInterface(controller);
		ui.start();
	}
}
