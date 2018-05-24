package taskman.backend.constraint.constraint;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Class representing a 'not' constraint.
 * It is responsible for keeping a reference to the clause of a 'not' constraint.
 * It is also responsible for checking if a map of requirements matches this constraint.
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 */
public class Not implements Constraint {

    /**
     * Represents the constraint of the 'not' constraint.
     */
    private final Constraint constraint;

    /**
     * Creates a new 'not' constraint with given attribute.
     * @param constraint the constraint of the 'not' constraint.
     */
    public Not(Constraint constraint){
        this.constraint = constraint;
    }

    /**
     * Returns the evaluate of testing the 'not' constraint with the given requirements.
     * @param requirements the requirements
     * @return true if the requirements meet the 'not' constraint, otherwise false
     */
    @Override
    public boolean evaluate(Map<ResourceType, Integer> requirements) {
        return !this.constraint.evaluate(requirements);
    }
}
