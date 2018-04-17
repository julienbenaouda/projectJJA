package taskman.backend.resource;

import taskman.backend.task.Task;
import taskman.backend.user.Developer;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Reservation reservation = new Reservation(resource, startTime, endTime);
            reservations.add(reservation);
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


}
