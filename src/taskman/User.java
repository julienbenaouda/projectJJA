package taskman;

/**
 * Class to hold the currently active user type.
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 */
public class User {

    // Initializing with a regular user avoids null!
    private static UserType userType = UserType.REGULARUSER;

    /**
     * Sets the active user type to the given user type.
     * @param s the name of the user type to change to.
     * @post the user type will be set to the given user type.
     * @throws IllegalArgumentException if the user type does not exist.
     */
    public static void setUserType(String s) throws IllegalArgumentException{
        userType = UserType.fromString(s);
    }

    /**
     * Returns the name of the active user type.
     * @return
     */
    public static String getUserType() {
        return userType.toString();
    }

    /**
     * Returns if the current user type can change the task status.
     * @return if the current user type can change the task status.
     */
    public static boolean canChangeTaskStatus(){
        return userType == UserType.DEVELOPER;
    }

    public static void addToXml(XmlObject userObject) {
        userObject.addAttribute("userType", getUserType());
    }

    public static void setFromXml(XmlObject userObject) {
        setUserType(userObject.getAttribute("userType"));
    }

}
