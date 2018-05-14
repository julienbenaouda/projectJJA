package taskman.backend.constraint;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Class representing an or constraint.
 * It is responsible for keeping references to the two parts of an or literal. It is also responsible for checking if a map of requirements matches this constraint.
 * Note: We apply here the Composite Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public class OrConstraint implements ConstraintComponent {

    /**
     * Creates a new or constraint with given attributes.

     * @param constraint1 the first constraint of the or constraint
     * @param constraint2 the second constraint of the or constraint
     * @post a new or constraint is created with given attributes
     */
    public OrConstraint(ConstraintComponent constraint1, ConstraintComponent constraint2){
        setConstraint1(constraint1);
        setConstraint2(constraint2);
    }


    /**
     * Represents the first constraint of the or constraint.
     */
    private ConstraintComponent constraint1;

    /**
     * Returns the first constraint of the or constraint.
     *
     * @return the first constraint of the or constraint
     */
    public ConstraintComponent getConstraint1() {
        return constraint1;
    }

    /**
     * Sets the first constraint of the or constraint to the given constraint.
     *
     * @param constraint1 the first constraint of the or constraint
     * @post the first constraint of the or constraint is set to the given constraint
     */
    private void setConstraint1(ConstraintComponent constraint1){
        this.constraint1 = constraint1;
    }


    /**
     * Represents the second constraint of the or constraint.
     */
    private ConstraintComponent constraint2;

    /**
     * Returns the second constraint of the or constraint.
     *
     * @return the second constraint of the or constraint
     */
    public ConstraintComponent getConstraint2() {
        return constraint2;
    }

    /**
     * Sets the second constraint of the or constraint to the given constraint.
     *
     * @param constraint2 the second constraint of the or constraint
     * @post the second constraint of the or constraint is set to the given constraint
     */
    private void setConstraint2(ConstraintComponent constraint2){
        this.constraint2 = constraint2;
    }


    /**
     * Returns the solution of testing the or constraint with the given requirements.
     *
     * @param requirements the requirements
     * @return true if the requirements meet the or constraint, otherwise false
     */
    @Override
    public boolean solution(Map<ResourceType, Integer> requirements) {
        return getConstraint1().solution(requirements) || getConstraint2().solution(requirements);
    }
}
