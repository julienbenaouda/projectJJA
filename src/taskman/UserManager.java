/**
 * 
 */
package taskman;

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
	 * returns the list of users
	 * @return the list of users
	 */
	private HashMap<String, User> getUsers() {
		return users;
	}

	/**
	 * represents the list of users
	 */
	private HashMap<String, User> users;
	
	/**
	 * @return the currentUser
	 */
	public User getCurrentUser() {
		return currentUser;
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
	 * adds a new developer to the list of users
	 * @param name the name of the developer
	 * @param password the password of the user
	 * @post a new developer is added to the list of users
	 */
	public void createDeveloper(String name, String password)
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
	public void createProjectManager(String name, String password)
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
		if(!getUsers().containsKey(name))
		{
			throw new IllegalArgumentException("There exists no user with the given name.");
		}
		User u = getUsers().get(name);
		if(password.equals(u.getPassword())) {
			setCurrentUser(u);
		} else {
			throw new IllegalArgumentException("The password for user " + name +" is incorrect. Please try again.");
		}
	}
}
