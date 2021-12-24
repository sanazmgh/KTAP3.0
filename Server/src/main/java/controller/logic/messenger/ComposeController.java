package controller.logic.messenger;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.*;

import java.util.LinkedList;

public class ComposeController {
    private User user;
    private Messenger messenger;
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(ComposeController.class);

    public void setUser(User user)
    {
        this.user = user;
        this.messenger = dataBase.messenger.get(user.getMessengerID());
    }

    public Group getPrivateChat(String name)
    {
        User currentUser = dataBase.users.getByUsername(name);
        user = dataBase.users.get(user.getId());
        this.messenger = dataBase.messenger.get(user.getMessengerID());

        Group pvGroup = null;

        if(currentUser != null)
        {
            if(!currentUser.getBlocked().contains(user.getId()) &&
                    !user.getBlocked().contains(currentUser.getId()) &&
                    user.getFollowings().contains(currentUser.getId()))
            {
                for(long groupID : messenger.getGroups())
                {
                    Group group = dataBase.groups.get(groupID);

                    if(group.getMembers().size() == 2)
                    {
                        User user1 = dataBase.users.get(group.getMembers().get(0).getKey());
                        User user2 = dataBase.users.get(group.getMembers().get(0).getKey());

                        if(user1.getUsername().equals(name) || user2.getUsername().equals(name))
                            pvGroup = group;
                    }
                }

                if (pvGroup == null) {
                    pvGroup = new Group(name, user.getId(), currentUser.getId());
                    pvGroup.setGroupID(dataBase.groups.lastID() + 1);

                    messenger.getGroups().add(pvGroup.getGroupID());
                    Messenger currentUserMessenger = dataBase.messenger.get(currentUser.getMessengerID());
                    currentUserMessenger.getGroups().add(pvGroup.getGroupID());

                    dataBase.groups.save(pvGroup);
                    dataBase.users.save(user);
                    dataBase.users.save(currentUser);
                }
            }
        }

        return pvGroup;
    }

    public void compose(String text, String attachmentPath, LinkedList<String> names, boolean forwarded, String extraInfo)
    {
        logger.info("Composing message by user : " + user.getId() + "/ to chats : " + names + "/ in composeController");

        user = dataBase.users.get(user.getId());

        Message message = new Message(user.getId(), user.getImageSTR(), text, attachmentPath, forwarded, extraInfo);
        message.setMessageID(dataBase.messages.lastID() + 1);
        message.setStatus(2);

        LinkedList<Group> groups = new LinkedList<>();

        for(String name : names)
        {
            User currentUser = dataBase.users.getByUsername(name);
            List currentList = dataBase.lists.getByName(name);
            Group currentGroup = dataBase.groups.getByName(name);

            if(currentUser != null && currentGroup == null)
                currentGroup = getPrivateChat(name);

            if(currentList != null)
            {
                for(int i=0 ; i<currentList.getMembers().size() ; i++)
                {
                    User tempUser = dataBase.users.get(currentList.getMembers().get(i));

                    if(tempUser != null)
                    {
                        Group tempGroup = getPrivateChat(tempUser.getUsername());

                        if(!groups.contains(tempGroup))
                            groups.add(tempGroup);
                    }
                }
            }

            else
                logger.debug("Couldn't find a list by name : " + name + "/ to compose by user " + user.getId() + "/ in composeController");

            if(currentGroup != null)
            {
                if(!groups.contains(currentGroup))
                    groups.add(currentGroup);
            }

            else
                logger.debug("Couldn't find a group by name : " + name + "/ to compose by user " + user.getId() + "/ in composeController");

        }

        for(Group group : groups)
        {
            group.newMessage(message.getMessageID());
            dataBase.messages.save(message);
            dataBase.groups.save(group);
        }
    }
}
