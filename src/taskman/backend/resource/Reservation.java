package taskman.backend.resource;

import taskman.backend.task.Task;
import taskman.backend.time.TimeSpan;

import java.time.LocalDateTime;

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
     * @param startTime the start time of the reservation
     * @param endTime the end time of the reservation
     * @post a new reservation is created with given attributes
     */
    public Reservation(Task task, Resource resource, TimeSpan timeSpan){
        setTask(task);
        setResource(resource);
        setTimeSpan(startTime, endTime);
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
    // TODO: dit de verantwoordelijkheid geven over de bidirectioneele verbinding

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
     * Creates a time span and sets the time span of the reservation to the new created time span.
     *
     * @param startTime the start time of the reservation its time span
     * @param endTime the end time of the reservation its time span
     * @post a new time span is created with given attributes and the time span of the reservation is set to this time span
     */
    private void setTimeSpan(TimeSpan timeSpan){
        this.timeSpan = timeSpan;
    }
    
    /**
     * Checks if this reservation overlaps with the given time span
     * @param timeSpan the timespan to check with
     * @return true if the start time of the timespan is between the start-and end time of this reservation, if the end time of the time span is between the start and end time of this reservation or if the start-and end time of this reservation are between the start and end time of the given time span
     */
    public boolean overlaps(TimeSpan timeSpan) {
    	if(timeSpan.getStartTime().isAfter(getTimeSpan().getStartTime()) && timeSpan.getStartTime().isBefore(getTimeSpan().getEndTime())) {
    		return true;
    	}
    	if(timeSpan.getEndTime().isAfter(getTimeSpan().getStartTime()) && timeSpan.getEndTime().isBefore(getTimeSpan().getEndTime())) {
    		return true;
    	}
    	if(timeSpan.getStartTime().isBefore(getTimeSpan().getStartTime()) && timeSpan.getEndTime().isAfter(getTimeSpan().getEndTime())) {
    		return true;
    	}
    	return false;
    }
}
