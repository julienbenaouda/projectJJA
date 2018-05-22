package taskman.backend.new_constraint.amount;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * The class is responsible for storing and evaluating a constant.
 *
 * @author Alexander Braekevelt
 */
public class Constant implements Amount {

	/**
	 * Represents the amount of the constant.
	 */
	private final int constant;

	/**
	 * Creates a constant.
	 * @param constant an int.
	 */
	public Constant(int constant) {
		this.constant = constant;
	}

	/**
	 * Evaluates the int amount for a given set of requirements.
	 * @param requirements the requirements.
	 * @return an int.
	 */
	@Override
	public int evaluate(Map<ResourceType, Integer> requirements) {
		return this.constant;
	}
}
