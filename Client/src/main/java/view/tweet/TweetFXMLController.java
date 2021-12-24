package view.tweet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import listeners.StringListener;
import shared.event.*;
import shared.listener.EventListener;
import shared.model.Tweet;
import shared.util.ShareImage;

import java.io.ByteArrayInputStream;

public class TweetFXMLController {

    @FXML
    Label informationLabel;
    @FXML
    Label tweetTextLabel;
    @FXML
    Button likeButton;
    @FXML
    Button retweetButton;
    @FXML
    Button viewCommentsButton;
    @FXML
    Button submitButton;
    @FXML
    TextField forwardTextField;
    @FXML
    ImageView tweetImageView;

    private Tweet tweet;
    private EventListener listener;
    private StringListener updateListener;
    private String where;

    public void setListener(EventListener listener, StringListener updateListener)
    {
        this.listener = listener;
        this.updateListener = updateListener;
    }

    public void setData(Tweet tweet, String where)
    {
        this.tweet = tweet;
        this.where = where;

        String info = "@" + tweet.getUsername();

        if(!tweet.getRetweetUsername().equals(""))
            info += " retweeted from @" + tweet.getRetweetUsername();

        else if(tweet.isCommented())
            info += " commented";

        else
            info += " tweeted";

        informationLabel.setText(info);
        tweetTextLabel.setText(tweet.getTweetText());

        likeButton.setText("Like(" + tweet.getLikes().size() + ")");
        retweetButton.setText("Retweet(" + tweet.getRetweets().size() + ")");
        viewCommentsButton.setText("View comments(" + tweet.getComments().size() + ")");

        if(!tweet.getTweetImageSTR().equals(""))
        {
            byte[] imageByte = ShareImage.decodeFromBase64(tweet.getTweetImageSTR());
            tweetImageView.setImage(new Image(new ByteArrayInputStream(imageByte)));
        }
    }

    public void like()
    {
        listener.listen(new TweetActionsEvent("Like", tweet.getId()));
    }

    public void retweet()
    {
        listener.listen(new NewTweetEvent(tweet.getTweetText(), tweet.getTweetImageSTR(), tweet.getId(), 0));
    }

    public void save()
    {
        listener.listen(new ComposeEvent(tweet.getTweetText(), tweet.getTweetImageSTR(), "Saved message", true, tweet.getUsername()));
    }

    public void forward()
    {
        updateListener.listen("Close loop");
        forwardTextField.setVisible(true);
        submitButton.setVisible(true);
    }

    public void submit()
    {
        listener.listen(new ComposeEvent(tweet.getTweetText(), tweet.getTweetImageSTR(), forwardTextField.getText(), true, tweet.getUsername()));
        forwardTextField.setVisible(false);
        submitButton.setVisible(false);
        updateListener.listen("Restart Loop");
        forwardTextField.clear();
    }

    public void viewComments()
    {
        listener.listen(new GetTweetListEvent(where, tweet.getId(), "Comment"));
    }

    public void addComment()
    {
        listener.listen(new NewCommentEvent(tweet.getId()));
    }

    public void report()
    {
        listener.listen(new TweetActionsEvent("Report", tweet.getId()));
    }
}
