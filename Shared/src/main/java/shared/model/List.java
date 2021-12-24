package shared.model;

import java.util.LinkedList;

public class List {
    private long listID;
    private String name;
    private LinkedList<Long> members = new LinkedList<>();

    public List() {}

    public List(String name , long userId)
    {
        this.name = name;
        members.add(userId);
    }

    public long getListID() {
        return listID;
    }

    public void setListID(long listID) {
        this.listID = listID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Long> getMembers() {
        return members;
    }

    public void setMembers(LinkedList<Long> members) {
        this.members = members;
    }
}
