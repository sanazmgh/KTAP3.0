package shared.model;

public class Notification {

    private long senderID;
    private String text;
    private long notifID;

    public Notification() {}

    public Notification(long senderID, String text)
    {
        this.senderID = senderID;
        this.text = text;
    }

    public long getNotifID() {
        return notifID;
    }

    public void setNotifID(long notifID) {
        this.notifID = notifID;
    }

    public long getSenderID() {
        return senderID;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static Notification newRequest(long senderID)
    {
        return new Notification(senderID , "This user requested to follow you.");
    }

    public static Notification newFollowing(long senderID)
    {
        return new Notification(senderID , "You started following this user.");
    }

    public static Notification newDenied(long senderID)
    {
        return new Notification(senderID , "This user denied your follow request.");
    }

    public static Notification newFollower(long senderID)
    {
        return new Notification(senderID , "This user started following you.");
    }

    public static Notification newUnfollow(long senderID)
    {
        return new Notification(senderID , "This user stopped following you");
    }
}
