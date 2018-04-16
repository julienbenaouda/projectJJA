/**
 * 
 */
package taskman.backend.resource;

/**
 * This enum represents a comparator for a constraint.
 *
 * @author Julien Benaouda, Jeroen Van Der Donckt
 *
 */
public enum AmountComparator {
	EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, SMALLER_THAN, SMALLER_THAN_OR_EQUALS;

	/**
	 * Returns the evaluation of an amount with another value.
	 *
	 * @param amount the amount to evaluate
	 * @param other the other value to compare with
	 * @return the result of the evaluation of the two values to this amount operator
	 * @throws IllegalStateException the amount operator is not mapped to an existing ordinal
	 */
	public boolean evaluate(int amount, int other) throws IllegalStateException {
		int ordinal = ordinal();
		String name = this.name();
		switch(name) {
			case "EQUALS": return amount == other;
			case "NOT_EQUALS": return amount != other;
			case "GREATER_THAN": return amount > other;
			case "GREATER_THAN_OR_EQUALS": return amount >= other;
			case "SMALLER_THAN": return amount < other;
			case "SMALLER_THAN_OR_EQUALS": return amount <= other;
			default: throw new IllegalStateException("There is no matching operation found with this operator.");
		   // TODO: klopt deze volgorde?
		}
	}
}
