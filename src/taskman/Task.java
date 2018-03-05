package taskman;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a task.
 * @author Jeroen Van Der Donckt
 *
 */
public class Task {

    private String description;
    private Duration estimatedDuration;
    private Double acceptableDeviation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Status status;
    ArrayList<Task> dependencies;
    Task alternative;

    /**
     * creates a new task with the given values
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task
     * @param acceptableDeviation the acceptable  deviation of the task
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @param dependencies the
     * @param alternative the alternative task of the task
     * @post a new task is created with the given attributes
     */
    public Task(String description, Duration estimatedDuration, Double acceptableDeviation, LocalDateTime startTime, LocalDateTime endTime, List<Task> dependencies, Task alternative)
    {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStartTime(startTime);
        setEndTime(endTime);
        setDependencies(dependencies);
        setAlternative(alternative);
        dependencies = new ArrayList<>();
    }


    // GETTERS

    /**
     * returns the task description
     * @return the task description
     */
    public String getDescription(){
        return description;
    }

    /**
     * returns the estimated duration of the task
     * @return the estimated duration of the task
     */
    public Duration getEstimatedDuration(){
        return estimatedDuration; // TODO: is het wel een slim idee om dit object terug te geven?? is het niet beter .toString()
    }

    /**
     * returns the acceptable deviation of the task
     * @return the acceptable deviation of the task
     */
    public Double getAcceptableDeviation(){
        return acceptableDeviation;
    }

    /**
     * returns the start time of the task
     * @return the start time of the task
     */
    public LocalDateTime getStartTime(){
        return startTime; // TODO: idem opmerking hierboven
    }

    /**
     * returns the end time of the task
     * @return the end time of the task
     */
    public LocalDateTime getEndTime(){
        return endTime; // TODO: idem opmerking hierboven
    }

    /**
     * returns the task status
     * @return the task status
     */
    public Status getStatus(){
        return status;
    }

    /**
     * Returns a list with all dependencies of the task
     * @return the dependencies of the task
     */
    public ArrayList<Task> getDependencies(){ // TODO: staat wel niet in UML en dit fixen
        ArrayList<Task> dependenciesCopy = new ArrayList<>(dependencies); // makes a copy
        return dependenciesCopy;
    }

    /**
     * returns the alternative task of the task
     * @return the alternative task
     */
    public Task getAlternative(){
        return alternative;
    }


    // SETTERS

    /**
     * Sets the task description to the given description
     * @param description the description of the task
     * @post the task description is set to the given description
     */
    private void setDescription(String description){
        this.description = description;
    }

    /**
     * Sets the estimated duration of the task to the given duration
     * @param estimatedDuration the estimated duration of the task
     * @post the estimated duration of the task is set to the given duration
     */
    private void setEstimatedDuration(Duration estimatedDuration){
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * Sets the acceptable deviation of the task to the given deviation
     * @param acceptableDeviation the acceptable deviation of the task
     * @post the acceptable deviation of the task is set to the given deviation
     */
    private void setAcceptableDeviation(Double acceptableDeviation){
        this.acceptableDeviation = acceptableDeviation;
    }

    /**
     * sets the start time of the task
     * @param startTime the start time of the task
     * @post the start time of the task is set to the given start time
     */
    private void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    /**
     * Sets the end time of the task
     * @param endTime the end time of the task
     * @pre the end time must be later than the start time
     * @post the end time of the task is set to the give end time
     */
    private void setEndTime(LocalDateTime endTime) throws  IllegalArgumentException{ // TODO: moet er @throws gebruikt worden hier??
        if(endTime.compareTo(startTime) > 0)
        {
            this.endTime = endTime;
        } else {
            throw new IllegalArgumentException("The end time can't be before the start time.");
        }
    }

    /**
     * sets the task status
     * @param status the task status
     * @post the task status is set to the given status
     */
    private void setStatus(Status status){
        this.status = status;
    }

    /**
     * sets the dependencies of the task
     * @param dependencies list of dependent tasks of the task
     * @post the dependencies is set to the given dependent tasks
     */
    private void setDependencies(List<Task> dependencies){
        this.dependencies = dependencies;
    }

    /**
     * adds a dependency to the task
     * @param dependency the dependency of the task
     * @post the dependency is added to the task
     */
    public void addDependency(Task dependency) {
        dependencies.add(dependency);
    }

    /**
     * sets the alternative task of the task
     * @param alternative the alternative task of the task
     * @post the alternative task of the task is set to the given task
     */
    private void setAlternative(Task alternative){  // TODO: kan dit dan ook naar zichzelf verwijzen?
        this.alternative = alternative;             // mag dus null zijn
    }


}
