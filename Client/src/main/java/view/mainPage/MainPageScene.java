package view.mainPage;

import controller.offline.OfflineController;
import listeners.BackOnlineListener;
import util.Constants;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import listeners.StringListener;
import shared.config.Config;
import shared.event.GetMessengerEvent;
import shared.event.GetProfileEvent;
import shared.event.GetTimelineEvent;
import shared.form.GroupDataForm;
import shared.form.ProfileInfoForm;
import shared.listener.EventListener;
import shared.model.Tweet;
import shared.model.User;
import shared.util.Loop;
import util.Status;
import view.messenger.MessengerPane;
import view.profile.ProfilePane;
import view.settings.SettingsPane;
import view.timelineAndExplorer.TimelinePane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class MainPageScene {
    private final Scene scene ;
    private final FXMLLoader loader;
    private static final String MAIN_PAGE = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"mainPageScene").orElse("");
    private EventListener listener;
    private final StringListener stringListener;
    private final OfflineController offlineController = OfflineController.getOfflineController();
    private User user;
    private final ProfilePane profilePane;
    private final TimelinePane timelinePane;
    private final TimelinePane explorerPane;
    private final SettingsPane settingsPane;
    private final MessengerPane messengerPane;
    private String currentPane = "";
    private Loop loop;

    public MainPageScene(){
        this.loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(MAIN_PAGE)));
        Parent root = null;
        try
        {
            root = loader.load();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        assert root != null;
        scene = new Scene(root);

        stringListener = str -> {
            if(str.equals("Close loop"))
            {
                if(loop != null)
                    loop.stop();

                currentPane = "";
            }

            if(str.equals("Restart Loop"))
            {
                if(loop != null)
                    loop.restart();
            }
        };

        Pane middlePane = getLoader().getMiddlePane();
        profilePane = new ProfilePane(middlePane, stringListener);
        timelinePane = new TimelinePane();
        timelinePane.getLoader().setType("Timeline");
        explorerPane = new TimelinePane();
        explorerPane.getLoader().setType("Explorer");
        settingsPane = new SettingsPane();
        messengerPane = new MessengerPane();
    }

    public Scene getScene() {
        return scene;
    }

    public MainPageFXMLController getLoader() {
        return loader.getController();
    }

    public void setListener(EventListener listener, BackOnlineListener backOnlineListener)
    {
        this.listener = listener;
        getLoader().setListener(listener, backOnlineListener);
        profilePane.setListener(listener, stringListener);
        timelinePane.getLoader().setListener(listener, stringListener);
        explorerPane.getLoader().setListener(listener, stringListener);
        settingsPane.getLoader().setListener(listener);
        messengerPane.setListener(listener);
    }

    public void setUser(User user)
    {
        this.user = user;
        getLoader().setUser(user);
        profilePane.setUser(user);
        timelinePane.getLoader().setUser(user);
        explorerPane.getLoader().setUser(user);
        messengerPane.setUser(user);
    }

    public void setProfile(ProfileInfoForm form, boolean backAvailable)
    {
        if (!currentPane.equals("Profile"))
        {
            Platform.runLater(
                    () ->
                    {
                        if(loop != null) {
                            messengerPane.closeLoop();
                            loop.stop();
                        }

                        currentPane = "Profile";
                        profilePane.getLoader().setData(user, form, backAvailable);

                        getLoader().getMiddlePane().getChildren().clear();
                        getLoader().getMiddlePane().getChildren().add(profilePane.getPane());

                        loop = new Loop(1 , this::updateProfile);
                        loop.start();
                    }
            );
        }

        else {

            Platform.runLater(
                    () -> {
                        profilePane.getLoader().setData(user, form, backAvailable);
                        //System.out.println("hii");
                    }
            );
        }
    }

    public void setTimeline(LinkedList<Tweet> tweets, LinkedList<ProfileInfoForm> forms, String type, boolean isRoot)
    {
        TimelinePane currentType;

        //System.out.println(type + " " + currentPane);

        if(type.equals("Timeline"))
            currentType = timelinePane;
        else
            currentType = explorerPane;

        if(!currentPane.equals(type))
        {
            Platform.runLater(
                    () ->
                    {
                        if(loop != null) {
                            loop.stop();
                            messengerPane.closeLoop();
                        }

                        currentPane = type;
                        currentType.getLoader().setData();
                        getLoader().getMiddlePane().getChildren().clear();
                        getLoader().getMiddlePane().getChildren().add(currentType.getPane());

                        loop = new Loop(1, this::updateTimeline);
                        loop.start();
                    }
            );
        }

        else {

            Platform.runLater(
                    () ->
                        currentType.getLoader().setList(tweets, forms, isRoot)
            );
        }
    }

    public void setSettings(boolean isPrivate, int lastSeen)
    {
        Platform.runLater(
                () ->
                {
                    currentPane = "Settings";

                    if (loop != null) {
                        loop.stop();
                        messengerPane.closeLoop();
                    }

                    settingsPane.getLoader().setData(isPrivate, lastSeen);
                    getLoader().getMiddlePane().getChildren().clear();
                    getLoader().getMiddlePane().getChildren().add(settingsPane.getPane());
                }
        );
    }

    public void setMessenger(LinkedList<GroupDataForm> forms)
    {
        if (!currentPane.equals("Messenger"))
        {
            Platform.runLater(
                    () ->
                    {
                        if(loop != null) {
                            loop.stop();
                            messengerPane.closeLoop();
                        }

                        currentPane = "Messenger";
                        messengerPane.getLoader().setData(forms);

                        getLoader().getMiddlePane().getChildren().clear();
                        getLoader().getMiddlePane().getChildren().add(messengerPane.getPane());

                        loop = new Loop(1 , this::updateMessenger);

                        if(Status.isIsOnline())
                            loop.start();
                    }
            );
        }

        else {

            Platform.runLater(
                    () -> messengerPane.getLoader().setData(forms)
            );
        }
    }

    public void updateProfile()
    {
        listener.listen(new GetProfileEvent(profilePane.getLoader().getForm().getUserID()));
    }

    public void updateTimeline()
    {
        listener.listen(new GetTimelineEvent(currentPane));
    }

    public void updateMessenger()
    {
        listener.listen(new GetMessengerEvent());
    }

    public ProfilePane getProfilePane() {
        return profilePane;
    }

    public TimelinePane getExplorerPane() {
        return explorerPane;
    }

    public SettingsPane getSettingsPane() {
        return settingsPane;
    }

    public MessengerPane getMessengerPane() {
        return messengerPane;
    }
}


