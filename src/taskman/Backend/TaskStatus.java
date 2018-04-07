package taskman.Backend;

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
    * Returns the TaskStatus with as name the given String.
    * @param s the String with the name of the TaskStatus.
    * @return the TaskStatus of the given string.
    * @throws IllegalArgumentException if the TaskStatus does not exist.
    */
    public static TaskStatus fromString(String s) throws IllegalArgumentException{
        for (TaskStatus status: TaskStatus.values()) {
            if (s.equals(status.name())) {
                return status;
            }
        }
        throw new IllegalArgumentException("The given status does not exist!");
    }

    /**
    * Returns the name of the status
    * @return a String.
    */
    @Override
    public String toString() {
        return this.name();
    }

    /**
     * Return if the TaskStatus is a final TaskStatus.
     * @return a Boolean.
     */
    public Boolean isFinal() {
        return this.equals(TaskStatus.FINISHED) || this.equals(TaskStatus.FAILED);
    }
}
