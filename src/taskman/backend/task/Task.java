package taskman.backend.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Stack;

import taskman.backend.time.TimeSpan;
import taskman.backend.visitor.Entity;
import taskman.backend.visitor.Visitor;


/**
 * This class represents a task.
 *
 * @author Jeroen Van Der Donckt
 */
public class Task implements Entity {

    /**
     * Creates a new task with the given values.
     *
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable  deviation of the task
     * @post a new task is created with the given attributes and inactive status
     */
    public Task(String description, long estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setState(new TaskStateInactive());
        dependencies = new ArrayList<>();
    }


    /**
     * Creates a new task with thte given values.
     *
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable deviation of the task
     * @param timeSpan the time span of the task
     * @param state the task state of the task
     * @post a new task is created with the given attributes
     */
    private Task(String description, long estimatedDuration, double acceptableDeviation, TimeSpan timeSpan, TaskState state) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setTimeSpan(timeSpan);
        setState(state);
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
     *
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
     *
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
     *
     * @param acceptableDeviation the acceptable deviation of the task
     * @post the acceptable deviation of the task is set to the given deviation
     */
    private void setAcceptableDeviation(double acceptableDeviation){
        this.acceptableDeviation = acceptableDeviation;
    }


    /**
     * The time span of the task.
     */
    private TimeSpan timeSpan;

    /**
     * Returns the time span of the task.
     *
     * @return the time span of the task
     */
    public TimeSpan getTimeSpan(){
        return timeSpan;
    }

    /**
     * Creates a time span and sets the time span of the task to the new created time span.
     *
     * @param startTime the start time of the task its time span
     * @param endTime the end time of the task its time span
     * @post a new time span is created with given attributes and the time span of the task is set to this time span
     */
    protected void setTimeSpan(LocalDateTime startTime, LocalDateTime endTime){
        TimeSpan timeSpan = new TimeSpan(startTime, endTime); // Task creates the time span, because task stores time span ==> GRASP: Creator
        this.timeSpan = timeSpan;
    }
    // TODO: moet er hier geen throws IllegalArugmentException bij en moet dit ook niet bij constructor timespan?

    /**
     * Sets the time span of the task to the given time span.
     *
     * @param timeSpan the time span of the task
     * @post the time span of the task is set to the given time span
     */
    private void setTimeSpan(TimeSpan timeSpan){
        this.timeSpan = timeSpan;
    }
    // TODO: in TimeSpan moet er gechecked worden of het wel een valid timespan is


    // STATE PATTERN

    /**
     * The task state of the task.
     */
    private TaskState state;

    /**
     * Returns the task state of the task.
     *
     * @return the task state of the task
     */
    public TaskState getState(){
        return state;
    }

    /**
     * Sets the task state to the given state.
     *
     * @param state the task state of the task
     * @post the task state of the task is set to the given state
     * @implNote This method is set protected, so that we can apply the State Pattern (a state may need to change the state of the task)
     */
    protected void setState(TaskState state){
        this.state = state;
    }


    /**
     * Returns if the task is available.
     *
     * @return true if the task is available, otherwise false
     */
    public boolean isAvailable(){
        return getState().isAvailable(this);
    }

    /**
     * Returns if the task is unavailable.
     *
     * @return true if the task is unavailable, otherwise false
     */
    public boolean isUnavailable(){
        return getState().isUnavailable(this);
    }

    /**
     * Returns if the task is executing.
     *
     * @return true if the task is executing, otherwise false
     */
    public boolean isExecuting(){
        return getState().isExecuting();
    }

    /**
     * Returns if the task is failed.
     *
     * @return true if the task is failed, otherwise false
     */
    public boolean isFailed(){
        return getState().isFailed();
    }

    /**
     * Returns if the task is finished.
     *
     * @return true of the task is finished, otherwise false
     */
    public boolean isFinished(){
        return getState().isFinished();
    }


    /**
     * Updates the status of the task.
     *
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @param status the new status of the task
     * @post the time span and status of the task will be updated
     */
    public void updateStatus(LocalDateTime startTime, LocalDateTime endTime, String status) throws IllegalStateException, IllegalArgumentException {
        getState().updateStatus(this, startTime, endTime, status);
    }


    /**
     * Return the delay between the end time and the estimated end time in minutes.
     *
     * @return the time between the end time and the estimated end time in minutes
     */
    public long getDelay() throws IllegalStateException {
        return getState().getDelay(this);
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
     * @post the alternative task of the task is set to the given task
     * @implNote this method is protected (and exists) so that the State Pattern can use this (to implement setAlternative())
     */
    protected void setAlternativeTask(Task alternative){
        this.alternative = alternative;
    }
    // TODO: dit is wel een skere oplossing


    /**
     * Sets the alternative task of the task to the given task.
     *
     * @param alternative the alternative task of the task
     * @post the alternative task of the task is set to the given task
     */
    public void setAlternative(Task alternative) throws IllegalStateException, IllegalArgumentException {
        getState().setAlternative(this, alternative);
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
        return (ArrayList<Task>) dependencies.clone();
    }


    /**
     * Sets the dependencies of the task.
     *
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
     * @post the dependency is added to the task
     * @implNote this method is protected (and exists) so that the State Pattern can use this (to implement addDependency())
     */
    protected void addDependencyTask(Task dependency){
        this.dependencies.add(dependency);
    }
    // TODO: dit is wel een skere oplossing


    /**
     * Adds a dependency to the task.
     *
     * @param dependency task that needs to be added to the task
     * @post the dependency is added to the task
     */
    public void addDependency(Task dependency) throws IllegalStateException, IllegalArgumentException {
        getState().addDependency(this, dependency);
    }


    /**
     * Removes dependency of the given task.
     *
     * @param dependency task that needs to be removed as dependency of the task
     * @throws IllegalArgumentException the dependency task must be a dependency of the task
     * @post the dependency is deleted from the task
     */
    public void removeDependency(Task dependency){
        if (! getDependencies().contains(dependency)){
            throw new IllegalArgumentException("The given task is not a dependency of the task.");
        }
        dependencies.remove(dependency);
    }


    // LOOP CHECKING CODE

    /**
     * Checks if the searched task is equal to the root or one of its dependencies or alternatives (recursively).
     * 
     * @param root the root task whose dependencies and alternatives will be further searched
     * @param searchedTask the task we want to search for
     * @return true if the task is found, false otherwise
     * @implNote this method is protected so that the State Pattern can make use of it
     */
    protected static boolean containsLoop(Task root, Task searchedTask){
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


    // VISITOR PATTERN

    /**
     * Accepts a visitor
     *
     * @param v the visitor to be accepted
     */
    @Override
    public void accept(Visitor v) {
        v.visitTask(this);
    }

}
