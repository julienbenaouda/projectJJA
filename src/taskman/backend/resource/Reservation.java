package taskman.backend.resource;

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
     * @param resource the resource of the reservation
     * @param startTime the start time of the reservation
     * @param endTime the end time of the reservation
     * @post a new reservation is created with given attributes and added to the resource and true changeable state
     * @throws IllegalArgumentException when the resource or timespan is null
     */
    public Reservation(Resource resource, LocalDateTime startTime, LocalDateTime endTime){
    	if( resource == null || startTime == null || endTime == null) {
    		throw new IllegalArgumentException("A reservation must have a resource, start time and end time !");
    	}
    	TimeSpan timeSpan = new TimeSpan(startTime, endTime);
        setResource(resource);
        setTimeSpan(timeSpan);
        resource.addReservation(this);
    }

    public Reservation(Resource resource, LocalDateTime startTime, LocalDateTime endTime, boolean userSpecific)


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
     *       the oldResource its reservation its
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
    	if(!(timeSpan.getStartTime().isAfter(getTimeSpan().getStartTime())) && !(timeSpan.getEndTime().isBefore(getTimeSpan().getEndTime()))) {
    		return true;
    	}
    	return false;
    }


    public void finishEarlier(LocalDateTime endTime) {
    	setTimeSpan(new TimeSpan(getTimeSpan().getStartTime(), endTime));
    }

    /**
     * Deconstructor for this reservation.
     *
     * @post the reservation is deleted from its resource its reservations
     *       and the resource is deleted from this reservation
     *       (the bidirectional link is deleted)
     */
    public void delete(){
        getResource().deleteReservation(this);
        setResource(null);
    }
}
