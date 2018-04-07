/**
 * 
 */
package taskman;

/**
 * @author Julien Benaouda
 *
 */
public interface Entity {
	/**
	 * accepts a visitor
	 * @param v the visitor to accept
	 * @param v
	 */
	public void accept(Visitor v);
}
