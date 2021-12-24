package util;

public class Status {
    private static boolean isOnline;

    public static boolean isIsOnline() {
        return isOnline;
    }

    public static void setIsOnline(boolean isOnline) {
        Status.isOnline = isOnline;
    }
}
