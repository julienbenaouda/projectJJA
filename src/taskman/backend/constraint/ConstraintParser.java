package taskman.backend.constraint;

import taskman.backend.constraint.amount.*;
import taskman.backend.constraint.constraint.*;
import taskman.backend.resource.ResourceManager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * This class is responsible for converting from strings to constraint objects.
 *
 * @author Alexander Braekevelt, Jeroen Van Der Donckt
 */
public class ConstraintParser {

    /**
     * Returns the constraint corresponding to the given string.
     * @param string the string to parse the constraint from.
     * @param resourceManager the resource manager.
     * @return the constraint parsed from the given string.
     * @throws IllegalArgumentException if the string does not represent a valid constraint.
     */
    public static Constraint parse(String string, ResourceManager resourceManager){
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Constraint cannot be empty!");
        }
        String[] parts = string.split(" ");
        Deque<Amount> amountStack = new ArrayDeque<>();
        Deque<Constraint> constraintStack = new ArrayDeque<>();
        for (int i = parts.length - 1; i >= 0; i--) {
            try {
                Amount a;
                Amount b;
                switch (parts[i]) {
                    case "and":
                        constraintStack.push(new Not(new Or(new Not(constraintStack.pop()), new Not(constraintStack.pop()))));
                        break;
                    case "or":
                        constraintStack.push(new Or(constraintStack.pop(), constraintStack.pop()));
                        break;
                    case "not":
                        constraintStack.push(new Not(constraintStack.pop()));
                        break;
                    case "==":
                        constraintStack.push(new Equals(amountStack.pop(), amountStack.pop()));
                        break;
                    case "!=":
                        constraintStack.push(new Not(new Equals(amountStack.pop(), amountStack.pop())));
                        break;
                    case "<":
                        constraintStack.push(new Smaller(amountStack.pop(), amountStack.pop()));
                        break;
                    case "<=":
                        a = amountStack.pop();
                        b = amountStack.pop();
                        constraintStack.push(new Or(new Smaller(a, b), new Equals(a, b)));
                        break;
                    case ">":
                        a = amountStack.pop();
                        b = amountStack.pop();
                        constraintStack.push(new Not(new Or(new Smaller(a, b), new Equals(a, b))));
                        break;
                    case ">=":
                        constraintStack.push(new Not(new Smaller(amountStack.pop(), amountStack.pop())));
                        break;
                    case "+":
                        amountStack.push(new Plus(amountStack.pop(), amountStack.pop()));
                        break;
                    case "-":
                        amountStack.push(new Minus(amountStack.pop(), amountStack.pop()));
                        break;
                    default:
                        if (parts[i].chars().allMatch(Character::isDigit)) {
                            amountStack.push(new Constant(Integer.parseInt(parts[i])));
                        } else {
                            amountStack.push(new Type(resourceManager.getResourceType(parts[i])));
                        }
                        break;
                }
            } catch (NoSuchElementException e) {
                throw new IllegalArgumentException("Invalid constraint expression!");
            }
        }
        if (amountStack.size() == 0 && constraintStack.size() == 1) {
            return constraintStack.pop();
        } else {
            throw new IllegalArgumentException("Invalid constraint expression!");
        }
    }
}
