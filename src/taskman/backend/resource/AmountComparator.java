/**
 * 
 */
package taskman.backend.resource;

/**
 * This enum represents a comparator for a constraint
 * @author Julien Benaouda
 *
 */
public enum AmountComparator {
	EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, SMALLER_THAN, SMALLER_THAN_OR_EQUALS;

	public boolean evaluate(int amount, int other) {
		int ordinal = ordinal();
		switch(ordinal) {
			case 0: return amount == other;
			case 1: return (amount > other || amount < other);
			// TODO de rest van de cases
		}
		return false; // TODO: this is a dummy.
	}
}
