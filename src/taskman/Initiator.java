/**
 * 
 */
package taskman;

import java.util.HashMap;

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
		HashMap<String, Project> projects = new HashMap<>();
		Clock clock = new Clock();
		Controller c = new Controller(projects, clock);
		UI ui = new UI(c);
		ui.welcomeDialog();
	}

}
