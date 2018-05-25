package taskman.backend.resource;

import taskman.backend.constraint.constraint.Constraint;
import taskman.backend.time.TimeParser;
import taskman.backend.user.Developer;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * This class is responsible for creating, storing and retrieving resources of the system.
 * The resource manager keeps a list of all resource types and constraints. 
 * A resource manager can check if a plans requirements can be satisfied, create an initial plan for a task, check if a planned task can be rescheduled, collect the earliest starting times for a plan
 * @author Jeroen Van Der Donckt, Julien Benaouda
 */
public class ResourceManager {

    /**
     * Construct an empty resource manager.
     * @post the set of resource types is set to a new HashsSet and the list of constraints is set to a new Arraylist
     */
    public ResourceManager() {
        this.resourceTypes = new HashSet<>();
        this.constraints = new ArrayList<>();
        createResourceType("developer"); // This will add developer as a resource type
    }


    /**
     * Set representing the existing resource types in the system.
     */
    private Set<ResourceType> resourceTypes;

    /**
     * Returns the resource type with the given name.
     * @param name the name of the resource type
     * @return the resource type with the given name
     * @throws NoSuchElementException if there exists no resource type with the given name
     */
    public ResourceType getResourceType(String name) throws NoSuchElementException {
        Iterator<ResourceType> iterator = resourceTypes.iterator();
        while (iterator.hasNext()){
            ResourceType resourceType = iterator.next();
            if (resourceType.getName().equals(name)){
                return resourceType;
            }
        }
        throw new NoSuchElementException("There exists no resource type with the given name.");
    }

    /**
     * Returns a list of the resource types.
     * @return a list of the resource types.
     */
    public List<ResourceType> getResourceTypes() {
        return new ArrayList<>(this.resourceTypes);
    }

    /**
     * Creates and adds the resource type with the given name to the resource types.
     *     * @param name the name of the resource type
     * @post a resource type with given name is created and added to the resource types
     * @throws IllegalArgumentException when the name contains spaces
     */
    public void createResourceType(String name){
    	if(name.contains(" ")) {
    		throw new IllegalArgumentException("a name for a resource type can't contain spaces");
    	}
        ResourceType resourceType = new ResourceType(name);
        resourceTypes.add(resourceType); // If there exists already a resource type with the given
    }


    /**
     * Represents the list of constraints for the resource types in the system.
     */
    private List<Constraint> constraints;

    /**
     * Returns the constraints for the resource types.
     * @return the list of constraints for the resource types
     */
    private List<Constraint> getConstraint(){
        return constraints;
    }

    /**
     * Adds the given constraint to the list of constraints.
     * @param constraint the constraint to add to the list
     * @post the given constraint is added to the list of constraints
     */
    public void addConstraint(Constraint constraint){
        constraints.add(constraint);
    }

    /**
     * Returns an iterator of the starting times (on or after the given time) for the given task.
     * @param plan the plan to get the starting times for
     * @param duration the duration of the reservations
     * @param startTime the time on or before the starting times
     * @return an iterator of starting times (on or after the given time) for the given task
     * @throws NoSuchElementException if there is no next element in the iterator
     */
    public Iterator<LocalDateTime> getStartingTimes(Plan plan, long duration, LocalDateTime startTime) throws NoSuchElementException {
        return new Iterator<LocalDateTime>() {
            LocalDateTime startingTime = TimeParser.roundUpLocalDateTime(startTime);

            @Override
            public boolean hasNext() {
                Map<ResourceType, Integer> requirements = plan.getRequirements();
                if (requirements.keySet().stream().anyMatch(t -> t.getNbOfResources() < requirements.get(t))) {
                    return false;
                }

                boolean hasAvailablePeriod = false;
                for (int day = 1; day <= 7; day++){
                    LocalTime startTime = LocalTime.MIN;
                    LocalTime endTime = LocalTime.MAX;
                    for (ResourceType resourceType : requirements.keySet()){
                        if (startTime.isBefore(resourceType.getAvailabilityPeriod(day).getStartTime())){
                            startTime = resourceType.getAvailabilityPeriod(day).getStartTime();
                        }
                        if (endTime.isAfter(resourceType.getAvailabilityPeriod(day).getEndTime())){
                            endTime = resourceType.getAvailabilityPeriod(day).getEndTime();
                        }
                    }
                    if (TimeParser.roundUpLocalTime(startTime).plusMinutes(duration).isBefore(endTime)){
                        hasAvailablePeriod = true;
                    }
                }

                return hasAvailablePeriod;
            }

            @Override
            public LocalDateTime next() {
                if (hasNext()) {
                    while (!plan.isAvailableStartingTime(duration, startingTime)) {
                        startingTime = TimeParser.roundUpLocalDateTime(startingTime.plusHours(1));
                    }
                    LocalDateTime prevStartingTime = startingTime;
                    startingTime = TimeParser.roundUpLocalDateTime(startingTime.plusHours(1));
                    return prevStartingTime;
                }
                throw new NoSuchElementException("There is no starting time available.");
            }
        };
    }


    /**
     * Checks the given requirements with the constraints of the system.
     * @param requirements the requirements to check the systems constraints with
     * @return true if there is no conflict between the system its constraints and the requirements, otherwise false
     */
    public boolean checkRequirements(Map<ResourceType, Integer> requirements) {
        for(ResourceType type: requirements.keySet()) {
        	if(requirements.get(type).intValue() > getResourceType(type.getName()).getNbOfResources()) {
        		return false;
        	}
        }
        for (Constraint constraint : getConstraint()){
            if (!constraint.evaluate(requirements)){
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the given requirement to the plan of the corresponding task.
     * @param plan the task to add the requirement to
     * @param resourceType the resource type of the requirement
     * @param amount the amount of the requirement
     */
    public void addRequirement(Plan plan, ResourceType resourceType, int amount){
        Map<ResourceType, Integer> requirementsCopy = plan.getRequirements();
        requirementsCopy.put(resourceType, amount);
        if (checkRequirements(requirementsCopy)){
            plan.addRequirement(resourceType, amount);
        } else {
            throw new IllegalArgumentException("The requirements of the task do not meet the system its constraints.");
        }
    }

    /**
     * Creates a new resource from the given user.
     * @param user the user to use for the resource creation
     * @throws IllegalArgumentException the break is null.
     * @post a new user resource is created and the resource is added to the user
     */
    public void createResourceForUser(User user, LocalTime startBreak) throws IllegalArgumentException {
    	if(user.getUserType().equals("developer")) {
            if(startBreak == null) {
                throw new IllegalArgumentException("A user must take a break");
            }
            Developer d = (Developer) user;
            getResourceType("developer").createResourceFromUser(d.getName(), startBreak, d);
    	}
    }

    /**
     * Removes the resource of the given user.
     *
     * @param user the user to use for the resource removal
     * @post the user resource is removed
     * @throws IllegalStateException if the resource cannot be removed.
     */
    public void removeResourceForUser(User user) {
        if (user.getUserType().equals("developer")) {
            Developer d = (Developer) user;
            getResourceType("developer").removeResource(getResourceType("developer").getResource(user.getName()));
        }
    }

}
