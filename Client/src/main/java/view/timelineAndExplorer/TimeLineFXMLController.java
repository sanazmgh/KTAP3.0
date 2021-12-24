package view.timelineAndExplorer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import listeners.StringListener;
import shared.event.GetTweetListEvent;
import shared.event.SearchEvent;
import shared.form.ProfileInfoForm;
import shared.listener.EventListener;
import shared.model.Tweet;
import shared.model.User;
import view.profile.ProfilePreViewPane;
import view.tweet.TweetPane;
import view.viewTools.ShowLists;

import java.util.LinkedList;

public class TimeLineFXMLController{
    @FXML
    private Text typeText;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Text errorText;
    @FXML
    private Pane preView1Pane;
    @FXML
    private Pane preView2Pane;
    @FXML
    private Pane preView3Pane;
    @FXML
    private Pane tweet1Pane;
    @FXML
    private Pane tweet2Pane;
    @FXML
    private Pane tweet3Pane;
    @FXML
    private Button backInCommentsButton;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;

    private EventListener listener;
    private StringListener stringListener;
    private String type;
    private User user;
    private ShowLists tweetsShowLists = new ShowLists();
    private ShowLists usersShowLists = new ShowLists();

    public void setListener(EventListener listener, StringListener stringListener) {
        this.listener = listener;
        this.stringListener = stringListener;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setData()
    {
        preView1Pane.getChildren().clear();
        errorText.setVisible(false);

        typeText.setText(type);
        searchButton.setVisible(type.equals("Explorer"));
        searchTextField.setVisible(type.equals("Explorer"));

        tweetsShowLists = new ShowLists();
        LinkedList<Pane> tweetsMainList = new LinkedList<>();
        tweetsMainList.add(tweet1Pane);
        tweetsMainList.add(tweet2Pane);
        tweetsMainList.add(tweet3Pane);
        tweetsShowLists.setMainPanes(tweetsMainList);
        tweetsShowLists.setButtons(backButton, nextButton);

        usersShowLists = new ShowLists();
        LinkedList<Pane> usersMainList = new LinkedList<>();
        usersMainList.add(preView1Pane);
        usersMainList.add(preView2Pane);
        usersMainList.add(preView3Pane);
        usersShowLists.setMainPanes(usersMainList);
        usersShowLists.setButtons(backButton, nextButton);
    }

    public void setList(LinkedList<Tweet> tweets, LinkedList<ProfileInfoForm> forms, boolean isRootList)
    {
        LinkedList<Pane> tweetsShowPanes = new LinkedList<>();
        for(Tweet currentTweet : tweets)
        {
            TweetPane currentTweetPane = new TweetPane();
            currentTweetPane.getLoader().setListener(listener, stringListener);
            currentTweetPane.getLoader().setData(currentTweet, type);
            tweetsShowPanes.add(currentTweetPane.getPane());
        }
        tweetsShowLists.setShowPanes(tweetsShowPanes);
        tweetsShowLists.setPanes();

        LinkedList<Pane> formsPane = new LinkedList<>();
        for(ProfileInfoForm form : forms)
        {
            ProfilePreViewPane currentProfile = new ProfilePreViewPane();
            currentProfile.getLoader().setListener(listener);
            currentProfile.getLoader().setUser(user);
            currentProfile.getLoader().setData(form);
            formsPane.add(currentProfile.getPane());
        }
        usersShowLists.setShowPanes(formsPane);
        usersShowLists.setPanes();

        backInCommentsButton.setVisible(!isRootList);
    }

    public void next()
    {
        tweetsShowLists.nextPage();
        usersShowLists.nextPage();
    }

    public void back()
    {
        tweetsShowLists.prevPage();
        usersShowLists.prevPage();
    }

    public void closeComment()
    {
        listener.listen(new GetTweetListEvent(type, 0, "Prev tweet"));
    }

    public void search()
    {
        stringListener.listen("Close loop");
        tweet1Pane.getChildren().clear();
        tweet2Pane.getChildren().clear();
        tweet3Pane.getChildren().clear();
        preView1Pane.getChildren().clear();
        preView2Pane.getChildren().clear();
        preView3Pane.getChildren().clear();
        errorText.setVisible(false);

        String str = searchTextField.getText();
        listener.listen(new SearchEvent(str));
    }

    public void setSearchData(boolean successful, ProfileInfoForm form)
    {
        Platform.runLater(
                () ->
                {
                    if (!successful)
                        errorText.setVisible(true);

                    else {
                        //System.out.println("here");
                        ProfilePreViewPane profilePreViewPane = new ProfilePreViewPane();
                        profilePreViewPane.getLoader().setListener(listener);
                        profilePreViewPane.getLoader().setUser(user);
                        profilePreViewPane.getLoader().setData(form);

                        preView1Pane.getChildren().clear();
                        preView1Pane.getChildren().add(profilePreViewPane.getPane());
                    }
                }
        );
    }
}
