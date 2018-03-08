package taskman;

public class User {

    private static UserType userType;


    public static void setUserType(String s){
        userType = UserType.fromString(s);
    }

    public static String getUserType() {
        return userType.toString();
    }

    public static boolean canChangeTaskStatus(){
        return userType == UserType.DEVELOPER;
    }
}
