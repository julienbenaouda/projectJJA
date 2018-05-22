package taskman.backend.constraint;

import taskman.backend.resource.ResourceManager;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class is responsible for converting from and to constraint objects.
 *
 * @author Jeroen Van Der Donckt
 */
public class ConstraintParser {

    /**
     * Returns the constraint corresponding to the given string
     * @param string the string to parse the constraint from
     * @param resourceManager the resource manager
     * @return the constraint parsed from the given string
     * @throws IllegalArgumentException if the string does not represent a valid constraint
     * @throws NumberFormatException if a number in the string cannot be parsed to an integer
     */
    public static ConstraintComponent toConstraint(String string, ResourceManager resourceManager){
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
                                resourceManager.getResourceType(argumentStack.pop()),
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
                                resourceManager.getResourceType(argumentStack.pop()),
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
                                resourceManager.getResourceType(argumentStack.pop()),
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
                                resourceManager.getResourceType(argumentStack.pop()),
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
                                resourceManager.getResourceType(argumentStack.pop()),
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
                                resourceManager.getResourceType(argumentStack.pop()),
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
