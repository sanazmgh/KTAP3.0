package controller.logic.messenger;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Group;
import shared.model.Message;
import shared.model.Messenger;
import shared.model.User;
import shared.response.ViewChatResponse;
import shared.util.Pair;

import java.util.*;

public class ChatController {
    private User user;
    private Messenger messenger;
    private Group group;
    private final MessageController messageController;

    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(ChatController.class);

    public ChatController()
    {
        messageController = new MessageController();
    }

    public void setUser(User user)
    {
        this.user = user;
        messenger = dataBase.messenger.get(user.getMessengerID());
        messageController.setUser(user);
    }

    public void setGroup(long groupID) {
        this.group = dataBase.groups.get(groupID);
    }

    public User getUser() {
        return user;
    }

    public int getUnread()
    {
        return group.getUnread(user.getId());
    }

    public String getName()
    {
        String name = group.getName();

        if(group.getMembers().size() == 2)
        {
            long otherUserID = group.getMembers().get(0).getKey().equals(user.getId()) ?
                    group.getMembers().get(1).getKey() : group.getMembers().get(0).getKey();
            name = dataBase.users.get(otherUserID).getUsername();
        }

        return name;
    }

    public LinkedList<String> getUserNames()
    {
        LinkedList<String> names = new LinkedList<>();
        //group = dataBase.groups.get(group.getGroupID());

        for(int i=0 ; i<group.getMembers().size() ; i++)
        {
            User currentUser = dataBase.users.get(group.getMembers().get(i).getKey());
            names.add(currentUser.getUsername());
        }

        return names;
    }

    public LinkedList<Message> getMessages()
    {
        LinkedList<Message> messages = new LinkedList<>();
        //group = dataBase.groups.get(group.getGroupID());

        for (long messageID : group.getMessages())
        {
            Message currentMessage = dataBase.messages.get(messageID);
            User currentUser = dataBase.users.get(currentMessage.getSenderId());
            currentMessage.setSenderName(currentUser.getUsername());

            if(currentMessage.getStatus() != 4)
            {
                if(currentMessage.getSenderId() != user.getId())
                {
                    currentMessage.setStatus(4);
                    dataBase.messages.save(currentMessage);
                }
            }

            messages.add(currentMessage);
        }

        Collections.reverse(messages);
        return messages;
    }

    public boolean canText()
    {
        user = dataBase.users.get(user.getId());

        if(group.getMembers().size() != 2)
            return true;

        User otherUser;

        if(group.getMembers().get(0).getKey() == user.getId())
            otherUser = dataBase.users.get(group.getMembers().get(1).getKey());

        else
            otherUser = dataBase.users.get(group.getMembers().get(0).getKey());

        return !otherUser.getBlocked().contains(user.getId()) &&
                !user.getBlocked().contains(otherUser.getId()) &&
                otherUser.isActive();
    }

    public ViewChatResponse getChat()
    {
        seenMessages();
        return new ViewChatResponse(getName(), group.getGroupID(), getUserNames(), getMessages(), !canText());
    }

    public void addMember(String username)
    {
        User addedUser = dataBase.users.getByUsername(username);
        group = dataBase.groups.get(group.getGroupID());
        user = dataBase.users.get(user.getId());

        if(addedUser != null)
        {
            logger.info("Adding user : " + addedUser.getId() + "/ in group : " + group.getGroupID() + "/ by user : " + user.getId() + "/ in chatController");

            if(!group.contains(addedUser.getId()) &&
                    (user.getFollowings().contains(addedUser.getId()) || user.getFollowers().contains(addedUser.getId())) &&
                    !addedUser.getBlocked().contains(user.getId()) &&
                    !user.getBlocked().contains(addedUser.getId()) &&
                    addedUser.isActive())
            {
                group.getMembers().add(new Pair(addedUser.getId(), 0));
                Messenger currentMessenger = dataBase.messenger.get(addedUser.getMessengerID());
                currentMessenger.getGroups().add(group.getGroupID());
                dataBase.messenger.save(currentMessenger);
            }
        }

        else
            logger.debug("Couldn't add : " + username + "/ in group : " + group.getGroupID() + "/ by user : " + user.getId() + "/ in chatController");


        dataBase.groups.save(group);
    }

    public void leaveTheChat()
    {
        logger.info("Leaving group : " + group.getGroupID() + "/ by user : " + user.getId() + "/ in chatController");

        messenger = dataBase.messenger.get(messenger.getMessengerID());
        group = dataBase.groups.get(group.getGroupID());

        messenger.getGroups().remove(group.getGroupID());

        group.leave(user.getId());

        dataBase.messenger.save(messenger);
        dataBase.groups.save(group);
    }

    public void seenMessages()
    {
        group = dataBase.groups.get(group.getGroupID());
        group.removeNotif(user.getId());
        dataBase.groups.save(group);
    }

    public void sendMessage(long editingID, String text, String imageSTR, boolean forwarded, long forwardedFrom, String date)
    {
        //logger.info("sending message in group : " + group.getGroupID() + "/ by user : " + user.getId() + "/ in chatController");

        group = dataBase.groups.get(group.getGroupID());

        if(editingID != 0)
        {
            messageController.setMessage(editingID);
            messageController.editText(text);
            return;
        }

        User forwardedUser = dataBase.users.get(forwardedFrom);
        String extraInfo = "";
        if(forwardedUser != null)
            extraInfo = forwardedUser.getUsername();



        Message currentMessage = new Message(user.getId(), user.getImageSTR(), text, imageSTR, forwarded, extraInfo);
        currentMessage.setMessageID(dataBase.messages.lastID() + 1);
        currentMessage.setStatus(2);

        if(date.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))
        {
            String[] times = date.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));

            currentMessage.setSchedule(calendar.getTime());
        }

        else
            currentMessage.setSchedule(new Date());

        Date currentDate = new Date();

        if(currentDate.before(currentMessage.getSchedule()))
        {
            group.getScheduledMessages().add(currentMessage.getMessageID());
        }

        else
            group.newMessage(currentMessage.getMessageID());

        dataBase.messages.save(currentMessage);
        dataBase.groups.save(group);
    }
}
