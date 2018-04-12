package taskman.backend.user;

import java.util.*;

/**
 * This class represents the user manager in the taskman system.
 * @author Julien Benaouda, Alexander Braekevelt
 *
 */
public class UserManager {

	/**
	 * Creates a new user manager object
	 * @post a new user manager is created 
	 */
	public UserManager() {
		this.users = new ArrayList<>();
	}

	/**
	 * Represents the list of users.
	 */
	private List<User> users;

	/**
	 * Returns a user.
	 * @return a user.
	 * @throws IllegalArgumentException when an user with the given name can't be found.
	 */
	private User getUser(String name) {
		for (User u: this.users) {
			if (u.getName().equals(name)) {
				return u;
			}
		}
		throw new IllegalArgumentException("There exists no user with name '" + name + "'.");
	}

	/**
	 * Returns the current user.
	 * @return the current user.
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
	 * Sets the current user to the given user.
	 * @param currentUser the new user to set as the current user.
	 * @post the current user is set to the new user.
	 */
	private void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Represents the current user logged in.
	 */
	private User currentUser;

	/**
	 * Adds a new user to the list of users.
	 * @param name the name of the user.
	 * @param password the password of the user.
	 * @param type the type of user;
	 * @throws IllegalArgumentException if the type is not valid.
	 * @post a new user is added to the list of users.
	 */
	public void createUser(String name, String password, String type) throws IllegalArgumentException {
		switch (type) {
			case "developer":
				createDeveloper(name, password);
				break;
			case "project manager":
				createProjectManager(name, password);
				break;
			default:
				throw new IllegalArgumentException("'" + type + "' is not a valid user type!");
		}
	}

	/**
	 * Return the possible user types.
	 * @return a collection of user types.
	 */
	public Collection<String> getUserTypes() {
		return Arrays.asList("developer", "project manager");
	}

	/**
	 * Adds a new developer to the list of users.
	 * @param name the name of the developer.
	 * @param password the password of the user.
	 * @post a new developer is added to the list of users.
	 */
	private void createDeveloper(String name, String password)
	{
		users.add(new Developer(name, password));
	}
	
	/**
	 * Adds a new project manager to the list of users.
	 * @param name the name of the project manager.
	 * @param password the password of the project manager.
	 * @post a project manager with the given name and password is added to the list of users.
	 */
	private void createProjectManager(String name, String password)
	{
		users.add(new ProjectManager(name, password));
	}
	
	/**
	 * Logs in with the given username and password.
	 * @param name the name of the user to log in.
	 * @param password the password of the user to log in with.
	 * @throws IllegalArgumentException when an user with the given name can't be found.
	 * @throws IllegalArgumentException when the password for the user with the given name is incorrect.
	 * @post the user is logged in and is now used in the system.
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