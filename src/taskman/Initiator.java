package taskman;

import taskman.Backend.Clock;
import taskman.Backend.Controller;
import taskman.Backend.ProjectOrganizer;
import taskman.Backend.UserManager;
import taskman.Frontend.UserInterface;

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
