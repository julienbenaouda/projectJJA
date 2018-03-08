package taskman;

public class User {

    private static UserType userType;


    public static void setUserType(String userTypeParam){
        if(UserTypeParam.equals("REGULARUSER")) {
        	userType = UserType.REGULARUSER;
        } else {
        	if(userTypeParam.equals("DEVELOPER")) {
        		userType = UserType.DEVELOPER;
        	} else {
        		throw new IllegalArgumentException("The qpecified user type does not exist, please try again");
        	}
        }
    }

    public static boolean canChangeTaskStatus(){
        return userType == UserType.DEVELOPER;
    }
}
