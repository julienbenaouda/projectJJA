package taskman.backend.constraint.amount;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * The class is responsible for storing and evaluating an amount of a resource type.
 *
 * @author Alexander Braekevelt
 */
public class Type implements Amount {

	/**
	 * Represents the resource type.
	 */
	private final ResourceType type;

	/**
	 * Creates a type amount.
	 * @param type the resource type.
	 */
	public Type(ResourceType type) {
		this.type = type;
	}

	/**
	 * Evaluates the int amount for a given set of requirements.
	 * @param requirements the requirements.
	 * @return an int.
	 */
	@Override
	public int evaluate(Map<ResourceType, Integer> requirements) {
		return requirements.getOrDefault(this.type, 0);
	}
}
