/**
 * 
 */
package taskman.Backend;

/**
 * @author Julien Benaouda
 *
 */
public interface Entity {
	/**
	 * accepts a visitor
	 * @param v the visitor to accept
	 */
	public void accept(Visitor v);
}
