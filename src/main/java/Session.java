package main.java;

public class Session {
    private static int userId = -1;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int id) {
        userId = id;
    }

    public static boolean isLoggedIn() {
        return userId > 0;
    }

    public static void logout() {
        userId = -1;
    }
}
