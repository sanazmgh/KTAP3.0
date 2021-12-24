package shared.model;

import java.util.LinkedList;

public class Messenger {
    private long messengerID;
    private long userId;
    private LinkedList<Long> groups = new LinkedList<>();
    private LinkedList<Long> lists = new LinkedList<>();
    private long savedMessages;

    public Messenger() {}

    public Messenger(User user, long savedMessageID)
    {
        this.userId = user.getId();
        this.savedMessages = savedMessageID;
        groups.add(savedMessageID);
    }

    public long getMessengerID() {
        return messengerID;
    }

    public void setMessengerID(long messengerID) {
        this.messengerID = messengerID;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LinkedList<Long> getGroups() {
        return groups;
    }

    public void setGroups(LinkedList<Long> groups) {
        this.groups = groups;
    }

    public LinkedList<Long> getLists() {
        return lists;
    }

    public void setLists(LinkedList<Long> lists) {
        this.lists = lists;
    }

    public Long getSavedMessages() {
        return savedMessages;
    }

    public void setSavedMessages(long savedMessages) {
        this.savedMessages = savedMessages;
    }
}
