package taskman.backend.visitor;

/**
 * This interface is responsible for accepting visitors.
 * @author Julien Benaouda, Alexander Braekevelt
 */
public interface Entity {

	/**
	 * Accepts a visitor.
	 * @param v the visitor to accept.
	 */
	void accept(Visitor v);

}
