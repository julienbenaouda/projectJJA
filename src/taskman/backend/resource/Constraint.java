/**
 *
 */
package taskman.backend.resource;

import java.util.Map;

/**
 * This class represents a constraint from a resource type
 * @author Julien Benaouda, Jeroen Van Der Donckt
 *
 */
public class Constraint {

    /**
     * Creates a new constraint with given attributes.
     *
     * @param rt1 the first resource type
     * @param comp1 the first amount comparator
     * @param amount1 the first amount
     * @param rt2 the second resource type
     * @param comp2 the second amount comparator
     * @param amount2 the second amount
     * @param allowed the boolean indicating whether or not the constraint is allowed
     */
    public Constraint(ResourceType rt1, AmountComparator comp1, int amount1, ResourceType rt2, AmountComparator comp2, int amount2, boolean allowed){
        setResourceType1(rt1);
        setAmountComparator1(comp1);
        setAmount1(amount1);
        setResourceType2(rt2);
        setAmountComparator2(comp2);
        setAmount2(amount2);
        setAllowed(allowed);
    }


    /**
     * Represents the first resource type of the constraint.
     */
    private ResourceType resourceType1;

    /**
     * Returns the first resource type of the constraint.
     *
     * @return the first resource type of the constraint
     */
    public ResourceType getResourceType1(){
        return resourceType1;
    }

    /**
     * Sets the first resource type of the constraint to the given resource type.
     *
     * @param resourceType1 the first resource type of the constraint
     * @post the first resource type of the constraint is set to the given resource type
     */
    private void setResourceType1(ResourceType resourceType1){
        this.resourceType1 = resourceType1;
    }


    /**
     * Represents the second resource type of the constraint.
     */
    private ResourceType resourceType2;

    /**
     * Returns the second resource type of the constraint.
     *
     * @return the second resource type of the constraint
     */
    public ResourceType getResourceType2(){
        return resourceType2;
    }

    /**
     * Sets the second resource type of the constraint to the given resource type.
     *
     * @param resourceType2 the second resource type of the constraint
     * @post the second resource type of the constraint is set to the given resource type
     */
    private void setResourceType2(ResourceType resourceType2){
        this.resourceType2 = resourceType2;
    }


    /**
     * Represents the firs amount comparator of the constraint.
     */
    private AmountComparator amountComparator1;

    /**
     * Returns the first amount comparator of the constraint.
     *
     * @return the first amount comparator of the constraint
     */
    public AmountComparator getAmountComparator1(){
        return amountComparator1;
    }

    /**
     * Sets the first amount comparator of the constraint to the given amount comparator.
     *
     * @param amountComparator1 the first amount comparator of the constraint
     * @post the first amount comparator of the constraint is set to the given amount comparator
     */
    private void setAmountComparator1(AmountComparator amountComparator1){
        this.amountComparator1 = amountComparator1;
    }


    /**
     * Represents the second amount comparator of the constraint.
     */
    private AmountComparator amountComparator2;

    /**
     * Returns the second amount comparator of the constraint.
     *
     * @return the second amount comparator of the constraint
     */
    public AmountComparator getAmountComparator2(){
        return this.amountComparator2;
    }

    /**
     * Sets the second amount comparator of the constraint to the given amount comparator.
     *
     * @param amountComparator2 the second amount comparator of the constraint
     * @post the second amount comparator of the constraint is set to the given amount comparator
     */
    private void setAmountComparator2(AmountComparator amountComparator2){
        this.amountComparator2 = amountComparator2;
    }


    /**
     * Represents the first amount of the constraint.
     */
    private int amount1;

    /**
     * Returns the first amount of the constraint.
     *
     * @return the first amount of the constraint
     */
    public int getAmount1(){
        return amount1;
    }

    /**
     * Sets the first amount of the constraint.
     *
     * @param amount1 the first amount of the constraint
     * @post the first amount of the constraint is set to the given amount
     */
    private void setAmount1(int amount1){
        this.amount1 = amount1;
    }


    /**
     * Represents the second amount of the constraint.
     */
    private int amount2;

    /**
     * Returns the second amount of the constraint.
     *
     * @return the second amount of the constraint
     */
    public int getAmount2(){
        return amount2;
    }

    /**
     * Sets the second amount of the constraint.
     *
     * @param amount2 the second amount of the constraint
     * @post the second amount of the constraint is set to the given amount
     */
    private void setAmount2(int amount2){
        this.amount2 = amount2;
    }


    /**
     * Represents whether or not the constrain is an allowance constraint.
     */
    private boolean allowed;

    /**
     * Returns if the constraint is an allowance constraint. (may have constraint)
     *
     * @return the allowed value of the constraint
     */
    public boolean isAllowed() {
        return allowed;
    }

    /**
     * Sets the allowed value of the constrain to the given value.
     *
     * @param allowed the allowed value of the constraint
     * @post the allowed value of the constraint is set to the given value
     */
    private void setAllowed(boolean allowed){
        this.allowed = allowed;
    }

    public boolean test(Map<ResourceType, Integer> requirements){
        for (ResourceType key : requirements.keySet()){
            if (key == getResourceType1()){
                if (requirements.containsKey(getResourceType2())){
                    return evaluate(requirements.get(key), requirements.get(getResourceType2()));
                }
                else{
                    return evaluate()
                }
            }
        }
        return true;
    }

    private boolean evaluate(int requirementAmount1, int requirementAmount2){

    }

}
