package taskman;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * This class represents a task.
 * @author Jeroen Van Der Donckt
 *
 */
public class Task {

    /**
     * Creates a new task with the given values.
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable  deviation of the task
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @post a new task is created with the given attributes
     */
    public Task(String description, Duration estimatedDuration, double acceptableDeviation, String startTime, String endTime)
    {
        setID();
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStartTime(startTime);
        setEndTime(endTime);
        dependencies = new ArrayList<>();
    }



    /**
     * The latest task ID.
     */
    private static int lastTaskID = 0;


    /**
     * Return the ID of the latest task. (This should be the maximum of all ID's)
     * @return the ID of the latest task
     */
    public static int getLastTaskID() { return lastTaskID; }


    /**
     * Sets the last task ID of task.
     * @param ID the ID of the last task ID
     * @post the last task ID is set to the given ID
     */
    private static void setLastTaskID(int ID) { lastTaskID = ID; }



    /**
     * The task ID.
     */
    private int ID;


    /**
     * Return the ID of the task.
     * @return the ID of the task
     */
    public int getID(){
        return ID;
    }


    /**
     * Sets the ID of the task.
     * @post the last task ID is incremented by 1
     * @post the ID of the task is set to new last task ID
     */
    private void setID(){
        ID = getLastTaskID() + 1;
        setLastTaskID(ID);
    }



    /**
     * The task description.
     */
    private String description;


    /**
     * Returns the task description.
     * @return the task description
     */
    public String getDescription(){
        return description;
    }


    /**
     * Sets the task description to the given description.
     * @param description the description of the task
     * @post the task description is set to the given description
     */
    private void setDescription(String description){
        this.description = description;
    }



    /**
     * The estimated duration of the task.
     */
    private Duration estimatedDuration; // TODO: aangezien er enkel minuten doorgegeven wordt, ist dan nie beter om da als int fzo bij te houden?


    /**
     * Returns the estimated duration of the task.
     * @return the estimated duration of the task
     */
    public Duration getEstimatedDuration(){
        return estimatedDuration; // TODO: is het wel een slim idee om dit object terug te geven?? is het niet beter .toString()
    }


    /**
     * Sets the estimated duration of the task to the given duration.
     * @param estimatedDuration the estimated duration of the task
     * @post the estimated duration of the task is set to the given duration
     */
    private void setEstimatedDuration(Duration estimatedDuration){
        this.estimatedDuration = estimatedDuration;
    }



    /**
     * The acceptable deviation of the task.
     */
    private double acceptableDeviation;


    /**
     * Returns the acceptable deviation of the task
     * @return the acceptable deviation of the task
     */
    public double getAcceptableDeviation(){
        return acceptableDeviation;
    }

    /**
     * Sets the acceptable deviation of the task to the given deviation.
     * @param acceptableDeviation the acceptable deviation of the task
     * @post the acceptable deviation of the task is set to the given deviation
     */
    private final void setAcceptableDeviation(double acceptableDeviation){
        this.acceptableDeviation = acceptableDeviation;
    }



    /**
     * The start time of the task.
     */
    private LocalDateTime startTime;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    /**
     * Returns the start time of the task.
     * @return the start time of the task
     */
    public LocalDateTime getStartTime(){
        return startTime; // TODO: is het wel slim om het object terug te geven?
    }


    /**
     * Sets the start time of the task to the given start time.
     * @param startTimeStr the start time of the task
     * @post the start time of the task is set to the given start time
     */
    private void setStartTime(String startTimeStr){
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, dateTimeFormatter);
        this.startTime = startTime;
    }



    /**
     * The end time of the task.
     */
    private LocalDateTime endTime;


    /**
     * Returns the end time of the task.
     * @return the end time of the task
     */
    public LocalDateTime getEndTime(){
        return endTime; // TODO: idem opmerking startTime
    }

    /**
     * Sets the end time of the task tot the given end time
     * @param endTimeStr the end time of the task
     * @pre the end time must be later than the start time
     * @post the end time of the task is set to the given end time
     */
    private void setEndTime(String endTimeStr) throws  IllegalArgumentException{ // TODO: moet er @throws gebruikt worden hier??
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, dateTimeFormatter);
        if(endTime.compareTo(startTime) > 0)
        {
            this.endTime = endTime;
        } else {
            throw new IllegalArgumentException("The end time can't be before the start time.");
        }
    }



    /**
     * The status of the task.
     */
    private Status status;


    /**
     * Returns the task status.
     * @return the task status
     */
    public Status getStatus(){
        return status;
    }


    /**
     * Sets the task status to the given status.
     * @param status the task status
     * @post the task status is set to the given status
     */
    private void setStatus(Status status){
        this.status = status;
    }



    /**
     * The alternative task of the task.
     */
    private Task alternative;


    /**
     * Returns the alternative task of the task
     * @return the alternative task
     */
    public Task getAlternative(){
        return alternative;
    }


    /**
     * Sets the alternative task of the task to the given task.
     * @param alternative the alternative task of the task
     * @post the alternative task of the task is set to the given task
     * @throws IllegalArgumentException alternative may not be this task or a dependency
     */
    public void setAlternative(Task alternative) throws IllegalArgumentException {  // TODO: kan dit dan ook naar zichzelf verwijzen? + moeten we niet controleren dat dit ook geen depedency is
        if (alternative == this){
            throw new IllegalArgumentException("The alternative task may not be this task.");
        }
        else if (dependencies.contains(alternative)){
            throw new IllegalArgumentException("The alternative task may not be a dependency.");
        }
        this.alternative = alternative;             // may be null
    }



    /**
     * The dependencies of the task.
     */
    private ArrayList<Task> dependencies;


    /**
     * Returns a list with all dependencies of the task.
     * @return the dependencies of the task
     */
    public ArrayList<Task> getDependencies(){
        return (ArrayList<Task>) this.dependencies.clone();
    }


    /**
     * Sets the dependencies of the task.
     * @param dependencies list of dependent tasks of the task
     * @post the dependencies is set to the given dependent tasks
     */
    private void setDependencies(ArrayList<Task> dependencies){
        this.dependencies = dependencies;
    }


    /**
     * Adds a dependency to the task.
     * @param dependency the dependency of the task
     * @post the dependency is added to the task
     * @throws IllegalArgumentException dependency may not be the alternative
     */
    public void addDependency(Task dependency) {
        if (dependency == alternative){
            throw new IllegalArgumentException("The dependency may not be the alternative of the task.");
        }
        dependencies.add(dependency);
    }


    public String getTaskDetails(){
        return "not yet implemented";
    }

}
