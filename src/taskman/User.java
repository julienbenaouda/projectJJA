package taskman;

public class User {

    private static UserType userType;


    public static void setUserType(UserType userTypeParam){
        userType = userTypeParam;
    }

    public static boolean canChangeTaskStatus(){
        return userType == UserType.DEVELOPER;
    }
}
