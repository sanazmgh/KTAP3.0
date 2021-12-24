package controller.logic.tweeter;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.form.ProfileInfoForm;
import shared.model.Notification;
import shared.model.User;
import shared.response.GetNotificationResponse;

import java.util.LinkedList;

public class NotificationController {
    private User user;
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(NotificationController.class);

    public void setUser(User user) {
        this.user = user;
    }

    public GetNotificationResponse getNotification(String type)
    {
        LinkedList<Long> currentList;

        LinkedList<Notification> notifications = new LinkedList<>();
        LinkedList<ProfileInfoForm> users = new LinkedList<>();

        user = dataBase.users.get(user.getId());

        if(type.equals("Request"))
            currentList = user.getRequestsNotifications();

        else
            currentList = user.getSystemNotifications();

        for(long i : currentList)
        {
            Notification currentNotif = dataBase.notifications.get(i);
            User currentSender = dataBase.users.get(currentNotif.getSenderID());
            ProfileInfoForm form = new ProfileInfoForm();

            String followingStatus = "Follow";

            if(user.getFollowings().contains(currentSender.getId()))
                followingStatus = "Unfollow";

            if(user.getRequested().contains(currentSender.getId()))
                followingStatus = "Requested";

            form.setName(currentSender.getName());
            form.setLastName(currentSender.getLastName());
            form.setUsername(currentSender.getUsername());
            form.setFollowingStatus(followingStatus);
            form.setBlocked(user.getBlocked().contains(currentSender.getId()) ? "Unblock" : "Block");
            form.setMuted(user.getMuted().contains(currentSender.getId()) ? "Unmute" : "Mute");
            form.setUserID(currentSender.getId());
            form.setMyProfile(user.getUsername().equals(currentSender.getUsername()));
            form.setImageSTR(currentSender.getImageSTR());

            notifications.add(currentNotif);
            users.add(form);
        }

        return new GetNotificationResponse(notifications, users);
    }

    public void accept(long notifID)
    {
        logger.info("accepting notif : " + notifID + "/ by user : " + user.getId() + "/ in notificationController");

        user = dataBase.users.get(user.getId());

        Notification notification = dataBase.notifications.get(notifID);
        User sender = dataBase.users.get(notification.getSenderID());

        long lastID = dataBase.notifications.lastID();

        Notification senderNotification = Notification.newFollowing(user.getId());
        senderNotification.setNotifID(lastID+1);

        Notification receiverNotification = Notification.newFollower(sender.getId());
        receiverNotification.setNotifID(lastID+2);

        user.getRequestsNotifications().remove((Long) notification.getNotifID());
        user.getRequested().remove(sender.getId());
        user.getSystemNotifications().add(receiverNotification.getNotifID());
        sender.getSystemNotifications().add(senderNotification.getNotifID());

        user.getFollowers().add(sender.getId());
        sender.getFollowings().add(user.getId());

        dataBase.users.save(user);
        dataBase.users.save(sender);

        dataBase.notifications.save(senderNotification);
        dataBase.notifications.save(receiverNotification);
    }


    public void deny(long notifID, boolean notify)
    {
        logger.info("denying notif : " + notifID + "/ by user : " + user.getId() + "/ in notificationController");

        user = dataBase.users.get(user.getId());

        Notification notification = dataBase.notifications.get(notifID);
        User sender = dataBase.users.get(notification.getSenderID());

        user.getRequestsNotifications().remove((Long) notification.getNotifID());
        user.getRequested().remove(sender.getId());

        if(notify)
        {
            logger.info("informing user : " + sender.getId() + "/ of denied notif : " + notifID + "/ by user : " + user.getId() + "/ in notificationController");

            Notification senderNotification = Notification.newDenied(user.getId());
            senderNotification.setNotifID(dataBase.notifications.lastID()+1);
            sender.getSystemNotifications().add(senderNotification.getNotifID());

            dataBase.notifications.save(senderNotification);
        }

        dataBase.users.save(sender);
        dataBase.users.save(user);
    }
}
