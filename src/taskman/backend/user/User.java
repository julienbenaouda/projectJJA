package taskman.backend.user;

import taskman.backend.visitor.Entity;
import taskman.backend.visitor.Visitor;
import taskman.backend.wrappers.UserWrapper;

/**
 * This class represents a user in the system.
 * @author Julien Benaouda
 */
public abstract class User implements Entity, UserWrapper {

	/**
	 * Creates a new user with the given name and password.
	 * @param name the name of the user.
	 * @param password the password of the user.
	 * @post a new user is created with the given name and password.
	 */
	public User(String name, String password) {
		setName(name);
		setPassword(password);
	}

	/**
	 * Represents the name of the user.
	 */
	private String name;

	/**
	 * Returns the name of the user.
	 * @return a String.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the user to the given name.
	 * @param name the name of the user.
	 * @throws IllegalArgumentException when the given name is empty.
	 * @post the name of the user is set to the given name.
	 */
	private void setName(String name) throws IllegalArgumentException {
		if(name == null || name.equals("")) {
			throw new IllegalArgumentException("The name can't be empty. Please choose another name");
		}
		this.name = name;
	}

	/**
	 * Represents the password of the user.
	 */
	private String password;
	
	/**
	 * Returns the password of the user.
	 * @return the password of the user.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * sets the password of the user to the given password.
	 * @param password the password of the user.
	 * @throws IllegalArgumentException when the password is empty.
	 * @post the password is set to the given password.
	 */
	private void setPassword(String password) throws IllegalArgumentException {
		if(password == null || password.equals("")) {
			throw new IllegalArgumentException("The password can't be empty. Please chose another password.");
		}
		this.password = password;
	}


	/**
	 * Returns the type of the user.
	 * @return a String.
	 */
	@Override
	public abstract String getUserType();

	/**
	 * Accepts a visitor.
	 * @param v the visitor to accept.
	 */
	@Override
	public void accept(Visitor v) {
		v.visitUser(this);
	}
}
