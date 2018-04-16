package taskman.backend.constraint;

import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;

import java.util.Map;

/**
 * Interface representing the constraint component.
 * Note: We apply here the Composite Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public interface ConstraintComponent {

    /**
     * Returns the solution of testing the constraint with the given requirements.
     *
     * @param requirements the requirements
     * @return true if the requirements meet the constraint, otherwise false
     */
    boolean solution(Map<ResourceType, Integer> requirements);

    /**
     * Returns a constraint from a given string.
     * @param string a string which represents a constraint.
     * @param manager a resource manager.
     * @return a constraint.
     * @throws IllegalArgumentException if the string does not represent a valid constraint.
     * @throws NumberFormatException if a number in the string cannot be parsed to an integer.
     */
    static ConstraintComponent parseConstraint(String string, ResourceManager manager) throws IllegalArgumentException, NumberFormatException {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Constraint cannot be empty!");
        }
        String[] substrings = string.split(" ");
        switch (substrings[0]) {
            case "&&":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new AndConstraint(parseConstraint(substrings[1], manager), parseConstraint(substrings[2], manager));
                }
            case "||":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new OrConstraint(parseConstraint(substrings[1], manager), parseConstraint(substrings[2], manager));
                }
            case "!":
                if (substrings.length != 2) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new NotConstraint(parseConstraint(substrings[1], manager));
                }
            case "if":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new IfThenConstraint(parseConstraint(substrings[1], manager), parseConstraint(substrings[2], manager));
                }
            case "==":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new Constraint(manager.getResourceType(substrings[1]), AmountComparator.EQUALS, Integer.parseInt(substrings[2]));
                }
            case "!=":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new Constraint(manager.getResourceType(substrings[1]), AmountComparator.NOT_EQUALS, Integer.parseInt(substrings[2]));
                }
            case ">":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new Constraint(manager.getResourceType(substrings[1]), AmountComparator.GREATER_THAN, Integer.parseInt(substrings[2]));
                }
            case ">=":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new Constraint(manager.getResourceType(substrings[1]), AmountComparator.GREATER_THAN_OR_EQUALS, Integer.parseInt(substrings[2]));
                }
            case "<":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new Constraint(manager.getResourceType(substrings[1]), AmountComparator.SMALLER_THAN, Integer.parseInt(substrings[2]));
                }
            case "<=":
                if (substrings.length != 3) {
                    throw new IllegalArgumentException(substrings[0] + " cannot be applied to " + (substrings.length - 1) + " arguments!");
                } else {
                    return new Constraint(manager.getResourceType(substrings[1]), AmountComparator.SMALLER_THAN_OR_EQUALS, Integer.parseInt(substrings[2]));
                }
            default:
                throw new IllegalArgumentException("Invalid constraint operator:" + substrings[0]);
        }
    }

}
