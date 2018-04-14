package taskman.backend.user;

/**
 * This class represents a project manager in the taskman system.
 * @author Julien Benaouda, Jeroen Van Der Donckt
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

	/**
	 * Returns that the user is a project manager
	 *
	 * @return true
	 */
	@Override
	public boolean isProjectManager(){
		return true;
	}
}
