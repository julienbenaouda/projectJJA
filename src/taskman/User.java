package taskman;

/**
 * Class to hold the currently active user type.
 *
 * @author Jeroen Van Der Donckt, Alexander Braekevelt
 */
public class User {

    public enum UserType {
        REGULARUSER,
        DEVELOPPER,
        PROJECTMANAGER;
    }

    public User(UserType userType){
        setUserType(userType);
    }

    private UserType userType;

    /**
     * Sets the active user type to the given user type.
     *
     * @param userType user type of the user
     * @post the user type will be set to the given user type.
     */
    private void setUserType(UserType userType){
        this.userType = userType;
    }

    /**
     * Returns the name of the active user type.
     *
     * @return user type
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Returns if the current user type can change the task status.
     *
     * @return if the current user type can change the task status.
     */
    public boolean canChangeTaskStatus(){
        return userType == UserType.DEVELOPPER;
    }

}
