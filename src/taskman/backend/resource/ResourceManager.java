package taskman.backend.resource;

import taskman.backend.task.Task;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.Developer;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * This class is responsible for creating, storing and retrieving resources of the system.
 *
 * @author Jeroen Van Der Donckt
 */
public class ResourceManager {

    /**
     * Construct an empty resource manager.
     *
     * @post the set of resource types is set to a new HashsSt and the list of constraints is set to a new Arraylist
     */
    public ResourceManager() {
        this.resourceTypes = new HashSet<>();
        this.constraints = new ArrayList<>();
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


    /**
     * Represents the list of constraints for the resource types in the system.
     */
    private List<ConstraintComponent> constraints;

    /**
     * Returns the constraints for the resource types.
     *
     * @return the list of constraints for the resource types.
     */
    private List<ConstraintComponent> getConstraint(){
        return constraints;
    }

    // TODO: moet onderstaande methode er wel bij? Kan mss via constructor geïnitaliseerd worden
    /**
     * Adds the given constraint to the list of constraints.
     *
     * @param constraint the constraint to add to the list
     * @post the given constraint is added to the list of constraints
     */
    public void addConstraint(ConstraintComponent constraint){
        constraints.add(constraint);
    }


    /**
     * Returns a list of available resources for the given resource type at the given startTime for the given task.
     *
     * @param task the task to get the available resources for.
     * @param startTime the start time on which the resources needs to be planned.
     * @return a list of available resources for the given resource type at the given startTime for the given task.
     */
    public List<Resource> getAvailableResources(Task task, LocalDateTime startTime){
        Map<ResourceType, Integer> requirements = task.getRequirements();
        long duration = task.getEstimatedDuration();
        TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(duration));
        List<Resource> availableResources = new ArrayList<>();
        for (ResourceType resourceType : requirements.keySet()){
            availableResources.addAll(resourceType.getAvailableResources(timeSpan));
        }
        return availableResources;
    }

