/**
 * 
 */
package taskman.Backend;

import taskman.Frontend.UserInterface;

/**
 * This class is responsible for the program initialisation. It does the initial setup when the program starts up.
 * @author Julien Benaouda
 *
 */
public class Initiator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initialize();
	}
	
	public static void initialize() {
		Controller controller = new Controller(new Clock(), new UserManager(), new ProjectOrganizer());
		UserInterface ui = new UserInterface(controller);
		ui.start();
	}
	// TODO: waarom is deze methode public?
}
