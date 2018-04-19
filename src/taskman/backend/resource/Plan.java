package taskman.backend.resource;

import taskman.backend.task.Task;
import taskman.backend.user.Developer;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Task getTask() {
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
    public void createReservations(List<Resource> resources, LocalDateTime startTime) {
        LocalDateTime endTime = startTime.plusMinutes(getTask().getEstimatedDuration());
        for (Resource resource : resources){
            createReservation(resource, startTime, endTime);
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
    public void createReservation(Resource resource, LocalDateTime startTime, LocalDateTime endTime){
        Reservation reservation = new Reservation(resource, startTime, endTime);
        reservations.add(reservation);
    }

    /**
     * Creates a specific reservation with the given attributes and adds it to the reservations of the plan.
     * The reservation is also set to an user specific state.
     *
     * @param resource the resource of the user specific reservation
     * @param startTime the start time of the user specific reservation
     * @param endTime the end time of the user specific reservation
     */
    public void createSpecificReservation(Resource resource, LocalDateTime startTime, LocalDateTime endTime){
        Reservation reservation = new Reservation(resource, startTime, endTime);
        reservation.setUserSpecific();
        reservations.add(reservation);
    }

    /**
     * Removes the given reservation from the plan its reservations.
     *
     * @param reservation the reservation to remove
     * @post the given reservation is removed from the plan its reservations
     */
    public void removeReservation(Reservation reservation){
        reservation.delete();
        reservations.remove(reservation);
    }

    /**
     * Get the resources of this plan.
     * @return a list of resources.
     */
    public List<Resource> getPlannedResources() {
        return this.reservations.stream().map(Reservation::getResource).collect(Collectors.toList());
    }

    /**
     * Changes the reservation from the old resource to the reservation of the new resource.
     *
     * @param oldResource the old resource to replace
     * @param newResource the new resource to add
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
                removeReservation(reservation);
                createSpecificReservation(newResource, startTime, endTime);
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
     * Finishes the reservations if needed.
     * @param endTime the end time when the reservation should be finished
     * @post if the end time is earlier then the foreseen endtime, the resources are set to available for the remaining time
     */
    public void finish(LocalDateTime endTime) {
    	if(endTime.isBefore(getReservations().get(0).getTimeSpan().getEndTime())) {
    		for(Reservation r: getReservations()) {
    			r.finishEarlier(endTime);
    		}
    	}
    }

    /**
     * Empties the plan.
     */
	public void emptyPlan() {
        this.reservations.clear();
    }


}
