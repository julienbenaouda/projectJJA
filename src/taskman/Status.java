package taskman;

/**
 * This enum represents the status of a task.
 *
 * @author Alexander Braekevelt, Jeroen Van Der Donckt
 */
public enum Status {
    /**
     * Available status.
     */
    AVAILABLE, /**
     * Unavailable status.
     */
    UNAVAILABLE, /**
     * Finished status.
     */
    FINISHED, /**
     * Failed status.
     */
    FAILED;

    /**
     * Returns the status with as name the given string.
     *
     * @param s the string with the name of the status.
     * @return the status of the given string.
     * @throws IllegalArgumentException if the status does not exist.
     */
    public static Status fromString(String s) throws IllegalArgumentException{
        for (Status status: Status.values()) {
            if (s.equals(status.name())) {
                return status;
            }
        }
        throw new IllegalArgumentException("The given status does not exist!");
    }

    /**
     * Returns the name of the status
     * @return a String
     */
    @Override
    public String toString() {
        return this.name();
    }

    /**
     * Is final boolean.
     *
     * @return the boolean
     */
    public Boolean isFinal() {
        return this.equals(Status.FINISHED) || this.equals(Status.FAILED);
    }
}
