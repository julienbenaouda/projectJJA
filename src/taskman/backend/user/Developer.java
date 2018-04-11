package taskman.backend.user;

/**
 * This class represents a developer in the taskman system.
 * @author Julien Benaouda
 *
 */
public class Developer extends User {

	/**
	 * creates a new developer with given name and password
	 * @param name the name of the developer
	 * @param password the password of the developer
	 * @post a new developer is created with given name and password
	 */
	public Developer(String name, String password) {
		super(name, password);
	}

}
