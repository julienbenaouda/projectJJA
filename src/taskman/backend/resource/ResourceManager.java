package taskman.backend.resource;

import taskman.backend.task.Task;
import taskman.backend.user.Developer;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * This class is responsible for creating, storing and retrieving resources of the system.
 *
 * @author Jeroen Van Der Donckt
 */
public class ResourceManager {

    /**
     * Construct an empty resource manager.
     */
    public ResourceManager() {
        this.resourceTypes = new HashSet<>();
        addResourceType("developer");
    }

    /**
     * Set representing the existing resource types in the system.
     */
    private Set<ResourceType> resourceTypes;

    /**
     * Returns the resource type with the given name.
     *
     * @param name the name of the resource type
     * @return the resource type with the given name
     * @throws IllegalArgumentException if there exists no resource type with the given name
     */
    public ResourceType getResourceType(String name) throws IllegalArgumentException{
        Iterator<ResourceType> iterator = resourceTypes.iterator();
        while (iterator.hasNext()){
            ResourceType resourceType = iterator.next();
            if (resourceType.getName().equals(name)){
                return resourceType;
            }
        }
        throw new IllegalArgumentException("There exists no resource type with the given name.");
    }

    /**
     * Creates and adds the resource type with the given name to the resource types.
     *
     * @param name the name of the resource type
     * @post a resource type with given name is created and added to the resource types
     */
    public void addResourceType(String name){
        ResourceType resourceType = new ResourceType(name);
        resourceTypes.add(resourceType); // If there exists already a resource type with the given
    }

    private boolean checkRequirements(Map<ResourceType, Integer> requirements) {
        // TODO
        return true;
    }

    public void plan(Task task, List<Resource> resources, LocalDateTime startTime){
        // TODO
        Map<ResourceType, Integer> requirements = task.getRequirements();
        long duration = task.getEstimatedDuration();
        if (checkRequirements(requirements)){

        }
        // TODO
    }
    
    /**
     * creates a new resource from the given user
     * @param user the user to use for the resource creation
     * @throws IllegalArgumentException the break is null
     */
    public void createResourceForUser(User user, LocalTime startBreak) throws IllegalArgumentException {
    	if(user.isProjectManager()) {
    			if(startBreak == null)
    			{
    				throw new IllegalArgumentException("A user must take a break");
    			}
    			DeveloperResource r = new DeveloperResource(getResourceType("developer"), startBreak);
    			Developer d = (Developer)user;
    			d.changeResource(r);
    	}
    }
}
