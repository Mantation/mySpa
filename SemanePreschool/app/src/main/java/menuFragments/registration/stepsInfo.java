package menuFragments.registration;

public class stepsInfo {
    private static String name;
    private static String surname;
    private static String gender;
    private static String phone;
    private static String messagingToken;


    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        stepsInfo.name = name;
    }

    public static String getSurname() {
        return surname;
    }

    public static void setSurname(String surname) {
        stepsInfo.surname = surname;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        stepsInfo.gender = gender;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        stepsInfo.phone = phone;
    }

    public static String getMessagingToken() {
        return messagingToken;
    }

    public static void setMessagingToken(String messagingToken) {
        stepsInfo.messagingToken = messagingToken;
    }
}
