package taskman.backend.resource;

import taskman.backend.Task.Task;
import taskman.backend.Time.TimeSpan;

/**
 * This class represents a reservation.
 *
 * @author Jeroen Van Der Donckt
 */
public class Reservation {

    /**
     * Creates a new reservation with the given values.
     *
     * @param task the task of the reservation
     * @param resource the resource of the reservation
     * @param timeSpan the time span of the reservation
     * @post a new reservation is created with given attributes
     */
    public Reservation(Task task, Resource resource, TimeSpan timeSpan){
        setTask(task);
        setResource(resource);
        setTimeSpan(timeSpan);
    }


    /**
     * The task of the reservation.
     */
    private Task task;

    /**
     * Returns the task of the reservation.
     *
     * @return the task of the reservation
     */
    public Task getTask(){
        return task;
    }

    /**
     * Sets the task of the reservation to the given task.
     *
     * @param task the task of the reservation
     * @post the task of the reservation is set to the given task
     */
    private void setTask(Task task){
        this.task = task;
    }


    /**
     * The resource of the reservation.
     */
    private Resource resource;

    /**
     * Returns the resource of the reservation.
     *
     * @return the resource of the reservation
     */
    public Resource getResource(){
        return resource;
    }

    /**
     * Sets the resource of te reservation to the given resource.
     *
     * @param resource the resource of the reservation
     * @post the resource of the reservation is set to the given resource
     */
    private void setResource(Resource resource){
        this.resource = resource;
    }


    /**
     * The time span of the reservation.
     */
    private TimeSpan timeSpan;

    /**
     * Returns the time span of the reservation.
     *
     * @return the time span of the reservation
     */
    public TimeSpan getTimeSpan() {
        return timeSpan;
    }

    /**
     * Sets the time span of the reservation to the given time span.
     *
     * @param timeSpan the time span of the reservation
     * @post the time span of the reservation is set to the given time span
     */
    private void setTimeSpan(TimeSpan timeSpan){
        this.timeSpan = timeSpan;
    }
}
