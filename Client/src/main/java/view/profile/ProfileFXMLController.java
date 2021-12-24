package view.profile;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import listeners.StringListener;
import shared.event.ChangeStatusEvent;
import shared.event.GetTweetListEvent;
import shared.event.PreProfileEvent;
import shared.form.ProfileInfoForm;
import shared.listener.EventListener;
import shared.model.Tweet;
import shared.model.User;
import shared.util.ShareImage;
import view.tweet.TweetPane;
import view.viewTools.ShowLists;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;

public class ProfileFXMLController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label bioLabel;
    @FXML
    private Label lastSeenLabel;
    @FXML
    private Label informationLabel;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Label privacyStatusLabel;
    @FXML
    private Pane tweet1Pane;
    @FXML
    private Pane tweet2Pane;
    @FXML
    private Pane tweet3Pane;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button prevStepButton;
    @FXML
    private Button followButton;
    @FXML
    private Button muteButton;
    @FXML
    private Button blockButton;
    @FXML
    private Pane toolBarPane;
    @FXML
    private Button backInCommentsButton;
    @FXML
    private Text visibleErrorText;

    private final ShowLists showLists = new ShowLists();

    private EventListener listener;
    private StringListener stringListener;
    private StringListener updateListener;

    private User user;
    private ProfileInfoForm form;
    private int noOfTweets = 0;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setStringListener(StringListener stringListener, StringListener updateListener) {
        this.stringListener = stringListener;
        this.updateListener = updateListener;
    }

    public void setData(User user, ProfileInfoForm form, boolean backAvailable)
    {
        this.user = user;
        this.form = form;

        nameLabel.setText(form.getName() + " " + form.getLastName());
        usernameLabel.setText("@" + form.getUsername());
        lastSeenLabel.setText(form.getLastSeen());
        bioLabel.setText(form.getBio());
        informationLabel.setText(form.getInfo());
        prevStepButton.setVisible(backAvailable);

        if(form.isMyProfile())
        {
            toolBarPane.setVisible(true);
            followButton.setVisible(false);
            muteButton.setVisible(false);
            blockButton.setVisible(false);
        }

        else
        {
            toolBarPane.setVisible(false);
            followButton.setVisible(true);
            followButton.setText(form.getFollowingStatus());
            muteButton.setVisible(true);
            muteButton.setText(form.isMuted());
            blockButton.setVisible(true);
            blockButton.setText(form.isBlocked());
        }

        privacyStatusLabel.setVisible(form.getPrivacyStatus());

        if(!form.getImageSTR().equals(""))
        {
            byte[] imageByte = ShareImage.decodeFromBase64(form.getImageSTR());
            profileImageView.setImage(new Image(new ByteArrayInputStream(imageByte)));
        }

        if(form.isVisibleTweets()) {
            visibleErrorText.setVisible(false);
            listener.listen(new GetTweetListEvent("Profile", 0, "Current"));
        }

        else {
            tweet1Pane.getChildren().clear();
            tweet2Pane.getChildren().clear();
            tweet3Pane.getChildren().clear();

            backButton.setDisable(true);
            nextButton.setDisable(true);
            backInCommentsButton.setVisible(false);
            visibleErrorText.setVisible(true);
        }
    }

    public void setTweets(LinkedList<Tweet> tweets, boolean isRootList)
    {
        noOfTweets = tweets.size();

        LinkedList<Pane> mainPanes = new LinkedList<>();
        mainPanes.add(tweet1Pane);
        mainPanes.add(tweet2Pane);
        mainPanes.add(tweet3Pane);

        LinkedList<Pane> showPanes = new LinkedList<>();
        for(int i=tweets.size()-1 ; i>=0 ; i--)
        {
            TweetPane tweetPane = new TweetPane();

            tweetPane.getLoader().setListener(listener, updateListener);
            tweetPane.getLoader().setData(tweets.get(i), "Profile");

            showPanes.add(tweetPane.getPane());
        }

        backInCommentsButton.setVisible(!isRootList);

        showLists.setMainPanes(mainPanes);
        showLists.setShowPanes(showPanes);
        showLists.setButtons(backButton, nextButton);
        showLists.setPanes();

    }

    public void backPage()
    {
        showLists.prevPage();
    }

    public void nextPage()
    {
        showLists.nextPage();
    }

    public void newTweet()
    {
        stringListener.listen("New tweet");
    }

    public void edit()
    {
        stringListener.listen("Edit");
    }

    public void lists()
    {
        stringListener.listen("Lists");
    }

    public void notifications()
    {
        stringListener.listen("Notifications");
    }

    public void block()
    {
        listener.listen(new ChangeStatusEvent(form.getUserID(), "Block"));
    }

    public void mute()
    {
        listener.listen(new ChangeStatusEvent(form.getUserID(), "Mute"));
    }

    public void follow()
    {
        listener.listen(new ChangeStatusEvent(form.getUserID(), "Follow"));
    }

    public void prevStep()
    {
        listener.listen(new PreProfileEvent());
    }

    public void closeComment()
    {
        listener.listen(new GetTweetListEvent("Profile", 0, "Prev tweet"));
    }

    public ProfileInfoForm getForm() {
        return form;
    }
}
