package taskman.backend.constraint;

import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * Interface representing the constraint component.
 * Note: We apply here the Composite Pattern
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
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
        String[] parts = string.split(" ");
        Deque<String> argumentStack = new ArrayDeque<>();
        Deque<ConstraintComponent> constraintStack = new ArrayDeque<>();
        for (int i = parts.length - 1; i >= 0; i--) {
            switch (parts[i]) {
                case "and":
                    if (constraintStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + constraintStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new AndConstraint(constraintStack.pop(), constraintStack.pop()));
                    }
                    break;
                case "or":
                    if (constraintStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + constraintStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new OrConstraint(constraintStack.pop(), constraintStack.pop()));
                    }
                    break;
                case "not":
                    if (constraintStack.size() < 1) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + constraintStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new NotConstraint(constraintStack.pop()));
                    }
                    break;
                case "if":
                    if (constraintStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + constraintStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new IfThenConstraint(constraintStack.pop(), constraintStack.pop()));
                    }
                    break;
                case "==":
                    if (argumentStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + argumentStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new Constraint(
                                manager.getResourceType(argumentStack.pop()),
                                AmountComparator.EQUALS,
                                Integer.parseInt(argumentStack.pop())
                        ));
                    }
                    break;
                case "!=":
                    if (argumentStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + argumentStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new Constraint(
                                manager.getResourceType(argumentStack.pop()),
                                AmountComparator.NOT_EQUALS,
                                Integer.parseInt(argumentStack.pop())
                        ));
                    }
                    break;
                case ">":
                    if (argumentStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + argumentStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new Constraint(
                                manager.getResourceType(argumentStack.pop()),
                                AmountComparator.GREATER_THAN,
                                Integer.parseInt(argumentStack.pop())
                        ));
                    }
                    break;
                case ">=":
                    if (argumentStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + argumentStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new Constraint(
                                manager.getResourceType(argumentStack.pop()),
                                AmountComparator.GREATER_THAN_OR_EQUALS,
                                Integer.parseInt(argumentStack.pop())
                        ));
                    }
                    break;
                case "<":
                    if (argumentStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + argumentStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new Constraint(
                                manager.getResourceType(argumentStack.pop()),
                                AmountComparator.SMALLER_THAN,
                                Integer.parseInt(argumentStack.pop())
                        ));
                    }
                    break;
                case "<=":
                    if (argumentStack.size() < 2) {
                        throw new IllegalArgumentException(parts[i] + " cannot be applied to " + argumentStack.size() + " arguments!");
                    } else {
                        constraintStack.push(new Constraint(
                                manager.getResourceType(argumentStack.pop()),
                                AmountComparator.SMALLER_THAN_OR_EQUALS,
                                Integer.parseInt(argumentStack.pop())
                        ));
                    }
                    break;
                default:
                    argumentStack.push(parts[i]);
                    break;
            }
        }
        if (argumentStack.size() == 0 && constraintStack.size() == 1) {
            return constraintStack.pop();
        } else {
            throw new IllegalArgumentException("Invalid constraint expression!");
        }
    }

}
