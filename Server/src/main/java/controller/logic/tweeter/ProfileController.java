package controller.logic.tweeter;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.form.ProfileInfoForm;
import shared.form.UserInfoForm;
import shared.model.Notification;
import shared.model.User;
import shared.response.EditInfoResponse;
import shared.response.GetNotificationResponse;
import shared.response.Response;
import shared.response.StringResponse;

import java.util.Date;
import java.util.LinkedList;

public class ProfileController {
    private User user;
    private User owner;
    private final NewTweetController newTweetController;
    private final EditInfoController editInfoController;
    private final TweetsListController tweetsListController;
    private final NotificationController notificationController;
    private final ListsController listsController;
    private final LinkedList<User> owners = new LinkedList<>();
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(ProfileController.class);


    public ProfileController()
    {
        newTweetController = new NewTweetController();
        editInfoController = new EditInfoController();
        tweetsListController = new TweetsListController("Profile");
        listsController = new ListsController();
        notificationController = new NotificationController();
    }

    public void setUser(User user) {
        this.user = user;
        newTweetController.setUser(user);
        editInfoController.setUser(user);
        listsController.setUser(user);
        tweetsListController.setViewer(user);
        notificationController.setUser(user);
    }

    public void setOwner(long ownerID) {
        boolean needChange = false;

        if(this.owner == null)
            needChange = true;

        else if(this.owner.getId() != ownerID)
            needChange = true;

        this.owner = dataBase.users.get(ownerID);

        if(needChange) {
            owners.add(this.owner);
            tweetsListController.setBaseList(owner.getTweets(), true);

        }
    }

    public void prevOwner()
    {
        if(owners.size() > 1)
            owners.removeLast();

        owner = owners.getLast();
    }

    public boolean prevAvailable()
    {
        return owners.size() > 1;
    }

    public String getName()
    {
        return owner.getName();
    }

    public String getLastName()
    {
        return owner.getLastName();
    }

    public String getUsername()
    {
        return owner.getUsername();
    }

    public String getLastSeen()
    {
        if((owner.getLastSeenMode() == 2 && owner.getFollowers().contains(user.getId())) || owner.getLastSeenMode() == 1)
            return owner.getLastSeen().compareTo(new Date()) == 0 ? "Online" : owner.getLastSeen().toString();

        return "Last seen recently";
    }

    public String getBio()
    {
        return owner.getBio();
    }

    public String getInformation()
    {
        String informationStr = "";

        if (!owner.getEmail().equals("") && owner.isVisibleEmail())
            informationStr += owner.getEmail();

        if (!owner.getPhone().equals("") && owner.isVisiblePhone())
            informationStr += " . " + owner.getPhone();

        if (owner.getDateOfBirth() != null && owner.isVisibleDateOfBirth()) {
            informationStr += " . " +  owner.getDateOfBirth();
        }

        return informationStr;
    }

    public String getStatus()
    {
        if(owner.getFollowers().contains(user.getId()))
            return "Following";

        if(owner.getRequested().contains(user.getId()))
            return "Requested";

        return "Follow";
    }

    public String getMuted()
    {
        if(user.getMuted().contains(owner.getId()))
            return "Unmute";

        return "Mute";
    }

    public String getBlocked()
    {
        if(user.getBlocked().contains(owner.getId()))
            return "Unblock";

        return "Block";
    }

    public String getPic()
    {
        return owner.getImageSTR();
    }

    public boolean visibleTweets()
    {
        if(owner.getBlocked().contains(user.getId()))
            return false;

        return !owner.isPrivate() || owner.getFollowers().contains(user.getId()) || owner.getId() == user.getId();
    }

