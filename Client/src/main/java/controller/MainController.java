package controller;

import DataBase.Context;
import controller.network.SocketHandler;
import controller.offline.OfflineController;
import javafx.stage.Stage;
import listeners.BackOnlineListener;
import shared.event.Event;
import shared.form.GroupDataForm;
import shared.form.ProfileInfoForm;
import shared.form.UserInfoForm;
import shared.model.Message;
import shared.model.Notification;
import shared.model.Tweet;
import shared.model.User;
import shared.response.Response;
import shared.response.ResponseVisitor;
import shared.util.Loop;
import util.Status;
import view.MainStage;
import view.profile.ProfileFXMLController;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MainController implements ResponseVisitor {
    private Context context = Context.getContext();
    private SocketHandler socketHandler;
    private final String host;
    private final int port;
    private final Stage stage;
    private MainStage mainStage;
    private long authToken;
    private final List<Event> events = new LinkedList<>();
    private final LinkedList<Event> cashedEvents = new LinkedList<>();
    private final Loop loop;
    private User user;

    public MainController(String host, int port, Stage stage)
    {
        this.stage = stage;
        this.host = host;
        this.port = port;
        this.loop = new Loop(10, this::sendEvents);

        checkConnection();
    }

    public void checkConnection()
    {
        if(Status.isIsOnline())
            return;

        Socket socket;
        try {
            socket = new Socket(host, port);
            socketHandler = new SocketHandler(socket);
            Status.setIsOnline(true);

            if(cashedEvents.size() != 0) {

                addEvent(cashedEvents.getFirst());
                sendEvents();
                cashedEvents.removeFirst();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Event event : cashedEvents)
                    addEvent(event);
            }

            loop.start();
        }

        catch (IOException e) {
            loop.stop();
            //mainStage.getMainPageScene().getLoader().offline();
            Status.setIsOnline(false);
            OfflineController.getOfflineController().setMainController(this);
        }
    }

    public void start() {
        BackOnlineListener backOnlineListener = this::checkConnection;

        mainStage = new MainStage(stage, this::addEvent, backOnlineListener);
    }

    private void addEvent(Event event) {
        if(!Status.isIsOnline())
            return;

        synchronized (events) {
            //System.out.println("added");
            events.add(event);
        }
    }

    public void addCashed(Event event)
    {
        cashedEvents.add(event);
    }

    private void sendEvents() {
        List<Event> temp;
        synchronized (events) {
            temp = new LinkedList<>(events);
            events.clear();
        }
        for (Event event : temp) {
            event.setAuthToken(authToken);
            Response response = socketHandler.send(event);

            if(response != null)
                response.visit(this);
        }
    }

    public void close()
    {
        socketHandler.close();
    }

    @Override
    public void visitAuthentication(User user, int autToken, boolean tokenUsername, boolean tokenEmail, boolean invalidPassword)
    {
        //System.out.println(user + " " + autToken);
        if(user != null)
        {
            //System.out.println(autToken);
            mainStage.enter(user);
            this.authToken = autToken;
            this.user = user;
            context.users.add(user);
        }

        else
            mainStage.setAuthenticationErrors(tokenEmail, tokenEmail, invalidPassword);

    }

    @Override
    public void visitGetProfile(ProfileInfoForm form, boolean backAvailable) {
        mainStage.getMainPageScene().setProfile(form, backAvailable);
    }

    @Override
    public void visitEditedInfo(boolean tokenUsername, boolean tokenEmail, boolean invalidPassword, boolean successful)
    {
        mainStage.getMainPageScene().getProfilePane().setEdit(tokenUsername, tokenEmail, invalidPassword, successful);
    }

    @Override
    public void visitLists(LinkedList<ProfileInfoForm> forms) {
        mainStage.getMainPageScene().getProfilePane().setLists(forms);
    }

    @Override
    public void visitCommenting(long tweetID) {
        mainStage.getMainPageScene().getProfilePane().setComment(tweetID);
    }

    @Override
    public void visitTweets(LinkedList<Tweet> tweets, LinkedList<ProfileInfoForm> users, String where, boolean isRootList) {
        if(where.equals("Profile"))
        {
            ProfileFXMLController profileFXML = mainStage.getMainPageScene().getProfilePane().getLoader();
            profileFXML.setTweets(tweets, isRootList);
        }

        if(where.equals("Timeline"))
        {
            mainStage.getMainPageScene().setTimeline(tweets, users, where, isRootList);
        }

        if(where.equals("Explorer"))
        {
            mainStage.getMainPageScene().setTimeline(tweets, users, where, isRootList);
        }
    }

    @Override
    public void visitSearchResult(ProfileInfoForm form, boolean successful) {
        mainStage.getMainPageScene().getExplorerPane().getLoader().setSearchData(successful, form);
    }

    @Override
    public void visitNotification(LinkedList<Notification> notifications, LinkedList<ProfileInfoForm> users) {
        mainStage.getMainPageScene().getProfilePane().setNotification(users, notifications);
    }

    @Override
    public void visitSettingsResponse(boolean successful) {
        mainStage.getMainPageScene().getSettingsPane().getLoader().setResponse(successful);
    }

    @Override
    public void visitSettingsInfo(boolean isPrivate, int lastSeen) {
        mainStage.getMainPageScene().setSettings(isPrivate, lastSeen);
    }

    @Override
    public void visitGetMessenger(LinkedList<GroupDataForm> forms) {
        mainStage.getMainPageScene().setMessenger(forms);

        if(Status.isIsOnline())
        {
            LinkedList<Long> groupForms = new LinkedList<>();

            for (GroupDataForm currentForm : forms) {
                context.groups.add(currentForm);
                groupForms.add(currentForm.getGpID());
            }

            UserInfoForm form = context.users.get(user.getId());
            form.setGroups(groupForms);
            context.users.update(form);
        }
    }

    @Override
    public void visitChat(String gpName, long gpID, LinkedList<String> usernames, LinkedList<Message> messages, boolean blocked) {
        mainStage.getMainPageScene().getMessengerPane().setChat(gpName, gpID, usernames, messages, blocked);

        if(Status.isIsOnline())
        {
            LinkedList<Long> messageIDS = new LinkedList<>();
            for (Message currentMessage : messages) {
                context.messages.add(currentMessage);
                messageIDS.add(currentMessage.getMessageID());
            }

            GroupDataForm form = context.groups.get(gpID);
            form.setMessages(messageIDS);
            form.setUsernames(usernames);
            form.setBlocked(blocked);
            context.groups.update(form);
        }
    }

    @Override
    public void visitMessengerLists(LinkedList<GroupDataForm> forms, String type) {
        mainStage.getMainPageScene().getMessengerPane().setMessengerLists(forms, type);
    }
}
