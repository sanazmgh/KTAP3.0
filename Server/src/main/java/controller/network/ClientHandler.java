package controller.network;

import DataBase.DataBase;
import controller.logic.messenger.MessengerController;
import controller.logic.tweeter.*;
import shared.event.Event;
import shared.event.EventVisitor;
import shared.form.LoginForm;
import shared.form.UserInfoForm;
import shared.model.User;
import shared.response.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.security.SecureRandom;
import java.util.Date;
import java.util.LinkedList;

public class ClientHandler extends Thread implements EventVisitor {
    private final SocketResponder sender;
    private final AuthenticationController authenticationController;
    private final ProfileController profileController;
    private final TimelineController timelineController;
    private final ExplorerController explorerController;
    private final SettingsController settingsController;
    private final MessengerController messengerController;
    private final TweetController tweetController;
    private User user;
    private int authToken;
    private final DataBase dataBase = DataBase.getDataBase();
    private final Timer timer;


    public ClientHandler(SocketResponder sender){
        this.sender = sender;
        authenticationController = new AuthenticationController();
        profileController = new ProfileController();
        timelineController = new TimelineController();
        explorerController = new ExplorerController();
        settingsController = new SettingsController();
        messengerController = new MessengerController();
        tweetController = new TweetController();
        timer = new Timer(60000, this::updateUser);
    }

    public void setUser(User user) {
        this.user = user;
        profileController.setUser(user);
        timelineController.setUser(user);
        explorerController.setUser(user);
        settingsController.setUser(user);
        tweetController.setViewer(user);
        messengerController.setUser(user);

        timer.start();
    }

