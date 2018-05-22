package taskman.backend.constraint;

import taskman.backend.resource.ResourceManager;

/**
 * This class is responsible for converting from and to constraint objects.
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
        return ConstraintComponent.parseConstraint(string, resourceManager);
    }
}
