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

    public List<Resource> getAvailableResources(Task task, LocalDateTime startTime){
        Map<ResourceType, Integer> requirements = task.getRequirements();
        long duration = task.getEstimatedDuration();
        TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(duration));
        ArrayList<Resource> resources = new ArrayList<>();
        for (ResourceType resourceType : requirements.keySet()){
            resources.addAll(resourceType.getAvailableResources(timeSpan));
        }
        return resources;
    }

    public List<Resource> getAlternativeResources(Resource resource, Task task, LocalDateTime startTime){
        long duration = task.getEstimatedDuration();
        TimeSpan timeSpan = new TimeSpan(startTime, startTime.plusMinutes(duration));
        // TODO zorgen dat resource er wel niet bij zit
        return resource.getType().getAvailableResources(timeSpan);
    }

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


    private boolean checkRequirements(Map<ResourceType, Integer> requirements) {
        // TODO
        return false;
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
