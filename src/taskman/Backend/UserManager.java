/**
 *
 */
package taskman.Backend;

import java.util.HashMap;

/**
 * This class represents the user manager in the taskman system.
 * @author Julien Benaouda
 *
 */
public class UserManager {
	/**
	 * creates a new user manager object
	 * @post a new user manager is created 
	 */
	public UserManager() {
		users = new HashMap<>();
	}

	/**
	 * represents the list of users
	 */
	private HashMap<String, User> users;

	/**
	 * returns a user
	 * @return a user
	 * @throws IllegalArgumentException when an user with the given name can't be found
	 */
	private User getUser(String name) {
		if(!this.users.containsKey(name))
		{
			throw new IllegalArgumentException("There exists no user with name '" + name + "'.");
		}
		return this.users.get(name);
	}

	/**
	 * @return the currentUser
	 * @throws OperationNotPermittedException if no user is logged in.
	 */
	public User getCurrentUser() throws OperationNotPermittedException {
		if (this.currentUser == null) {
			throw new OperationNotPermittedException("No user is logged in!");
		} else {
			return currentUser;
		}
	}

	/**
	 * If a user is logged in.
	 * @return a Boolean.
	 */
	public Boolean hasCurrentUser() {
		return this.currentUser != null;
	}

	/**
	 * sets the current user to the given user
	 * @param currentUser the new user to set as the current user
	 * @post the current user is set to the new user
	 */
	private void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * represents the current user logged in
	 */
	private User currentUser;

	/**
	 * adds a new user to the list of users
	 * @param name the name of the user
	 * @param password the password of the user
	 * @param type the type of user
	 * @throws IllegalArgumentException if the type is not valid
	 * @post a new user is added to the list of users.
	 */
	public void createUser(String name, String password, String type) throws IllegalArgumentException {
		switch (type) {
			case "developer":
				createDeveloper(name, password);
				break;
			case "projectmanager":
				createProjectManager(name, password);
				break;
			default:
				throw new IllegalArgumentException("'" + type + "' is not a valid user type!");
		}
	}

	/**
	 * adds a new developer to the list of users
	 * @param name the name of the developer
	 * @param password the password of the user
	 * @post a new developer is added to the list of users
	 */
	private void createDeveloper(String name, String password)
	{
		Developer d = new Developer(name, password);
		users.put(name, d);
	}
	
	/**
	 * adds a new project manager to the list of users
	 * @param name the name of the project manager
	 * @param password the password of the project manager
	 * @post a project manager with the given name and password is added to the list of users
	 */
	private void createProjectManager(String name, String password)
	{
		ProjectManager pm = new ProjectManager(name, password);
		users.put(name, pm);
	}
	
	/**
	 * logs in with the given username and password
	 * @param name the name of the user to log in
	 * @param password the password of the user to log in with
	 * @throws IllegalArgumentException when an user with the given name can't be found
	 * @throws IllegalArgumentException when the password for the user with the given name is incorrect
	 * @post the user is logged in and is now used in the system
	 */
	public void login(String name, String password) throws IllegalArgumentException
	{
		User u = getUser(name);
		if(password.equals(u.getPassword())) {
			setCurrentUser(u);
		} else {
			throw new IllegalArgumentException("The password for user " + name +" is incorrect. Please try again.");
		}
	}

	/**
	 * Logout the current user.
	 */
	public void logout() {
		setCurrentUser(null);
	}

}
