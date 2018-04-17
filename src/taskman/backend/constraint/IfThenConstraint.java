package taskman.backend.constraint;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Class representing a if ... then ... constraint.
 * Note: We apply here the Composite Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public class IfThenConstraint implements ConstraintComponent {

    /**
     * Creates a new if then constraint with given attributes.
     *
     * @param ifConstraint the if constraint of the if then constraint
     * @param thenConstraint the then constraint of the if then constraint
     * @post a new if then constraint is created with given attributes.
     */
    public IfThenConstraint(ConstraintComponent ifConstraint, ConstraintComponent thenConstraint){
        setIfConstraint(ifConstraint);
        setThenConstraint(thenConstraint);
    }


    /**
     * Represents the if constraint of the if then constraint.
     */
    private ConstraintComponent ifConstraint;

    /**
     * Returns the if constraint of the if then constraint.
     *
     * @return the if constraint of the if then constraint.
     */
    public ConstraintComponent getIfConstraint(){
        return ifConstraint;
    }

    /**
     * Sets the if constraint of the if then constraint to the given constraint.
     *
     * @param ifConstraint the if constraint of the if then constraint
     * @post the if constraint of the if then constraint is set to the given constraint
     */
    private void setIfConstraint(ConstraintComponent ifConstraint){
        this.ifConstraint = ifConstraint;
    }


    /**
     * Represents the then constraint of the if then constraint.
     */
    private ConstraintComponent thenConstraint;

    /**
     * Returns the then constraint of the if then constraint.
     *
     * @return the then constraint of the if then constraint
     */
    public ConstraintComponent getThenConstraint(){
        return thenConstraint;
    }

    /**
     * Sets the then constraint of the if then constraint to the given constraint.
     *
     * @param thenConstraint the then constraint of the if then constraint
     * @post the then constraint of the if then constraint is set to the given constraint
     */
    private void setThenConstraint(ConstraintComponent thenConstraint){
        this.thenConstraint = thenConstraint;
    }


    /**
     * Returns the solution of testing the if then constraint with the given requirements.
     *
     * @param requirements the requirements
     * @return true if the requirements meet the if then constraint, otherwise false
     */
    @Override
    public boolean solution(Map<ResourceType, Integer> requirements) {
        if (getIfConstraint().solution(requirements)){
            return getThenConstraint().solution(requirements);
        }
        else {
            return true;
            // TODO: nakijken of dit wel klopt
        }
    }
}
