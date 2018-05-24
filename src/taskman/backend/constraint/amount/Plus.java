package taskman.backend.constraint.amount;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * The class is responsible for storing and evaluating an addition between two amounts.
 *
 * @author Alexander Braekevelt
 */
public class Plus implements Amount {

	/**
	 * Represents the first value of the addition.
	 */
	private final Amount amount1;

	/**
	 * Represents the second value of the addition.
	 */
	private final Amount amount2;

	/**
	 * Creates a plus operator.
	 * @param amount1 the first value of the addition.
	 * @param amount2 the second value of the addition.
	 */
	public Plus(Amount amount1, Amount amount2) {
		this.amount1 = amount1;
		this.amount2 = amount2;
	}

	/**
	 * Evaluates the int amount for a given set of requirements.
	 * @param requirements the requirements.
	 * @return an int.
	 */
	@Override
	public int evaluate(Map<ResourceType, Integer> requirements) {
		return this.amount1.evaluate(requirements) + this.amount2.evaluate(requirements);
	}
}
