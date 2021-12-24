package view.mainPage;

import controller.offline.OfflineController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import listeners.BackOnlineListener;
import shared.event.GetMessengerEvent;
import shared.event.GetProfileEvent;
import shared.event.GetSettingsEvent;
import shared.event.GetTimelineEvent;
import shared.listener.EventListener;
import shared.model.User;
import util.Status;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageFXMLController implements Initializable {

    @FXML
    private Pane middlePane;
    @FXML
    private Button onlineButton;

    private User user;
    private EventListener listener;
    private BackOnlineListener backOnlineListener;
    private OfflineController offlineController = OfflineController.getOfflineController();

    public Pane getMiddlePane() {
        return middlePane;
    }

    public void setListener(EventListener listener, BackOnlineListener backOnlineListener)
    {
        this.listener = listener;
        this.backOnlineListener = backOnlineListener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onlineButton.setVisible(!Status.isIsOnline());
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void viewProfile()
    {
        listener.listen(new GetProfileEvent(user.getId()));
    }

    public void viewTimeline()
    {
        listener.listen(new GetTimelineEvent("Timeline"));
    }

    public void viewExplorer()
    {
        listener.listen(new GetTimelineEvent("Explorer"));
    }

    public void viewMessenger()
    {
        listener.listen(new GetMessengerEvent());

        if(!Status.isIsOnline())
            offlineController.getMessenger();
    }

    public void viewSettings()
    {
        listener.listen(new GetSettingsEvent());

        if(!Status.isIsOnline())
            offlineController.getSettings();
    }

    public void backOnline()
    {
        backOnlineListener.checkOnline();

        if(Status.isIsOnline())
            onlineButton.setVisible(false);
    }

    public void offline()
    {
        onlineButton.setVisible((true));
    }
}
