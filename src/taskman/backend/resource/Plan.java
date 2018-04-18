package taskman.backend.resource;

import taskman.backend.task.Task;
import taskman.backend.user.Developer;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Class representing a plan.
 *
 * @author Jeroen Van Der Donckt
 */
public class Plan {

    /**
     * Creates a new plan with given task.
     *
     * @param task the task where this plan is made for
     * @post a new plan is created for the given task, requirements is set to empty HashMap, reservations is set to empty ArrayList
     */
    public Plan(Task task){
        setTask(task);
        requirements = new HashMap<>();
        reservations = new ArrayList<>();
    }


    /**
     * Represents the task of the plan.
     */
    private Task task;

    /**
     * Returns the task of the plan.
     *
     * @return the task of the plan
     */
    public Task getTask(){
        return  task;
    }

    /**
     * Sets the task of the plan to the given task.
     *
     * @param task the new task of the plan
     * @post the task of the plan is set to the given task
     */
    private void setTask(Task task){
        this.task = task;
    }


    /**
     * Represents the requirements of the plan.
     */
    private Map<ResourceType, Integer> requirements;

    /**
     * Returns the requirements of the plan.
     *
     * @return the requirements of the plan
     */
    public Map<ResourceType, Integer> getRequirements(){
        return  new HashMap<>(requirements);
    }

    /**
     * Adds the given requirement to the requirements.
     *
     * @param resourceType the resource type of the requirement
     * @param amount the amount of the requirement
     * @post the given requirement is added to the requirements
     */
    public void addRequirement(ResourceType resourceType, int amount){
        requirements.put(resourceType, amount);
    }


    /**
     * Represents the reservations of the plan.
     */
    private List<Reservation> reservations;

    /**
     * Returns the reservations of the plan.
     *
     * @return the reservations of the plan
     */
    public List<Reservation> getReservations(){
        return new ArrayList<>(reservations);
    }

    /**
     * Creates reservations for all the given resources at the given start time
     *
     * @param resources the list of resources to create a reservation for
     * @param startTime the start time for the reservations
     * @post for each resource a reservation is created and added to the reservations of the plan
     */
    public void createReservation(List<Resource> resources, LocalDateTime startTime) {
        LocalDateTime endTime = startTime.plusMinutes(getTask().getEstimatedDuration());
        for (Resource resource : resources){
            addReservation(resource, startTime, endTime);
        }
    }

    /**
     * Creates a reservation with the given attributes and adds its to the reservations of the plan.
     *
     * @param resource the resource of the reservation
     * @param startTime the start time of the reservation
     * @param endTime the end time of the reservation
     * @post a reservation with the given attributes is created and added to the
     *       reservations of the plan
     */
    private void addReservation(Resource resource, LocalDateTime startTime, LocalDateTime endTime){
        Reservation reservation = new Reservation(resource, startTime, endTime);
        reservations.add(reservation);
    }

    /**
     * Removes the given reservation from the plan its reservations.
     *
     * @param reservation the reservation to remove
     * @post the given reservation is removed from the plan its reservations
     */
    private void removeReservation(Reservation reservation){
        reservations.remove(reservation);
    }


    /**
     * Changes the reservation from the old resource to the reservation of the new resource.
     *
     * @param oldResource
     * @param newResource
     * @throws IllegalArgumentException
     * @post the reservation for the old resource is deleted (also from the plan its reservations)
     *       and for the new resource a reservation is created and added to the reservations of the plan
     */
    public void changeResource(Resource oldResource, Resource newResource) throws IllegalArgumentException {
        int index = 0;
        boolean changed = false;
        while (!changed && index < getReservations().size()){
            Reservation reservation = getReservations().get(index);
            if (reservation.getResource() == oldResource){
                LocalDateTime startTime = reservation.getTimeSpan().getStartTime();
                LocalDateTime endTime = reservation.getTimeSpan().getEndTime();
                reservation.delete();
                removeReservation(reservation);
                // TODO: die boolean changable nog doen
                addReservation(newResource, startTime, endTime);
                changed = true;
            }
            index += 1;
        }
        if (!changed) {
            throw new IllegalArgumentException("There is no reservation in the plan for the given resource.");
        }
    }


    /**
     * Returns if the user is a developer used in the plan.
     *
     * @param user the user we want to check
     * @return true if the user is a developer for the plan, otherwise false
     */
    public boolean isDeveloperFromPlan(User user){
        if (!(user instanceof Developer)){
            return false;
        }
        Developer developer = (Developer) user;
        for (Reservation reservation : getReservations()){
            if (reservation.getResource() instanceof DeveloperResource){
                if (((DeveloperResource)reservation.getResource()).getDeveloper() == developer){
                        return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 
     * @param resourceManager the resourceManager to use to replan
     * @param startTime the start time of the task
     * @param duration the duration of the task
     * @post if the start time is before the foreseen start time, a new plan is generated for the task
     * @throws IllegalArgumentException when not enough resources are available to make a new plan for the given time
     */
    public void makeExecuting(ResourceManager resourceManager, LocalDateTime startTime, Long duration) {
    	if(startsEarlier(startTime)) {
    		if(resourceManager.getStartingTimes(this, startTime, duration).next().isAfter(startTime)) {
    			throw new IllegalArgumentException("Execution of this task can't start at the given start time because there are no resources available.");
    		} else {
    			resourceManager.planBySystem(this, resourceManager.getAvailableResources(this, startTime, duration), startTime);
    		}
    	}
    }
    
    /**
     * finishes the reservations if needed
     * @param endTime the end tiem when the reservation should be finished
     * @post if the end time is earlier then the foreseen endtime, the resources are set to available for the remaining time
     */
    public void finish(LocalDateTime endTime) {
    	if(endTime.isBefore(getReservations().get(0).getTimeSpan().getEndTime())) {
    		for(Reservation r: getReservations()) {
    			r.finishEarlier(endTime);
    		}
    	}
    }

	private boolean startsEarlier(LocalDateTime startTime) {
		return startTime.isBefore(getReservations().get(0).getTimeSpan().getStartTime());
	}


}
