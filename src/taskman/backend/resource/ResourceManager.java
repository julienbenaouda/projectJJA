package taskman.backend.resource;

import taskman.backend.constraint.ConstraintComponent;
import taskman.backend.time.TimeParser;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.Developer;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for creating, storing and retrieving resources of the system.
 *
 * @author Jeroen Van Der Donckt, Julien Benaouda
 */
public class ResourceManager {

    // TODO: duration en starttime samenvoegen in timespan!!!

    /**
     * Construct an empty resource manager.
     *
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
     *
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
     *
     * @param name the name of the resource type
     * @post a resource type with given name is created and added to the resource types
     */
    public void createResourceType(String name){
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
     * @return the list of constraints for the resource types
     */
    private List<ConstraintComponent> getConstraint(){
        return constraints;
    }

    /**
     * Adds the given constraint to the list of constraints.
     *
     * @param constraint the constraint to add to the list
     * @post the given constraint is added to the list of constraints
     */
    private void addConstraint(ConstraintComponent constraint){
        constraints.add(constraint);
    }

    /**
     * Creates a constraint from a given string.
     *
     * @param string a string which represents a constraint
     * @post adds a constraint to the resource manager
     * @throws IllegalArgumentException if the string does not represent a valid constraint
     * @throws NumberFormatException if a number in the string cannot be parsed to an integer
     */
    public void createConstraint(String string) {
        addConstraint(ConstraintComponent.parseConstraint(string, this));
    }

    /**
     * Returns an iterator of the starting times (on or after the given time) for the given task.
     *
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
                    while (!isAvailableStartingTime(plan, duration, startingTime)) {
                        startingTime = startingTime.plusHours(1);
                    }
                    return startingTime;
                }
                throw new NoSuchElementException("There is no starting time available.");
            }
        };
    }

    /**
     * Returns if the given time is an available starting time for the given task.
     *
     * @param plan the plan to check the starting time for
     * @param duration the duration of the reservations
     * @param startTime the starting time to check
     * @return true if the given time is available for the given task, otherwise false
     */
    private boolean isAvailableStartingTime(Plan plan, long duration, LocalDateTime startTime){ // TODO: mss beter om niet task door te geven maar de zaken die nodig zijn van task
        Map<ResourceType, Integer> requirements = plan.getRequirements();
        TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(duration));
        for (ResourceType resourceType : requirements.keySet()){
            if (!resourceType.hasAvailableResources(timeSpan, requirements.get(resourceType))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Tests a map of requirements on its correctness.
     *
     * @param requirements the requirements to test
     * @throws IllegalArgumentException when the requirements are not valid
     */
    public void testRequirements(Map<ResourceType, Integer> requirements) {
    	if(!checkRequirements(requirements)) {
    		throw new IllegalArgumentException("The list of requirements doesn't match the constraints.");
    	}
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
     * Adds the given requirement to the plan of the corresponding task.
     *
     * @param plan the task to add the requirement to
     * @param resourceType the resource type of the requirement
     * @param amount the amount of the requirement
     */
    public void addRequirement(Plan plan, ResourceType resourceType, int amount){
        Map<ResourceType, Integer> requirementsCopy = plan.getRequirements();
        requirementsCopy.put(resourceType, amount);
        if (checkRequirements(requirementsCopy)){ // TODO: moet deze check hier of bij Plan gebeuren?
            plan.addRequirement(resourceType, amount);
        } else {
            throw new IllegalArgumentException("The requirements of the task do not meet the system its constraints.");
        }
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
            // TODO: is dit wel goede code
        }
    }

    /**
     * Initializes a plan.
     * @param plan the plan to initialize.
     * @param duration the duration of the plan.
     * @param startTime the start time.
     */
    public void initializePlan(Plan plan, long duration, LocalDateTime startTime) {
        TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(duration));
        Map<ResourceType, Integer> requirements = plan.getRequirements();
        List<Resource> resources = new ArrayList<>();
        for(ResourceType type: requirements.keySet()) {
            resources.addAll(
                    type.getAvailableResources(timeSpan)
                            .stream()
                            .limit(requirements.get(type))
                            .collect(Collectors.toList())
            );
        }
        plan.createReservations(resources, startTime);
    }

    /**
     * Reschedule a plan to a given time span.
     * @param plan the plan.
     * @param newTimeSpan the new time span.
     * @throws IllegalArgumentException if the plan cannot be rescheduled.
     */
    public void reschedulePlan(Plan plan, TimeSpan newTimeSpan) throws IllegalArgumentException {
        if (!canBeRescheduled(plan, newTimeSpan)) throw new IllegalArgumentException("Plan cannot be rescheduled!");
        for (Reservation reservation : plan.getReservations()) {
            if (reservation.isUserSpecific()) {
                Resource resource = reservation.getResource();
                plan.removeReservation(reservation);
                plan.createSpecificReservation(resource, newTimeSpan.getStartTime(), newTimeSpan.getEndTime());
            } else {
                Resource resource = reservation.getResource();
                plan.removeReservation(reservation);
                if (!resource.isAvailable(newTimeSpan)) {
                    resource = getAlternativeResources(resource, newTimeSpan).get(0);
                }
                plan.createReservation(resource, newTimeSpan.getStartTime(), newTimeSpan.getEndTime());
            }
        }
    }

    /**
     * Returns if the planned task can be rescheduled.
     * @param plan a plan.
     * @param newTimeSpan the time span to check.
     * @return true if the plan can be rescheduled, otherwise false.
     */
    public boolean canBeRescheduled(Plan plan, TimeSpan newTimeSpan){
        for (Reservation reservation: plan.getReservations()) {
            if (reservation.isUserSpecific()) {
                Resource resource = reservation.getResource();
                plan.removeReservation(reservation);
                if (!resource.isAvailable(newTimeSpan)) {
                    return false;
                }
                plan.createSpecificReservation(resource, reservation.getTimeSpan().getStartTime(), reservation.getTimeSpan().getEndTime());
            } else {
                Resource resource = reservation.getResource();
                plan.removeReservation(reservation);
                if (!resource.isAvailable(newTimeSpan) && getAlternativeResources(resource, newTimeSpan).isEmpty()) {
                    return false;
                }
                plan.createReservation(resource, reservation.getTimeSpan().getStartTime(), reservation.getTimeSpan().getEndTime());
            }
        }
        return true;
    }

    /**
     * Returns a list of resources as alternatives for the given resource and the given task at the given time.
     * @param resource the resource to get a list of alternatives for
     * @param timeSpan the time span of the reservation time
     * @return a list of resources as alternatives for the given resource and the given task at the given time
     */
    public List<Resource> getAlternativeResources(Resource resource, TimeSpan timeSpan){
        List<Resource> r = resource.getType().getAvailableResources(timeSpan);
        r.remove(resource);
        return r;
    }
}
