package shared.form;

import java.util.LinkedList;

public class GroupDataForm {
    private final String gpName;
    private final long gpID;
    private final int unread;
    private LinkedList<String> usernames;
    private LinkedList<Long> messages;
    private boolean blocked;

    public GroupDataForm(String gpName, long gpID, int unread, LinkedList<String> usernames, LinkedList<Long> messages, boolean blocked) {
        this.gpName = gpName;
        this.gpID = gpID;
        this.unread = unread;
        this.usernames = usernames;
        this.messages = messages;
        this.blocked = blocked;
    }

    public String getGpName() {
        return gpName;
    }

    public long getGpID() {
        return gpID;
    }

    public int getUnread() {
        return unread;
    }

    public LinkedList<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(LinkedList<String> usernames) {
        this.usernames = usernames;
    }

    public LinkedList<Long> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<Long> messages) {
        this.messages = messages;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
