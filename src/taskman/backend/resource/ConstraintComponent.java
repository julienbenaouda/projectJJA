package taskman.backend.resource;

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
}
