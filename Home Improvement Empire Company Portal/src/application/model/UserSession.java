package application.model;

public class UserSession {

    private static Credential loggedInUser;

    private UserSession() {
        // Prevent creating UserSession objects
    }

    public static void setLoggedInUser(Credential credential) {
        loggedInUser = credential;
    }

    public static Credential getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static void logout() {
        loggedInUser = null;
    }
}