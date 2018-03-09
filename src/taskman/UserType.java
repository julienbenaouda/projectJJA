package taskman;

/**
 * This enum represents the type of a user.
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 *
 */
public enum UserType {
    REGULARUSER, DEVELOPER;

    /**
     * Returns the user type with as name the given string.
     * @param s the string with the name of the user type.
     * @return the user type of the given string.
     * @throws IllegalArgumentException if the user type does not exist.
     */
    public static UserType fromString(String s) throws IllegalArgumentException {
        for (UserType u: UserType.values()) {
            if (s.equals(u.name())) {
                return u;
            }
        }
        throw new IllegalArgumentException("The given user type does not exist!");
    }

    /**
     * Returns the name of the user type
     * @return a String
     */
    @Override
    public String toString() {
        return this.name();
    }

}