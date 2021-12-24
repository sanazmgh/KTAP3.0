package view.profile;

import util.Constants;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import listeners.StringListener;
import shared.config.Config;
import shared.form.ProfileInfoForm;
import shared.listener.EventListener;
import shared.model.Notification;
import shared.model.User;
import view.Lists.ListsPane;
import view.edit.EditPane;
import view.newTweet.NewTweetPane;
import view.notification.NotificationPane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class ProfilePane{
    private Pane pane ;
    private final Pane middlePane;
    private final FXMLLoader loader;
    private static final String PROFILE = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"profilePane").orElse("");
    private final NewTweetPane newTweetPane;
    private final EditPane editPane;
    private final ListsPane listsPane;
    private final NotificationPane notificationPane;
    private final StringListener mainPageListener;

    public ProfilePane(Pane middlePane, StringListener mainPageListener){
        this.middlePane = middlePane;
        this.mainPageListener = mainPageListener;

        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(PROFILE)));

        try {
            pane = loader.load();

        }

        catch (IOException e) {
            e.printStackTrace();
        }

        newTweetPane = new NewTweetPane();
        editPane = new EditPane();
        listsPane = new ListsPane();
        notificationPane = new NotificationPane();
    }

    public void setListener(EventListener listener, StringListener updateListener)
    {
        StringListener stringListener = str -> {
            switch (str) {
                case "New tweet" -> {
                    mainPageListener.listen("Close loop");
                    middlePane.getChildren().clear();
                    newTweetPane.getLoader().setType(0);
                    middlePane.getChildren().add(newTweetPane.getPane());
                }
                case "Edit" -> {
                    mainPageListener.listen("Close loop");
                    editPane.getLoader().setData();
                    middlePane.getChildren().clear();
                    middlePane.getChildren().add(editPane.getPane());
                }
                case "Lists" -> {
                    mainPageListener.listen("Close loop");
                    middlePane.getChildren().clear();
                    middlePane.getChildren().add(listsPane.getPane());
                }
                case "Notifications" -> {
                    mainPageListener.listen("Close loop");
                    middlePane.getChildren().clear();
                    middlePane.getChildren().add(notificationPane.getPane());
                }
            }
        };

        getLoader().setListener(listener);
        getLoader().setStringListener(stringListener, updateListener);
        newTweetPane.getLoader().setListener(listener);
        editPane.getLoader().setListener(listener);
        listsPane.getLoader().setListener(listener);
        notificationPane.getLoader().setListener(listener);
    }

    public void setUser(User user) {
        newTweetPane.getLoader().setUser(user);
        editPane.getLoader().setUser(user);
        listsPane.getLoader().setUser(user);
        notificationPane.getLoader().setUser(user);
    }

    public void setEdit(boolean tokenUsername, boolean tokenEmail, boolean invalidPassword, boolean successful)
    {
        editPane.getLoader().setResponse(tokenUsername, tokenEmail, invalidPassword, successful);
    }

    public void setLists(LinkedList<ProfileInfoForm> forms)
    {
        listsPane.getLoader().setList(forms);
    }

    public void setComment(long tweetID)
    {
        Platform.runLater(
                () ->
                {
                    mainPageListener.listen("Close loop");
                    middlePane.getChildren().clear();
                    newTweetPane.getLoader().setType(tweetID);
                    middlePane.getChildren().add(newTweetPane.getPane());
                }
        );
    }

    public void setNotification(LinkedList<ProfileInfoForm> forms, LinkedList<Notification> notifications)
    {
        notificationPane.getLoader().setNotif(forms, notifications);
    }

    public Pane getPane() {
        return pane;
    }

    public ProfileFXMLController getLoader() {
        return loader.getController();
    }
}
