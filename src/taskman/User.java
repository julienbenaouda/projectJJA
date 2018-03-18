package taskman;

/**
 * Class to hold the currently active user type.
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 */
public class User {

    // Initializing with a regular user avoids null!
    private static UserType userType = UserType.REGULARUSER;

    /**
     * Sets the active user type to the given user type.
     *
     * @param s the name of the user type to change to.
     * @throws IllegalArgumentException if the user type does not exist.
     * @post the user type will be set to the given user type.
     */
    public static void setUserType(String s) throws IllegalArgumentException{
        userType = UserType.fromString(s);
    }

    /**
     * Returns the name of the active user type.
     *
     * @return user type
     */
    public static String getUserType() {
        return userType.toString();
    }

    /**
     * Returns if the current user type can change the task status.
     *
     * @return if the current user type can change the task status.
     */
    public static boolean canChangeTaskStatus(){
        return userType == UserType.DEVELOPER;
    }

    /**
     * Add a user to an XmlObject.
     *
     * @param userObject an XmlObject.
     * @throws ImportExportException if the user cannot be added to the XmlObject.
     * @post the user will be added to the XmlObject.
     */
    public static void addToXml(XmlObject userObject) throws ImportExportException {
        userObject.addAttribute("userType", getUserType());
    }

    /**
     * Restore a user from an XmlObject.
     *
     * @param userObject the XmlObject.
     * @throws ImportExportException if the user can't be created.
     * @post the user will be restored.
     */
    public static void setFromXml(XmlObject userObject) throws ImportExportException {
        setUserType(userObject.getAttribute("userType"));
    }

}
