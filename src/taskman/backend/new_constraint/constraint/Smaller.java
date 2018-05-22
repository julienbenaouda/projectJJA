package taskman.backend.new_constraint.constraint;

import taskman.backend.new_constraint.amount.Amount;
import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Class representing an '<' constraint.
 * It is responsible for keeping references to the two amount of an '<' literal.
 * It is also responsible for checking if a map of requirements matches this constraint.
 * Note: We apply the Composite Pattern here
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 */
public class Smaller implements Constraint {

	/**
	 * Represents the first amount of the '<' constraint.
	 */
	private final Amount constraint1;

	/**
	 * Represents the second amount of the '<' constraint.
	 */
	private final Amount constraint2;

	/**
	 * Creates a new '<' constraint with given attributes.
	 * @param a the first amount of the '<' constraint.
	 * @param b the second amount of the '<' constraint.
	 */
	public Smaller(Amount a, Amount b){
		this.constraint1 = a;
		this.constraint2 = b;
	}

	/**
	 * Returns the evaluate of testing the and constraint with the given requirements.
	 * @param requirements the requirements
	 * @return true if the requirements meet the '<' constraint, otherwise false
	 */
	@Override
	public boolean evaluate(Map<ResourceType, Integer> requirements) {
		return this.constraint1.evaluate(requirements) < this.constraint2.evaluate(requirements);
	}
}
