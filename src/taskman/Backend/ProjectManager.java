/**
 * 
 */
package taskman.Backend;

/**
 * This class represents a project manager in the taskman system.
 * @author Julien Benaouda
 *
 */
public class ProjectManager extends User {

	/**
	 * creates a new project manager with the given parameters
	 * @param name the name of the project manager
	 * @param password the password of the project manager
	 * @post a new project manager is created with the given parameters
	 */
	public ProjectManager(String name, String password) {
		super(name, password);
	}

}
