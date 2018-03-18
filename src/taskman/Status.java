package taskman;

/**
 * This enum is responsible for storing the status of a Task.
 * @author Alexander Braekevelt, Jeroen Van Der Donckt
 */
public enum Status {
    /**
     * Available status.
     */
    AVAILABLE,
    /**
     * Unavailable status.
     */
    UNAVAILABLE,
    /**
     * Finished status.
     */
    FINISHED,
    /**
     * Failed status.
     */
    FAILED;

    /**
     * Returns the Status with as name the given String.
     * @param s the String with the name of the Status.
     * @return the Status of the given string.
     * @throws IllegalArgumentException if the Status does not exist.
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
     * @return a String.
     */
    @Override
    public String toString() {
        return this.name();
    }

    /**
     * Return if the Status is a final Status.
     * @return a Boolean.
     */
    public Boolean isFinal() {
        return this.equals(Status.FINISHED) || this.equals(Status.FAILED);
    }
}
