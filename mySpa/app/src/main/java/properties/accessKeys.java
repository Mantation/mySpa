package properties;

public class accessKeys {
    private static String name;
    private static String surname;
    private static String email;
    private static String phone;
    private static String altPhone;
    private static String gender;
    private static String idnumber;
    private static String idType;
    public static String []TownValues;
    public static String []ProvinceValues;
    private static String defaultDocument;
    private static String Longitude;
    private static String Latitude;
    private static String defaultUserId;
    private static String defaultUserEmail;
    private static boolean authenticated;
    private static boolean networkUnAvailable;
    private static boolean loggedin;
    private static boolean exitApplication;
    private static String messagingToken;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        accessKeys.name = name;
    }

    public static String getSurname() {
        return surname;
    }

    public static void setSurname(String surname) {
        accessKeys.surname = surname;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        accessKeys.email = email;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        accessKeys.phone = phone;
    }

    public static String getAltPhone() {
        return altPhone;
    }

    public static void setAltPhone(String altPhone) {
        accessKeys.altPhone = altPhone;
    }

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        accessKeys.gender = gender;
    }

    public static String getIdnumber() {
        return idnumber;
    }

    public static void setIdnumber(String idnumber) {
        accessKeys.idnumber = idnumber;
    }

    public static String getIdType() {
        return idType;
    }

    public static void setIdType(String idType) {
        accessKeys.idType = idType;
    }

    public static String getDefaultDocument() {
        return defaultDocument;
    }

    public static void setDefaultDocument(String defaultDocument) {
        accessKeys.defaultDocument = defaultDocument;
    }

    public static String getLongitude() {
        return Longitude;
    }

    public static void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public static String getLatitude() {
        return Latitude;
    }

    public static void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public static String getDefaultUserId() {
        return defaultUserId;
    }

    public static void setDefaultUserId(String defaultUserId) {
        accessKeys.defaultUserId = defaultUserId;
    }

    public static boolean isAuthenticated() {
        return authenticated;
    }

    public static void setAuthenticated(boolean authenticated) {
        accessKeys.authenticated = authenticated;
    }

    public static boolean isNetworkUnAvailable() {
        return networkUnAvailable;
    }

    public static void setNetworkUnAvailable(boolean networkUnAvailable) {
        accessKeys.networkUnAvailable = networkUnAvailable;
    }

    public static String getDefaultUserEmail() {
        return defaultUserEmail;
    }

    public static void setDefaultUserEmail(String defaultUserEmail) {
        accessKeys.defaultUserEmail = defaultUserEmail;
    }

    public static boolean isLoggedin() {
        return loggedin;
    }

    public static void setLoggedin(boolean loggedin) {
        accessKeys.loggedin = loggedin;
    }

    public static boolean isExitApplication() {
        return exitApplication;
    }

    public static void setExitApplication(boolean exitApplication) {
        accessKeys.exitApplication = exitApplication;
    }

    public static String getMessagingToken() {
        return messagingToken;
    }

    public static void setMessagingToken(String messagingToken) {
        accessKeys.messagingToken = messagingToken;
    }
}
