package taskman.backend.constraint;

import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Class representing a constraint. (This is the leaf component)
 * Note: We apply the Composite Pattern here
 *
 * @author Jeroen Van Der Donckt
 */
public class Constraint implements ConstraintComponent {

    /**
     * Create a constraint with given attributes.
     * @param resourceType the resource type of the constraint
     * @param operator the operator of the constraint
     * @param amount the amount of the constraint
     * @post a new constraint is created with given parameters
     */
    public Constraint(ResourceType resourceType, AmountComparator operator, Integer amount){
        setResourceType(resourceType);
        setAmountComparator(operator);
        setAmount(amount);
    }

    /**
     * Represents the resource type of the constraint.
     */
    private ResourceType resourceType;

    /**
     * Returns the resource type of the constraint.
     * @return the resource type of the constraint
     */
    public ResourceType getResourceType(){
        return resourceType;
    }

    /**
     * Sets the resource type of the constraint to the given resource type.
     * @param resourceType the resource type of the constraint
     * @post the resource type of the constraint is set to the given resource type
     */
    private void setResourceType(ResourceType resourceType){
        this.resourceType = resourceType;
    }

    /**
     * Represents the amount comparator of the constraint.
     */
    private AmountComparator amountComparator;

    /**
     * Returns the amount comparator of the constraint.
     * @return the amount comparator of the constraint
     */
    public AmountComparator getAmountComparator(){
        return amountComparator;
    }

    /**
     * Sets the amount comparator of the constraint to the given amount comparator.
     * @param amountComparator the amount comparator of the constraint
     * @post the amount comparator of the constraint is set to the given amount comparator
     */
    private void setAmountComparator(AmountComparator amountComparator){
        this.amountComparator = amountComparator;
    }

    /**
     * Represents the amount of the constraint.
     */
    private int amount;

    /**
     * Returns the amount of the constraint.
     * @return the amount of the constraint
     */
    public int getAmount(){
        return amount;
    }

    /**
     * Sets the amount of the constraint.
     * @param amount the amount of the constraint
     * @post the amount of the constraint is set to the given amount
     */
    private void setAmount(int amount){
        this.amount = amount;
    }

    /**
     * Returns the evaluate of testing the constraint with the given requirements.
     * @param requirements the requirements
     * @return true if the requirements meet the constraint, otherwise false
     */
    @Override
    public boolean solution(Map<ResourceType, Integer> requirements) {
        Integer value = requirements.getOrDefault(getResourceType(), 0);
        return getAmountComparator().evaluate(value, getAmount());
    }

}
