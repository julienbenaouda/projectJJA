package taskman.backend.constraint.constraint;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Interface representing the constraint component.
 * It is responsible for storing a constraint on one or multiple requirements.
 * It is also responsible for checking of a map of requirements matches this constraint.
 * Note: We apply the Composite Pattern here
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 */
public interface Constraint {

    /**
     * Returns the evaluate of testing the constraint with the given requirements.
     * @param requirements the requirements
     * @return true if the requirements meet the constraint, otherwise false
     */
    boolean evaluate(Map<ResourceType, Integer> requirements);

}