    public void updateUser(ActionEvent e)
    {
        if(e.getSource() == timer)
        {
            if (user != null) {
                user = dataBase.users.get(user.getId());
                user.setLastSeen(new Date());
                dataBase.users.save(user);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void run()
    {
        while (true) {
            Event event = sender.getEvent();

            if(event != null) {
                sender.sendResponse(event.visit(this));
            }
        }
    }

    @Override
    public Response signUp(UserInfoForm userInfoForm) {
        AuthenticationResponse response = authenticationController.signUp(userInfoForm);

        if(authenticationController.getUser()!= null)
        {
            setUser(authenticationController.getUser());

            SecureRandom random = new SecureRandom();
            this.authToken = random.nextInt();

            response.setAutToken(this.authToken);
        }

        return response;
    }

    @Override
    public Response login(LoginForm loginForm) {

        AuthenticationResponse response = authenticationController.login(loginForm);
        if(authenticationController.getUser()!= null)
        {
            setUser(authenticationController.getUser());

            SecureRandom random = new SecureRandom();
            this.authToken = random.nextInt();

            //System.out.println(authToken);

            response.setAutToken(this.authToken);
            response.setUser(user);
        }

        return response;
    }

    @Override
    public Response getProfile(long autToken, long ownerID) {

        if(this.authToken != autToken)
            return null;

        profileController.setOwner(ownerID);
        return new GetProfileResponse(profileController.makeForm(), profileController.prevAvailable());
    }

    @Override
    public Response getPrevStep(long autToken) {

        if(this.authToken != autToken)
            return null;

        profileController.prevOwner();
        return new GetProfileResponse(profileController.makeForm(), profileController.prevAvailable());
    }

    @Override
    public Response getSettingsInfo(long autToken) {

        if(this.authToken != autToken)
            return null;

        return settingsController.getInfo();
    }

    @Override
    public Response getSettingsAction(long autToken, String type, String action) {

        if(this.authToken != autToken)
            return null;

        return settingsController.getEvent(type, action);
    }

    @Override
    public Response getMessenger(long autToken) {

        if(this.authToken != autToken)
            return null;

        return messengerController.getChats();
    }

    @Override
    public Response submitMessage(long autToken, long editingID, String text, String imagePath, boolean forwarded, long forwardedFromID, long gpID, String date) {
        if(this.authToken != autToken)
            return null;

        messengerController.sendAMessage(editingID, text, imagePath, forwarded, forwardedFromID, gpID, date);
        return new StringResponse("Submitted");
    }

    @Override
    public Response composeMessage(long autToken, String text, String attachmentPath, LinkedList<String> names, boolean forwarded, String extraInfo) {

        if(this.authToken != autToken)
            return null;

        messengerController.getComposeController().compose(text, attachmentPath, names, forwarded, extraInfo);
        return new StringResponse("Composed");
    }

    @Override
    public Response getGroup(long autToken, long gpID) {

        if(this.authToken != autToken)
            return null;

        return messengerController.getGroup(gpID);
    }

    @Override
    public Response newGroup(long autToken, String gpName, String username, String type) {

        if(this.authToken != autToken)
            return null;

        if(type.equals("Groups"))
            messengerController.getNewMessengerListController().newGroup(gpName, username);

        else
            messengerController.getNewMessengerListController().newList(gpName, username);

        return new StringResponse("New List");
    }

    @Override
    public Response changeMembers(long autToken, String action, String type, long gpID, String username) {

        if(this.authToken != autToken)
            return null;

        if(type.equals("Lists"))
            messengerController.changeMembersInList(action, gpID, username);

        else
            messengerController.changeMembersInGroup(action, gpID, username);

        return new StringResponse("Changed members");
    }

    @Override
    public Response changeInfo(long autToken, UserInfoForm userInfoForm) {

        if(this.authToken != autToken)
            return null;

        return profileController.editInfo(userInfoForm);
    }

    @Override
    public Response changeStatus(long autToken, long ownerID, String type) {

        if(this.authToken != autToken)
            return null;

        profileController.setOwner(ownerID);

        switch (type) {
            case "Follow" -> {
                return profileController.changeStatus();
            }
            case "Mute" -> {
                return profileController.changeMuteOwner();
            }
            case "Block" -> {
                return profileController.changeBlockOwner();
            }
        }

        return new StringResponse("");
    }

    @Override
    public Response getLists(long autToken, String type)
    {

        if(this.authToken != autToken)
            return null;

        if(type.equals("Lists") || type.equals("Groups"))
            return messengerController.getLists(type);

        return profileController.getList(type);
    }

    @Override
    public Response tweetActions(long autToken, String action, long tweetID) {

        if(this.authToken != autToken)
            return null;

        return profileController.getTweetsListController().getTweetController().getEvent(action, tweetID);
    }

    @Override
    public Response newComment(long autToken, long tweetID) {

        if(this.authToken != autToken)
            return null;

        return new WriteCommentResponse(tweetID);
    }

    @Override
    public Response getTweetList(long autToken, String where, long tweetID, String action) {

        if(this.authToken != autToken)
            return null;


        if(where.equals("Profile"))
        {
            profileController.updateRootTweets();

            if (action.equals("Current"))
                return profileController.getTweetsListController().getCurrentList();

            if (action.equals("Comment"))
                return profileController.getTweetsListController().makeNextList(tweetID);

            if (action.equals("Prev tweet"))
                return profileController.getTweetsListController().getPrevList();
        }

        if(where.equals("Timeline"))
        {
            //timelineController.updateRootTweets();

            if (action.equals("Current"))
                return timelineController.getTweetsListController().getCurrentList();

            if (action.equals("Comment"))
                return timelineController.getTweetsListController().makeNextList(tweetID);

            if (action.equals("Prev tweet"))
                return timelineController.getTweetsListController().getPrevList();
        }

        else
        {
            //profileController.updateRootTweets();

            if (action.equals("Current"))
                return explorerController.getTweetsListController().getCurrentList();

            if (action.equals("Comment"))
                return explorerController.getTweetsListController().makeNextList(tweetID);

            if (action.equals("Prev tweet"))
                return explorerController.getTweetsListController().getPrevList();
        }

        return new ViewTweetResponse(new LinkedList<>(), where, true);

    }

    @Override
    public Response getTimelineTweets(long autToken, String type) {

        if(this.authToken != autToken)
            return null;

        if(type.equals("Timeline"))
            return timelineController.createTimeLine();

        else
            return explorerController.createExplorer();
    }

    @Override
    public Response searchUsername(long autToken, String username) {

        if(this.authToken != autToken)
            return null;

        return explorerController.search(username);
    }

    @Override
    public Response getNotification(long autToken, String type) {

        if(this.authToken != autToken)
            return null;

        return profileController.getNotification(type);
    }

    @Override
    public Response responseToNotif(long autToken, String response, long notifID) {

        if(this.authToken != autToken)
            return null;

        profileController.responseToNotification(response, notifID);
        return getNotification(autToken, "Request");
    }

    @Override
    public Response newTweet(long autToken, String text, String picPath, long retweetedFromID, long commentedUnderID) {

        if(this.authToken != autToken)
            return null;

        profileController.newTweet(text, picPath, retweetedFromID, commentedUnderID);
        return getProfile(authToken, authenticationController.getUser().getId());
    }

}
