package view.Lists;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import shared.event.GetListsEvent;
import shared.event.GetProfileEvent;
import shared.form.ProfileInfoForm;
import shared.listener.EventListener;
import shared.model.User;
import shared.util.Loop;
import view.profile.ProfilePreViewPane;
import view.viewTools.ShowLists;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ListsFXMLController implements Initializable {

    @FXML
    private Pane preView1Pane;
    @FXML
    private Pane preView2Pane;
    @FXML
    private Pane preView3Pane;
    @FXML
    private Pane preView4Pane;
    @FXML
    private Pane preView5Pane;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;

    private User user;
    private EventListener listener;
    private ShowLists showLists;
    private Loop loop;
    private String currentList = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LinkedList<Pane> mainPanes = new LinkedList<>();
        mainPanes.add(preView1Pane);
        mainPanes.add(preView2Pane);
        mainPanes.add(preView3Pane);
        mainPanes.add(preView4Pane);
        mainPanes.add(preView5Pane);

        showLists = new ShowLists();
        showLists.setMainPanes(mainPanes);
        showLists.setShowPanes(new LinkedList<>());
        showLists.setButtons(backButton, nextButton);
        backButton.setDisable(true);
        nextButton.setDisable(true);
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void showFollowers()
    {
        if(currentList.equals("Followers"))
            listener.listen(new GetListsEvent("Followers"));

        else
        {
            if(loop != null)
                loop.stop();

            currentList = "Followers";
            loop = new Loop(1, this::showFollowers);
            loop.start();
        }
    }

    public void showFollowings()
    {
        if(currentList.equals("Followings"))
            listener.listen(new GetListsEvent("Followings"));

        else
        {
            if(loop != null)
                loop.stop();

            currentList = "Followings";
            loop = new Loop(1, this::showFollowings);
            loop.start();
        }
    }

    public void showMuted()
    {
        if(currentList.equals("Muted"))
            listener.listen(new GetListsEvent("Muted"));

        else
        {
            if(loop != null)
                loop.stop();

            currentList = "Muted";
            loop = new Loop(1, this::showMuted);
            loop.start();
        }
    }

    public void showBlocked()
    {
        if(currentList.equals("Blocked"))
            listener.listen(new GetListsEvent("Blocked"));

        else
        {
            if(loop != null)
                loop.stop();

            currentList = "Blocked";
            loop = new Loop(1, this::showBlocked);
            loop.start();
        }
    }

    public void setList(LinkedList<ProfileInfoForm> forms)
    {
        LinkedList<Pane> showPanes = new LinkedList<>();

        for(ProfileInfoForm form : forms)
        {
            ProfilePreViewPane currentProfile = new ProfilePreViewPane();
            currentProfile.getLoader().setListener(listener);
            currentProfile.getLoader().setUser(user);
            currentProfile.getLoader().setData(form);

            showPanes.add(currentProfile.getPane());
        }

        showLists.setShowPanes(showPanes);
        showLists.setPanes();
    }

    public void nextPage()
    {
        showLists.nextPage();
    }

    public void backPage()
    {
        showLists.prevPage();
    }

    public void prevStep()
    {
        if(loop != null)
            loop.stop();

        listener.listen(new GetProfileEvent(user.getId()));
    }

}