    public StringResponse changeStatus()
    {
        String str = "";

        owner = dataBase.users.get(owner.getId());
        user = dataBase.users.get(user.getId());

        if(owner.getFollowers().contains(user.getId()))
        {
            logger.info("unfollowing user : " + owner.getId() + "/ by user : " + user.getId() + "/ in profileController");

            owner.getFollowers().remove((Long) user.getId());
            user.getFollowings().remove((Long) owner.getId());

            Notification notification = Notification.newUnfollow(user.getId());
            notification.setNotifID(dataBase.notifications.lastID()+1);

            owner.getSystemNotifications().add(notification.getNotifID());

            dataBase.notifications.save(notification);

            str = "Unfollowed";
        }

        else if(!owner.getRequested().contains(user.getId()) && !owner.getBlocked().contains(user.getId()))
        {
            if(owner.isPrivate())
            {
                logger.info("following user : " + owner.getId() + "/ by user : " + user.getId() + "/ in profileController");

                owner.getRequested().add(user.getId());

                Notification notification = Notification.newRequest(user.getId());
                notification.setNotifID(dataBase.notifications.lastID()+1);

                owner.getRequestsNotifications().add(notification.getNotifID());

                dataBase.notifications.save(notification);

                str = "Requested";
            }

            else
            {
                logger.info("requesting user : " + owner.getId() + "/ by user : " + user.getId() + "/ in profileController");

                owner.getFollowers().add(user.getId());
                user.getFollowings().add(owner.getId());

                long lastId = dataBase.notifications.lastID();
                Notification ownerNotification = Notification.newFollower(user.getId());
                Notification userNotification = Notification.newFollowing(owner.getId());
                ownerNotification.setNotifID(lastId + 1);
                userNotification.setNotifID(lastId + 2);

                owner.getSystemNotifications().add(ownerNotification.getNotifID());
                user.getSystemNotifications().add(userNotification.getNotifID());

                dataBase.notifications.save(ownerNotification);
                dataBase.notifications.save(userNotification);

                str = "Followed";
            }

        }

        dataBase.users.save(owner);
        dataBase.users.save(user);

        return new StringResponse(str);
    }


    public StringResponse changeBlockOwner()
    {
        String str;

        user = dataBase.users.get(user.getId());

        if(user.getBlocked().contains(owner.getId())) {
            logger.info("blocking user : " + owner.getId() + "/ by user : " + user.getId() + "/ in profileController");

            user.getBlocked().remove((Long) owner.getId());
            str = "Unblocked";
        }

        else
        {
            logger.info("unblocking user : " + owner.getId() + "/ by user : " + user.getId() + "/ in profileController");

            user.getBlocked().add(owner.getId());

            user.getFollowers().remove((Long) owner.getId());
            user.getFollowings().remove((Long) owner.getId());

            owner.getFollowings().remove((Long) user.getId());
            owner.getFollowers().remove((Long) user.getId());

            str = "Blocked";
        }

        dataBase.users.save(user);

        return new StringResponse(str);
    }

    public StringResponse changeMuteOwner()
    {

        String str;

        user = dataBase.users.get(user.getId());

        if(user.getMuted().contains(owner.getId())) {
            logger.info("unmuting user : " + owner.getId() + "/ by user : " + user.getId() + "/ in profileController");

            user.getMuted().remove((Long) owner.getId());
            str = "Unmuted";
        }

        else {
            logger.info("muting user : " + owner.getId() + "/ by user : " + user.getId() + "/ in profileController");

            user.getMuted().add(owner.getId());
            str = "Muted";
        }

        dataBase.users.save(user);
        return new StringResponse(str);
    }

    public ProfileInfoForm makeForm()
    {
        ProfileInfoForm form = new ProfileInfoForm();

        owner = dataBase.users.get(owner.getId());
        user = dataBase.users.get(user.getId());

        form.setMyProfile(owner.getUsername().equals(user.getUsername()));
        form.setMuted(getMuted());
        form.setBlocked(getBlocked());
        form.setFollowingStatus(getStatus());
        form.setVisibleTweets(visibleTweets());
        form.setPrivacyStatus(owner.isPrivate());
        form.setName(getName());
        form.setLastName(getLastName());
        form.setUsername(getUsername());
        form.setInfo(getInformation());
        form.setBio(getBio());
        form.setLastSeen(getLastSeen());
        form.setUserID(owner.getId());
        form.setImageSTR(getPic());

        return form;
    }

    public void newTweet(String text, String picPath, long retweetedFromID, long commentedUnderID)
    {
        newTweetController.newTweet(text, picPath, retweetedFromID, commentedUnderID);
    }

    public EditInfoResponse editInfo(UserInfoForm form)
    {
        return editInfoController.checkValidation(form);
    }

    public Response getList(String type)
    {
        return listsController.getList(type);
    }

    public GetNotificationResponse getNotification(String type)
    {
        return notificationController.getNotification(type);
    }

    public void responseToNotification(String response, long notifID)
    {
        if(response.equals("Accept"))
            notificationController.accept(notifID);

        if(response.equals("NotifiedDeny"))
            notificationController.deny(notifID, true);

        if(response.equals("NonNotifiedDeny"))
            notificationController.deny(notifID, false);
    }

    public void updateRootTweets()
    {
        owner = dataBase.users.get(owner.getId());
        tweetsListController.setBaseList(owner.getTweets(), false);
    }

    public TweetsListController getTweetsListController() {
        return tweetsListController;
    }


}
