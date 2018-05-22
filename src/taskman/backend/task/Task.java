package taskman.backend.task;

import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.resource.Plan;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.Developer;
import taskman.backend.user.User;
import taskman.backend.wrappers.TaskWrapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * This class represents a task.
 * A task contains a name, description, estimated duration, acceptable deviation and time span. 
 * Furthermore, it contains a list of the task on which it depends, and an alternative task. The alternative task can only be filled in when the task is failed. 
 * @author Jeroen Van Der Donckt
 */
public class Task implements TaskWrapper {

    /**
     * Creates a new task with the given values.
     * @param name the name of the task.
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable  deviation of the task
     * @post a new task is created with the given attributes and unavailable status, empty dependencies list and a new plan
     */
    public Task(String name, String description, long estimatedDuration, double acceptableDeviation) {
        setName(name);
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setState(new TaskStateUnavailable());
        dependencies = new ArrayList<>();
        plan = new Plan(this);
    }

    /**
     * The task name.
     */
    private String name;

    /**
     * Returns the name of the task.
     * @return a String.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the task.
     */
    private void setName(String name) {
        this.name = name;
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
    @Override
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
    @Override
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
    @Override
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
        this.timeSpan = new TimeSpan(startTime, endTime);
    }

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
     * Returns the status of the task state.
     *
     * @return the status of the task state
     */
    @Override
	public String getStatus(){
        return state.getStatus();
    }

    /**
     * Returns if the task can be planned.
     * @return if the task can be planned.
     */
    @Override
    public boolean canBePlanned() {
        return getState().canBePlanned();
    }

    /**
     * Returns if the task can be updated.
     * @return if the task can be update.
     */
    @Override
    public boolean canBeUpdated() {
        return getState().canBeUpdated();
    }


    /**
     * End the execution of the task.
     *
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @param status the new status of the task
     * @param user the user to update the task status for
     * @post the time span and status of the task will be updated
     */
    public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) throws IllegalStateException, IllegalArgumentException {
        if (getPlan().isDeveloperFromPlan(user)) {
            getState().endExecution(this, startTime, endTime, status);
        } else {
        	throw new IllegalArgumentException("The user must be a developer of the task.");
        }
    }

    /**
     * makes a task executing
     * @param resourceManager the resource manager to pass to the state
     * @param startTime the time when the task starts executing
     * @param user the user to check the assignment for
     * @post the status of the task is changed to executing
     * @throws IllegalArgumentException when the user is not assigned to the task
     * @throws IllegalArgumentException if the plan cannot be rescheduled.
     */
    public void makeExecuting(ResourceManager resourceManager, LocalDateTime startTime, User user) throws IllegalArgumentException {
        if (getPlan().isDeveloperFromPlan(user)) {
            getState().execute(this, resourceManager, startTime);
        } else {
            throw new IllegalArgumentException("The user must be a developer of the task.");
        }
    }

    /**
     * Returns if this task is available.
     * @param startTime a start time.
     * @return true if this task is available, otherwise false.
     */
    public boolean isAvailable(LocalDateTime startTime){
        return getState().isAvailable(this, startTime);
    }

    /**
     * Returns if this task is finished.
     * @return if this task is finished.
     */
    public boolean isFinished() {
        return getState().isFinished();
    }

    /**
     * Return the delay between the end time and the estimated end time in minutes.
     *
     * @return the time between the end time and the estimated end time in minutes
     */
    @Override
    public long getDelay() throws IllegalStateException {
        return getState().getDelay(this);
    }

    /**
     * The alternative task of the task.
     */
    private Task alternative;

    /**
     * Returns the alternative task of the task.
     * @return the alternative task
     */
    @Override
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

    /**
     * Sets the alternative task of the task to the given task.
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
     * @return the dependencies of the task
     */
    @Override
    public List<Task> getDependencies(){
        return new ArrayList<>(dependencies);
    }

    /**
     * Adds a dependency to the task.
     * @param dependency task that needs to be added to the task
     * @post the dependency is added to the task
     * @implNote this method is protected (and exists) so that the State Pattern can use this (to implement addDependency())
     */
    protected void addDependencyTask(Task dependency){
        this.dependencies.add(dependency);
    }

    /**
     * Adds a dependency to the task.
     * @param dependency task that needs to be added to the task
     * @post the dependency is added to the task
     */
    public void addDependency(Task dependency) throws IllegalStateException, IllegalArgumentException {
        getState().addDependency(this, dependency);
    }

    /**
     * Removes dependency of the given task.
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


    /**
     * Represents the plan of the task.
     */
    private Plan plan;

    /**
     * Returns the plan of the task.
     *
     * @return the plan of the task
     */
    public Plan getPlan(){
        return plan;
    }

    /**
     * Initializes a plan for this task.
     * @param startTime the start time for the plan.
     * @throws IllegalStateException if the state is not unavailable.
     */
    public void initializePlan(LocalDateTime startTime) throws IllegalStateException {
        getState().initializePlan(this, startTime);
    }

    /**
     * Get the resources of the plan of a task.
     * @return a list of resources.
     * @throws IllegalStateException if the state is not planned.
     */
    public List<Resource> getPlannedResources() throws IllegalStateException {
        return getState().getPlannedResources(this);
    }

    /**
     * Returns a list of resources as alternatives for the given resource.
     * @param resource a resource wrapper to search alternatives for.
     * @return a list of resources as alternatives for the given resource.
     * @throws IllegalStateException if the state is not planned.
     */
    public List<Resource> getAlternativeResources(Resource resource) throws IllegalStateException {
        return getState().getAlternativeResources(this, resource);
    }

    /**
     * Change a resource of a plan of this task.
     * @param oldResource the resource to change.
     * @param newResource the resource to change to.
     * @throws IllegalStateException if the state is not planned.
     */
    public void changeResource(Resource oldResource, Resource newResource) throws IllegalStateException {
        getState().changeResource(this, oldResource, newResource);
    }

    /**
     * Cancel the plan of this task.
     * @throws IllegalStateException if the state is not planned.
     */
    public void cancelPlan() throws IllegalStateException {
        getState().cancelPlan(this);
    }

    /**
     * Adds the given requirement to the task its requirements.
     *
     * @param resourceManager the resource manager of the system
     * @param resourceType the resource type of the requirement
     * @param amount the amount of the requirement
     * @post the requirement is added to the requirements of the task.
     */
    public void addRequirement(ResourceManager resourceManager, ResourceType resourceType, int amount) {
        getState().addRequirement(resourceManager, this, resourceType, amount);
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

    /**
     * Returns if the given user has access to this task.
     * @param user a User.
     * @return if the given user has access to this task.
     */
    public boolean hasAccessTo(User user) {
        if (user instanceof Developer) {
            return this.getPlan().isDeveloperFromPlan(user);
        } else {
            return true;
        }
    }

    /**
     * Delegates the task to the given branch office.
     * @param branchOffice the branch office to delegate the given task to.
     * @param currentTime the current system time
     * @post the task is delegated to the given branch office
     */
    public void delegate(BranchOffice branchOffice, LocalDateTime currentTime) {
    	getState().delegate(branchOffice, this, currentTime);
    }

}
