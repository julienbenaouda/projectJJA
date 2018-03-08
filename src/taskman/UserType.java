package taskman;

/**
 * This enum represents the type of a user.
 * @author Jeroen Van Der Donckt
 *
 */
public enum UserType {
    REGULARUSER, DEVELOPER;

    public static UserType fromString(String s) {
        for (UserType u: UserType.values()) {
            if (s.equals(u.name())) {
                return u;
            }
        }
        throw new IllegalArgumentException("The given user type does not exist!");
    }

}