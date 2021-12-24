package controller.logic.messenger;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Group;
import shared.model.List;
import shared.model.Messenger;
import shared.model.User;

public class NewMessengerListController {
    private User user;
    private Messenger messenger;
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(NewMessengerListController.class);


    public void setUser(User user)
    {
        this.user = user;
        this.messenger = dataBase.messenger.get(user.getMessengerID());
    }

    public void newList(String listName , String username)
    {
        logger.info("creating list : " + listName + "/ by user : " + user.getId() + "/ in newMessengerListController");

        User addedUser = dataBase.users.getByUsername(username);
        messenger = dataBase.messenger.get(messenger.getMessengerID());
        user = dataBase.users.get(user.getId());

        if(addedUser != null)
        {
            if(user.getFollowings().contains(addedUser.getId()) || addedUser.getFollowings().contains(user.getId()))
            {
                List list = new List(listName, addedUser.getId());
                list.setListID(dataBase.lists.lastID() + 1);
                messenger.getLists().add(list.getListID());
                dataBase.messenger.save(messenger);
                dataBase.lists.save(list);
            }

            else
                logger.debug("Couldn't have list : " + listName  + "/ with user : " + addedUser.getId() + "/ by user : " + user.getId() + "/ in newMessengerListController");

        }

        else
            logger.debug("Couldn't create list : " + listName + "/ by user : " + user.getId() + "/ in newMessengerListController");


    }

    public void newGroup(String gpName , String username)
    {
        logger.info("creating group : " + gpName + "/ by user : " + user.getId() + "/ in newMessengerListController");


        User addedUser = dataBase.users.getByUsername(username);
        messenger = dataBase.messenger.get(messenger.getMessengerID());
        user = dataBase.users.get(user.getId());

        if(addedUser != null)
        {
            if((user.getFollowings().contains(addedUser.getId()) || user.getFollowers().contains(addedUser.getId())) &&
                    !addedUser.getBlocked().contains(user.getId()) &&
                    !user.getBlocked().contains(addedUser.getId()) &&
                    addedUser.isActive())
            {
                Group group = new Group(gpName, addedUser.getId(), user.getId());
                group.setGroupID(dataBase.groups.lastID() + 1);
                messenger.getGroups().add(group.getGroupID());

                Messenger currentMessenger = dataBase.messenger.get(addedUser.getMessengerID());
                currentMessenger.getGroups().add(group.getGroupID());

                System.out.println("reached");

                dataBase.messenger.save(messenger);
                dataBase.messenger.save(currentMessenger);
                dataBase.groups.save(group);
            }

            else
                logger.debug("Couldn't have group : " + gpName  + "/ with user : " + addedUser.getId() + "/ by user : " + user.getId() + "/ in newMessengerListController");

        }

        else
            logger.debug("Couldn't create group : " + gpName + "/ by user : " + user.getId() + "/ in newMessengerListController");
    }
}
