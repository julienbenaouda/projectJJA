package taskman.backend.constraint.constraint;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Class representing an 'or' constraint.
 * It is responsible for keeping references to the two parts of an 'or' literal.
 * It is also responsible for checking if a map of requirements matches this constraint.
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 */
public class Or implements Constraint {

    /**
     * Represents the first constraint of the 'or' constraint.
     */
    private final Constraint constraint1;

    /**
     * Represents the second constraint of the 'or' constraint.
     */
    private final Constraint constraint2;

    /**
     * Creates a new 'or' constraint with given attributes.
     * @param constraint1 the first constraint of the 'or' constraint.
     * @param constraint2 the second constraint of the 'or' constraint.
     */
    public Or(Constraint constraint1, Constraint constraint2){
        this.constraint1 = constraint1;
        this.constraint2 = constraint2;
    }

    /**
     * Returns the evaluate of testing the 'or' constraint with the given requirements.
     * @param requirements the requirements
     * @return true if the requirements meet the 'or' constraint, otherwise false
     */
    @Override
    public boolean evaluate(Map<ResourceType, Integer> requirements) {
        return this.constraint1.evaluate(requirements) || this.constraint2.evaluate(requirements);
    }
}
