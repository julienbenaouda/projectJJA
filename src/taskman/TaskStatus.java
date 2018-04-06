package taskman;

/**
 * This enum is responsible for storing the status of a Task.
 * @author Alexander Braekevelt, Jeroen Van Der Donckt
 */
public enum TaskStatus {

    /**
     * Inactive status.
     */
    INACTIVE,
    /**
     * Finished status.
     */
    FINISHED,
    /**
     * Failed status.
     */
    FAILED;

    /**
     * Return if the TaskStatus is a final TaskStatus.
     * @return a Boolean.
     */
    public Boolean isFinal() {
        return this.equals(TaskStatus.FINISHED) || this.equals(TaskStatus.FAILED);
    }
}
