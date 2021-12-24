package view.notification;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import shared.event.GetNotificationsEvent;
import shared.event.GetProfileEvent;
import shared.form.ProfileInfoForm;
import shared.listener.EventListener;
import shared.model.Notification;
import shared.model.User;
import shared.util.Loop;
import view.profile.ProfilePreViewPane;
import view.viewTools.ShowLists;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class NotificationFXMLController implements Initializable {

    @FXML
    private Pane preView1Pane;
    @FXML
    private Pane preView2Pane;
    @FXML
    private Pane preView3Pane;
    @FXML
    private Pane preView4Pane;
    @FXML
    private Pane notification1Pane;
    @FXML
    private Pane notification2Pane;
    @FXML
    private Pane notification3Pane;
    @FXML
    private Pane notification4Pane;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;

    private String currentType = "";
    private EventListener listener;
    private User user;
    private Loop loop;
    private final ShowLists notifList = new ShowLists();
    private final ShowLists usersList = new ShowLists();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LinkedList<Pane> usersPane = new LinkedList<>();
        usersPane.add(preView1Pane);
        usersPane.add(preView2Pane);
        usersPane.add(preView3Pane);
        usersPane.add(preView4Pane);
        usersList.setMainPanes(usersPane);
        usersList.setButtons(backButton, nextButton);

        LinkedList<Pane> notifPane = new LinkedList<>();
        notifPane.add(notification1Pane);
        notifPane.add(notification2Pane);
        notifPane.add(notification3Pane);
        notifPane.add(notification4Pane);
        notifList.setMainPanes(notifPane);
        notifList.setButtons(backButton, nextButton);

    }

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void showRequests()
    {
        if(currentType.equals("Request"))
            listener.listen(new GetNotificationsEvent(currentType));

        else
        {
            if(loop != null)
                loop.stop();

            currentType = "Request";
            loop = new Loop(1, this::showRequests);
            loop.start();
        }

    }

    public void showSystemNotifications()
    {
        if(currentType.equals("System"))
            listener.listen(new GetNotificationsEvent(currentType));

        else
        {
            if(loop != null)
                loop.stop();

            currentType = "System";
            loop = new Loop(1, this::showSystemNotifications);
            loop.start();
        }
    }

    public void setNotif(LinkedList<ProfileInfoForm> users, LinkedList<Notification> notifications)
    {
        LinkedList<Pane> preProfilePanes = new LinkedList<>();
        LinkedList<Pane> notificationPane = new LinkedList<>();

        for(int i=0 ; i<notifications.size() ; i++)
        {
            NotifMessagePane currentNotif = new NotifMessagePane();
            currentNotif.getLoader().setListener(listener);
            currentNotif.getLoader().setData(notifications.get(i), currentType);
            notificationPane.add(currentNotif.getPane());

            ProfilePreViewPane currentUser = new ProfilePreViewPane();
            currentUser.getLoader().setListener(listener);
            currentUser.getLoader().setUser(user);
            currentUser.getLoader().setData(users.get(i));
            preProfilePanes.add(currentUser.getPane());
        }

        notifList.setShowPanes(notificationPane);
        usersList.setShowPanes(preProfilePanes);

        notifList.setPanes();
        usersList.setPanes();

    }

    public void next()
    {
        notifList.nextPage();
        usersList.nextPage();
    }

    public void back()
    {
        notifList.prevPage();
        usersList.prevPage();
    }

    public void prevStep()
    {
        if(loop != null)
            loop.stop();

        listener.listen(new GetProfileEvent(user.getId()));
    }

}
