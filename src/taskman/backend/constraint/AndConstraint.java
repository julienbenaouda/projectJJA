package taskman.backend.constraint;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Class representing an and constraint.
 * It is responsible for keeping references to the two clauses of an and literal.
 * It is also responsible for checking if a map of requirements matches this constraint.
 * Note: We apply the Composite Pattern here
 *
 * @author Jeroen Van Der Donckt
 */
public class AndConstraint implements ConstraintComponent {

    /**
     * Creates a new and constraint with given attributes.
     * @param constraint1 the first constraint of the and constraint
     * @param constraint2 the second constraint of the and constraint
     * @post a new and constraint is created with given attributes
     */
    public AndConstraint(ConstraintComponent constraint1, ConstraintComponent constraint2){
        setConstraint1(constraint1);
        setConstraint2(constraint2);
    }

    /**
     * Represents the first constraint of the and constraint.
     */
    private ConstraintComponent constraint1;

    /**
     * Returns the first constraint of the and constraint.
     * @return the first constraint of the and constraint
     */
    public ConstraintComponent getConstraint1() {
        return constraint1;
    }

    /**
     * Sets the first constraint of the and constraint to the given constraint.
     *
     * @param constraint1 the first constraint of the and constraint
     * @post the first constraint of the and constraint is set to the given constraint
     */
    private void setConstraint1(ConstraintComponent constraint1){
        this.constraint1 = constraint1;
    }

    /**
     * Represents the second constraint of the and constraint.
     */
    private ConstraintComponent constraint2;

    /**
     * Returns the second constraint of the and constraint.
     * @return the second constraint of the and constraint
     */
    public ConstraintComponent getConstraint2() {
        return constraint2;
    }

    /**
     * Sets the second constraint of the and constraint to the given constraint.
     * @param constraint2 the second constraint of the and constraint
     * @post the second constraint of the and constraint is set to the given constraint
     */
    private void setConstraint2(ConstraintComponent constraint2){
        this.constraint2 = constraint2;
    }

    /**
     * Returns the evaluate of testing the and constraint with the given requirements.
     * @param requirements the requirements
     * @return true if the requirements meet the and constraint, otherwise false
     */
    @Override
    public boolean solution(Map<ResourceType, Integer> requirements) {
        return getConstraint1().solution(requirements) && getConstraint2().solution(requirements);
    }
}
