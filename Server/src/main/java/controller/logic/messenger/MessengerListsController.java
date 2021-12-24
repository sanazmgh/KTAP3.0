package controller.logic.messenger;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.List;
import shared.model.Messenger;
import shared.model.User;

public class MessengerListsController {
    private User user;
    private Messenger messenger;
    private List list;
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(MessengerListsController.class);

    public void setUser(User user )
    {
        this.user = user;
        this.messenger = dataBase.messenger.get(user.getMessengerID());
    }

    public void setList(long listID) {
        this.list = dataBase.lists.get(listID);
    }

    public void add(String username)
    {
        User addedUser = dataBase.users.getByUsername(username);
        list = dataBase.lists.get(list.getListID());

        if(addedUser != null)
        {
            logger.info("adding user : " + addedUser.getId() + "/ in list : " + list.getListID() + "/ by user : " + user.getId() + "/ in messengerListController");

            list.getMembers().add(addedUser.getId());

            dataBase.lists.save(list);
        }

        else
        {
            logger.debug("Couldn't add : " + username + "/ in list : " + list.getListID() + "/ by user : " + user.getId() + "/ in messengerListController");
        }
    }

    public void remove(String username)
    {
        User removedUser = dataBase.users.getByUsername(username);

        if(removedUser != null)
        {
            logger.info("removing user : " + removedUser.getId() + "/ from list : " + list.getListID() + "/ by user : " + user.getId() + "/ in messengerListController");

            list.getMembers().remove((Long)removedUser.getId());

            dataBase.lists.save(list);
        }

        else
        {
            logger.debug("Couldn't remove : " + username + "/ from list : " + list.getListID() + "/ by user : " + user.getId() + "/ in messengerListController");
        }
    }

    public String getName()
    {
        return list.getName();
    }

    public String getMembers()
    {
        StringBuilder str = new StringBuilder();
        list = dataBase.lists.get(list.getListID());

        for(int i=0 ; i<list.getMembers().size() ; i++)
        {
            User currentUser = dataBase.users.get(list.getMembers().get(i));
            str.append(currentUser.getName()).append(", ");
        }

        if(!str.toString().equals(""))
            return str.substring(0 , str.length()-2);

        return str.toString();
    }
}
