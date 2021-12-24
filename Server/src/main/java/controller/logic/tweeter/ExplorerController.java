package controller.logic.tweeter;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.form.ProfileInfoForm;
import shared.model.User;
import shared.response.SearchResponse;
import shared.response.ViewTweetResponse;

import java.util.Collections;
import java.util.LinkedList;

public class ExplorerController {

    private User user;
    private final LinkedList<Long> explorer = new LinkedList<>();
    private final TweetsListController tweetsListController;
    private final DataBase dataBase = DataBase.getDataBase();

    public ExplorerController(){
        tweetsListController = new TweetsListController("Explorer");
    }

    public void setUser(User user) {
        this.user = user;
        tweetsListController.setViewer(user);
    }

    public ViewTweetResponse createExplorer()
    {
        user = dataBase.users.get(user.getId());
        explorer.clear();

        for(User currentUser : dataBase.users.all())
        {
            if(!user.getFollowings().contains(currentUser.getId()) &&
                    !user.getMuted().contains(currentUser.getId()) &&
                    currentUser.getId() != user.getId() &&
                    currentUser.isActive() &&
                    !user.getBlocked().contains(currentUser.getId()) &&
                    !currentUser.getBlocked().contains(user.getId()) &&
                    !currentUser.isPrivate() &&
                    !currentUser.getUsername().equals(user.getUsername()))
                explorer.addAll(currentUser.getTweets());
        }

        Collections.sort(explorer);
        Collections.reverse(explorer);
        tweetsListController.setBaseList(explorer, false);

        return tweetsListController.getCurrentList();
    }

    public TweetsListController getTweetsListController() {
        return tweetsListController;
    }
    
    public SearchResponse search(String username)
    {
        User searched =  dataBase.users.getByUsername(username);
        user = dataBase.users.get(user.getId());

        if(searched != null) {
            if(searched.isActive())
            {
                ProfileInfoForm form = new ProfileInfoForm();

                String followingStatus = "Follow";

                if (user.getFollowings().contains(searched.getId()))
                    followingStatus = "Unfollow";

                if (user.getRequested().contains(searched.getId()))
                    followingStatus = "Requested";

                form.setName(searched.getName());
                form.setLastName(searched.getLastName());
                form.setUsername(searched.getUsername());
                form.setFollowingStatus(followingStatus);
                form.setBlocked(user.getBlocked().contains(searched.getId()) ? "Unblock" : "Block");
                form.setMuted(user.getMuted().contains(searched.getId()) ? "Unmute" : "Mute");
                form.setUserID(searched.getId());
                form.setImageSTR(searched.getImageSTR());
                form.setMyProfile(user.getUsername().equals(searched.getUsername()));

                return new SearchResponse(form, true);
            }
        }

        return new SearchResponse(null, false);
    }
}