    /**
     * Returns a list of resources as alternatives for the given resource and the given task at the given time.
     *
     * @param resource the resource to get a list of alternatives for
     * @param task the task to get a list of alternatives for
     * @param startTime the start time on which the alternative resources will be planned
     * @return a list of resources as alternatives for the given resource and the given task at the given time.
     */
    public List<Resource> getAlternativeResources(Resource resource, Task task, LocalDateTime startTime){
        long duration = task.getEstimatedDuration();
        TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(duration));
        // TODO zorgen dat resource er wel niet bij zit
        return resource.getType().getAvailableResources(timeSpan);
    }


    /**
     * Returns an iterator of the starting times (on or after the given time) for the given task.
     *
     * @param task the task to get the starting times for
     * @param startTime the time on or before the starting times
     * @return an iterator of starting times (on or after the given time) for the given task
     * @throws NoSuchElementException if there is no next element in the iterator
     */
    public Iterator<LocalDateTime> getStartingTimes(Task task, LocalDateTime startTime) throws NoSuchElementException {
        // TODO: zorgen dat dit een iterator returnt
        Iterator<LocalDateTime> startingTimes = new Iterator<LocalDateTime>() {

            LocalDateTime startingTime = startTime.truncatedTo(ChronoUnit.HOURS);

            @Override
            public boolean hasNext() {
                Map<ResourceType, Integer> requirements = task.getRequirements();
                boolean enoughResources = true;
                for (ResourceType resourceType : requirements.keySet()){
                    if (resourceType.getNbOfResources() < requirements.get(resourceType)){
                        enoughResources = false;
                    }
                }

                long duration = task.getEstimatedDuration();
                boolean hasAvailablePeriod = false;
                for (int day = 1; day <= 7; day++){
                    AvailabilityPeriod availabilityPeriod = new AvailabilityPeriod(LocalTime.MIN, LocalTime.MAX);
                    for (ResourceType resourceType : requirements.keySet()){
                        LocalTime startTime;
                        LocalTime endTime;
                        if (availabilityPeriod.getStartTime().isBefore(resourceType.getAvailabilityPeriod(day).getStartTime())){
                            startTime = resourceType.getAvailabilityPeriod(day).getStartTime();
                        }
                        else{
                            startTime = availabilityPeriod.getStartTime();
                        }
                        if (availabilityPeriod.getEndTime().isAfter(resourceType.getAvailabilityPeriod(day).getEndTime())){
                            endTime = resourceType.getAvailabilityPeriod(day).getEndTime();
                        }
                        else{
                            endTime = availabilityPeriod.getEndTime();
                        }
                        availabilityPeriod = new AvailabilityPeriod(startTime, endTime);
                    }
                    if (availabilityPeriod.getStartTime().truncatedTo(ChronoUnit.HOURS).plus(duration, ChronoUnit.MINUTES).isBefore(availabilityPeriod.getEndTime())){
                        hasAvailablePeriod = true;
                    }
                }

                return enoughResources && hasAvailablePeriod;
            }

            @Override
            public LocalDateTime next() {
                if (hasNext()) {
                    while (!isAvailableStartingTime(task, startingTime)) {
                        startingTime = startingTime.plusHours(1);
                    }
                    return startingTime;
                }
                throw new NoSuchElementException("There is no starting time available.");
            }
        };
        return startingTimes;
    }

    /**
     * Returns if the given time is an available starting time for the given task.
     *
     * @param task the task to check the starting time for
     * @param startTime the starting time to check
     * @return true if the given time is available for the given task, otherwise false
     */
    private boolean isAvailableStartingTime(Task task, LocalDateTime startTime){ // TODO: mss beter om niet task door te geven maar de zaken die nodig zijn van task
        Map<ResourceType, Integer> requirements = task.getRequirements();
        long duration = task.getEstimatedDuration();
        TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(duration));
        for (ResourceType resourceType : requirements.keySet()){
            if (!resourceType.hasAvailableResources(timeSpan, requirements.get(resourceType))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks the given requirements with the constraints of the system.
     *
     * @param requirements the requirements to check the systems constraints with
     * @return true if there is no conflict between the system its constraints and the requirements, otherwise false
     */
    private boolean checkRequirements(Map<ResourceType, Integer> requirements) {
        for (ConstraintComponent constraint : getConstraint()){
            if (!constraint.solution(requirements)){
                return false;
            }
        }
        return true;
    }


    /**
     * Plans the given resources for the given task at the given start time.
     *
     * @param task the task to plan the resources for
     * @param resources the resource to plan
     * @param startTime the starting time to plan the resources at
     * @throws IllegalArgumentException the requirements of the task violate the system its constraints
     * @post the resources are planned for the given task at a newly created time span (based on the start time)
     */
    public void plan(Task task, List<Resource> resources, LocalDateTime startTime) throws IllegalArgumentException {
        // TODO
        Map<ResourceType, Integer> requirements = task.getRequirements();
        long duration = task.getEstimatedDuration();
        TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(duration));
        if (checkRequirements(requirements)){
            for (Resource resource : resources){
                resource.createReservation(task, timeSpan);
            }
        }
        throw new IllegalArgumentException("The requirements of the task do not meet the system its constraints.");
        // TODO is het niet beter om deze requirements vroeger te checken
    }


    /**
     * Creates a new resource from the given user.
     *
     * @param user the user to use for the resource creation
     * @throws IllegalArgumentException the break is null.
     * @post a new user resource is created and the resource is added to the user
     */
    public void createResourceForUser(User user, LocalTime startBreak) throws IllegalArgumentException {
    	if(user.getUserType().equals("developer")) {
            if(startBreak == null) {
                throw new IllegalArgumentException("A user must take a break");
            }
            DeveloperResource r = new DeveloperResource(getResourceType("developer"), startBreak);
            Developer d = (Developer) user;
            d.changeResource(r);
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
            getResourceType("developer").removeResource(d.getResource());
        }
    }
}
