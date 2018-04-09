package taskman.Backend.User;

/**
 * This class reprents an OperationNotPermittedException. It is thrown when a user doesn't have permission to perform an operation.
 * @author Julien Benaouda
 *
 */
public class OperationNotPermittedException extends RuntimeException {


	/**
	 * creates a new object using the given message
	 * @param message the message of the exception
	 */
	public OperationNotPermittedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
