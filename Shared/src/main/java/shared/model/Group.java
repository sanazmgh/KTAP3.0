package shared.model;

import shared.util.Pair;

import java.util.Collections;
import java.util.LinkedList;

public class Group {
    private long groupID;
    private String name;
    private LinkedList<Pair> members = new LinkedList<>(); //List of <userId , noOfUnread>
    private LinkedList<Long> messages = new LinkedList<>();
    private LinkedList<Long> scheduledMessages = new LinkedList<>();
    //static private final Logger logger = LogManager.getLogger(Group.class);

    public Group() {}

    public Group (String name , long user1Id , long user2Id)
    {
        this.name = name;
        members.add(new Pair(user1Id , 0));
        members.add(new Pair(user2Id , 0));
    }

    public Group(String name, long userId)
    {
        this.name = name;
        members.add(new Pair(userId , 0));
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Pair> getMembers() {
        return members;
    }

    public void setMembers(LinkedList<Pair> members) {
        this.members = members;
    }

    public LinkedList<Long> getMessages() {
        return messages;
    }

    public void setMessages(LinkedList<Long> messages) {
        this.messages = messages;
    }

    public LinkedList<Long> getScheduledMessages() {
        Collections.sort(scheduledMessages);
        return scheduledMessages;
    }

    public void setScheduledMessages(LinkedList<Long> scheduledMessages) {
        this.scheduledMessages = scheduledMessages;
    }

    public int getUnread(long userID)
    {
        for (Pair member : members)
            if (member.getKey() == userID)
                return member.getValue();

        return 0;
    }

    public boolean contains(Long Id)
    {
        for(Pair pair : members )
        {
            if(pair.getKey() == Id)
                return true;
        }

        return false;
    }

    public void newMessage(long messageID)
    {
        for(int i=0 ; i<members.size() ; i++)
        {
            Pair pair = members.get(i);
            members.set(i, new Pair(pair.getKey(), pair.getValue()+1));
        }
        messages.add(messageID);
    }

    public void removeNotif(long userId)
    {
        for(int i=0 ; i<members.size() ; i++)
        {
            Pair pair = members.get(i);

            if(pair.getKey() == userId)
            {
                members.set(i, new Pair(pair.getKey(), 0));
            }
        }
    }

    public void leave(long userID)
    {
        Pair ans = null;

        for (Pair pair : members) {
            if (pair.getKey() == userID) {
                ans = pair;
            }
        }

        if(ans != null)
            members.remove(ans);
    }
}
