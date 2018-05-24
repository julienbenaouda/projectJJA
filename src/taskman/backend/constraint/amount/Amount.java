package taskman.backend.constraint.amount;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * This interface is responsible for evaluating amounts in constraint expressions.
 *
 * @author Alexander Braekevelt
 */
public interface Amount {

    /**
     * Evaluates the int amount for a given set of requirements.
     * @param requirements the requirements.
     * @return an int.
     */
    int evaluate(Map<ResourceType, Integer> requirements);

}
