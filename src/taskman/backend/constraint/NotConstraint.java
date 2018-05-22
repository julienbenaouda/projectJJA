package taskman.backend.constraint;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Class representing a not constraint.
 * It is responsible for keeping a reference to the clause of a not constraint.
 * It is also responsible for checking if a map of requirements matches this constraint.
 * Note: We apply the Composite Pattern here
 *
 * @author Jeroen Van Der Donckt
 */
public class NotConstraint implements ConstraintComponent {

    /**
     * Creates a new or constraint with given attribute.
     * @param constraint the constraint of the not constraint
     * @post a new not constraint is created with given attribute
     */
    public NotConstraint(ConstraintComponent constraint){
        setConstraint(constraint);
    }

    /**
     * Represents the constraint of the not constraint.
     */
    private ConstraintComponent constraint;

    /**
     * Returns the constraint of the not constraint.
     * @return the constraint of the not constraint
     */
    public ConstraintComponent getConstraint() {
        return constraint;
    }

    /**
     * Sets the constraint of the not constraint to the given constraint.
     * @param constraint the constraint of the not constraint
     * @post the constraint of the not constraint is set to the given constraint
     */
    private void setConstraint(ConstraintComponent constraint){
        this.constraint = constraint;
    }

    /**
     * Returns the evaluate of testing the not constraint with the given requirements.
     * @param requirements the requirements
     * @return true if the requirements meet the not constraint, otherwise false
     */
    @Override
    public boolean solution(Map<ResourceType, Integer> requirements) {
        return !getConstraint().solution(requirements);
    }
}
