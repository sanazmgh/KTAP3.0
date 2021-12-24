package controller.logic.tweeter;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.User;
import shared.response.GetSettingsInfoResponse;
import shared.response.Response;
import shared.response.SettingsResponse;
import shared.response.StringResponse;

public class SettingsController {
    private User user;
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(SettingsController.class);

    public void setUser(User user) {
        this.user = user;
    }

    public Response getEvent(String type, String action)
    {

        switch (type) {
            case "Privacy":
                setPrivacy(action);
                return new StringResponse("Changed privacy");
            case "LastSeen":
                setLastSeen(action);
                return new StringResponse("Changed last seen");
            case "DeleteAccount":
                return new SettingsResponse(deleteAccount(action));
            case "DeactiveAccount":
                return new SettingsResponse(deactiveAccount(action));
        }

        return null;
    }

    public GetSettingsInfoResponse getInfo()
    {
        user = dataBase.users.get(user.getId());
        return new GetSettingsInfoResponse(user.isPrivate(), user.getLastSeenMode());
    }

    public void setPrivacy(String action)
    {
        user = dataBase.users.get(user.getId());
        logger.warn("user : " + user.getId() + "/ set privacy to : " + action + "/ in settingsController");

        user.setPrivate(action.equals("Private"));
        dataBase.users.save(user);

    }

    public void setLastSeen(String action)
    {
        logger.warn("user : " + user.getId() + "/ set lastSeen status to : " + action + "/ in settingsController");

        user = dataBase.users.get(user.getId());

        int mode = 0;
        if(action.equals("Everyone"))
            mode = 1;
        else if(action.equals("Followers"))
            mode = 2;

        user.setLastSeenMode(mode);
        dataBase.users.save(user);
    }

    public boolean deleteAccount(String pass)
    {
        if(pass.equals(user.getPassword()))
        {
            logger.warn("deleting account by user : " + user.getId() +  "/ in settingsController");

            user.setDeleted(true);
            user.setActive(false);
            user.setUsername("DeletedAccount");
            user.setEmail("DeletedAccount");
            user.setName("Deleted");
            user.setUsername("Account");

            for(int i=0 ; i<user.getFollowers().size() ; i++)
            {
                User currentUser = dataBase.users.get(user.getFollowers().get(i));
                currentUser.getFollowings().remove((Long) user.getId());
                dataBase.users.save(currentUser);
            }

            for(int i=0 ; i<user.getFollowings().size() ; i++)
            {
                User currentUser = dataBase.users.get(user.getFollowings().get(i));
                currentUser.getFollowers().remove((Long) user.getId());
                dataBase.users.save(currentUser);
            }

            user.getFollowings().clear();
            user.getFollowers().clear();

            dataBase.users.save(user);
            return true;
        }


        logger.warn("deleting account by user : " + user.getId() +  "went wrong" + "/ in settingsController");

        return false;
    }

    public boolean deactiveAccount(String pass)
    {
        user = dataBase.users.get(user.getId());

        if(pass.equals(user.getPassword()))
        {
            logger.warn("deactivating account by user : " + user.getId() +  "/ in settingsController");

            user.setActive(false);
            dataBase.users.save(user);

            return true;
        }

        logger.warn("deactivating account by user : " + user.getId() +  "went wrong" + "/ in settingsController");

        return false;
    }
}
