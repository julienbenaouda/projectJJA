/**
 * 
 */
package taskman;

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
		ui.welcomeDialog();
	}
	// TODO: waarom is deze methode public?
}
