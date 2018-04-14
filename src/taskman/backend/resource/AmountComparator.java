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
	EqualS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, SMALLER_THAN, SMALLER_THAN_OR_EQUALS
	
	public boolean evaluate(int amount, int other) {
		int ordinal = ordinal();
		switch(ordinal) {
		case 0: return amount == other;
		break;
		case 1: return (amount > other || amount < other);
		break;
		// TODO de rest van de cases
		}
	}
}
