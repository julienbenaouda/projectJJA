package taskman;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Stack;


/**
 * This class represents a task.
 *
 * @author Jeroen Van Der Donckt
 */
public class Task {

    /**
     * Creates a new task with the given values.
     *
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable  deviation of the task
     * @post a new task is created with the given attributes an available status
     */
    public Task(String description, long estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        initializeTimeSpan(TaskStatus.AVAILABLE);
        dependencies = new ArrayList<>();
    }


    /**
     * Creates a new task with thte given values.
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable deviation of the task
     * @param timeSpan the time span of the task
     * @post a new task is created with the given attributes
     */
    private Task(String description, long estimatedDuration, double acceptableDeviation, TimeSpan timeSpan) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setTimeSpan(timeSpan);
        dependencies = new ArrayList<>();
    }


    /**
     * The task description.
     */
    private String description;


    /**
     * Returns the task description.
     *
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
     * The estimated duration of the task in minutes.
     */
    private long estimatedDuration;


    /**
     * Returns the estimated duration of the task in minutes.
     *
     * @return the estimated duration of the task in minutes
     */
    public long getEstimatedDuration(){
        return estimatedDuration;
    }


    /**
     * Sets the estimated duration of the task to the given duration in minutes.
     * @param estimatedDuration the estimated duration of the task in minutes
     * @post the estimated duration of the task is set to the given duration
     */
    private void setEstimatedDuration(long estimatedDuration){
        this.estimatedDuration = estimatedDuration;
    }


    /**
     * The acceptable deviation of the task.
     */
    private double acceptableDeviation;


    /**
     * Returns the acceptable deviation of the task.
     *
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
    private void setAcceptableDeviation(double acceptableDeviation){
        this.acceptableDeviation = acceptableDeviation;
    }


    /**
     * The start time of the task.
     */
    private TimeSpan timeSpan;


    /**
     * Returns the start time of the task.
     *
     * @return the start time of the task
     */
    public TimeSpan getTimeSpan(){
        return timeSpan;
    }

    /**
     * Initializes the time span for a new task with the given status
     *
     * @param status the status of the time span
     * @post a new time span is created with the given status
     */
    private void initializeTimeSpan(TaskStatus status){
        timeSpan = new TimeSpan(status);
    }

    /**
     * Sets the status of the time span of the task to the given status
     *
     * @param status the status of the time span of the task
     * @post the status of the time span of the task is set to the the given status
     */
    private void setTimeSpanStatus(TaskStatus status){
        getTimeSpan().setStatus(status);
    }

    /**
     * Sets the time span of the task to the given time span.
     * @param timeSpan the time span of the task
     * @post the time span of the task is set to the given time span
     */
    private void setTimeSpan(TimeSpan timeSpan){
        this.timeSpan = timeSpan;
    }
    // TODO: in TimeSpan moet er gechecked worden of het wel een valid timespan is


    /**
     * Updates the status of the task.
     *
     * @param timeSpan the time span of the task
     * @throws IllegalArgumentException when the status is not FINISHED and not FAILED or when the timeSpan is invalid
     * @post the start time, end time and status of the task will be updated
     */
    public void updateStatus(TimeSpan timeSpan) throws IllegalArgumentException {
        if (timeSpan.getStatus() != TaskStatus.FINISHED && timeSpan.getStatus() != TaskStatus.FAILED){
            throw new IllegalArgumentException("The status may only be finished or failed.");
        }
        for (Task dependency: this.getDependencies()) {
            if (dependency.getTimeSpan().getStatus().isFinal() && timeSpan.getStartTime().isBefore(dependency.getTimeSpan().getEndTime())) {
                throw new IllegalArgumentException("The task must start after its dependencies!");
            }
        }
        setTimeSpan(timeSpan);
    }


    /**
     * Return the delay between the end time and the estimated end time in minutes.
     *
     * @return the time between the end time and the estimated end time in minutes
     * @throws IllegalStateException if the task is not yet finished.
     */
    public Long getDelay() throws IllegalStateException {
        if (this.getTimeSpan().getStatus() != TaskStatus.FINISHED) {
            throw new IllegalStateException("Cannot calculate delay of task if not finished!");
        }
        else {
            return Duration.between(getTimeSpan().getStartTime(), getTimeSpan().getEndTime()).toMinutes() - getEstimatedDuration();
        }
    }


    /**
     * The alternative task of the task.
     */
    private Task alternative;


    /**
     * Returns the alternative task of the task.
     *
     * @return the alternative task
     */
    public Task getAlternative(){
        return alternative;
    }


    /**
     * Sets the alternative task of the task to the given task.
     *
     * @param alternative the alternative task of the task
     * @throws IllegalStateException the task must be failed to set the alternative task
     * @throws IllegalArgumentException the alternative may not be this task or its alternative or one of its dependencies or one of these alternatives recursively
     * @post the alternative task of the task is set to the given task
     */
    public void setAlternative(Task alternative) throws IllegalStateException, IllegalArgumentException {
        if (getTimeSpan().getStatus() != TaskStatus.FAILED){
            throw new IllegalStateException("The task must be failed to set an alternative.");
        }
        if (containsLoop(this, alternative)){
            throw new IllegalArgumentException("The alternative may not be one of the dependecies or the alternative of this or of its dependendecies recursivley");
        }
        this.alternative = alternative;             // may be null if there is no alternative
    }


    /**
     * The dependencies of the task.
     */
    private ArrayList<Task> dependencies;


    /**
     * Returns a list with all dependencies of the task.
     *
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
     *
     * @param dependency task that needs to be added to the task
     * @throws IllegalArgumentException when the dependency is this task or its alternative or one of its dependencies or one of these alternatives recursively
     * @throws IllegalStateException if the task is already finished or failed
     * @post the dependency is added to the task, the status of the task is updated accordingly
     */
    public void addDependency(Task dependency) throws IllegalArgumentException, IllegalStateException {
        if (containsLoop(this, dependency)){
            throw new IllegalArgumentException("The alternative may not be one of the dependencies or the alternative of this or of its dependendecies recursivley");
        }
        if (getTimeSpan().getStatus() == TaskStatus.FAILED || getTimeSpan().getStatus() == TaskStatus.FINISHED){
            throw new IllegalStateException("No dependencies may be added to failed or finished tasks.");
        }
        if (dependency.getTimeSpan().getStatus() == TaskStatus.AVAILABLE || dependency.getTimeSpan().getStatus() == TaskStatus.UNAVAILABLE){
            setTimeSpanStatus(TaskStatus.UNAVAILABLE);
        }
        else if (dependency.getTimeSpan().getStatus() == TaskStatus.FAILED){
            Task alternative = dependency;
            while (alternative.getAlternative() !=  null && alternative.getTimeSpan().getStatus() == TaskStatus.FAILED){
                alternative = alternative.getAlternative();
            }
            if (alternative.getTimeSpan().getStatus() != TaskStatus.FINISHED){
                setTimeSpanStatus(TaskStatus.UNAVAILABLE);
            }
        }
        dependencies.add(dependency);
    }

    /**
     * Restores the dependencies of the task without checks.
     *
     * @param dependencies an ArrayList of Task on which this task is dependant.
     */
    public void restoreDependencies(ArrayList<Task> dependencies) { // TODO: wordt deze functie wel echt gebruikt of moet die in de andere public constructor gebruikt worden
        this.setDependencies(dependencies);
    }

    /**
     * Removes dependency of the given task.
     *
     * @param dependency task that needs to be removed as dependency of the task
     * @throws IllegalArgumentException the dependency task must be a dependency of the task
     * @post the dependency is deleted from the task and the task status is updated accordingly
     */
    public void removeDependency(Task dependency){
        if (! dependencies.contains(dependency)){
            throw new IllegalArgumentException("The given task is not a dependency of the task.");
        }
        dependencies.remove(dependency);
        if (dependency.getTimeSpan().getStatus() == TaskStatus.AVAILABLE || dependency.getTimeSpan().getStatus() == TaskStatus.UNAVAILABLE){
            boolean becomesAvailable = true;
            for (Task d : getDependencies()){
                if (d.getTimeSpan().getStatus() == TaskStatus.AVAILABLE || d.getTimeSpan().getStatus() == TaskStatus.UNAVAILABLE){
                    becomesAvailable = false;
                }
                else if (d.getTimeSpan().getStatus() == TaskStatus.FAILED){
                    Task alternative = d;
                    while (alternative.getAlternative() != null && alternative.getTimeSpan().getStatus() == TaskStatus.FAILED){
                        alternative = alternative.getAlternative();
                    }
                    if (alternative.getTimeSpan().getStatus() != TaskStatus.FINISHED){
                        becomesAvailable = false;
                    }
                }
            }
            if (becomesAvailable){
                setTimeSpanStatus(TaskStatus.AVAILABLE);
            }
        }
    }



    // LOOP CHECKING CODE

    /**
     * Checks if the searched task is equal to the root or one of its dependencies or alternatives (recursively).
     * @param root the root task whose dependencies and alternatives will be further searched
     * @param searchedTask the task we want to search for
     * @return true if the task is found, false otherwise
     */
    private static boolean containsLoop(Task root, Task searchedTask){
        Stack<Task> searchStack = new Stack<>();
        searchStack.push(root);
        if (root.getAlternative() != null) {
            searchStack.push(root.getAlternative());
        }

        while (! searchStack.isEmpty()){
            Task task = searchStack.pop();
            if (task == searchedTask) {
                return true;
            }

            if (task.getAlternative() != null){
                searchStack.push(task.getAlternative());
            }
            for (Task dependency : task.getDependencies()){
                searchStack.push(dependency);
            }
        }
        return false;
    }

}
