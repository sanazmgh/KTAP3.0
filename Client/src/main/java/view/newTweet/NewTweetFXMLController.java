package view.newTweet;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import shared.event.GetProfileEvent;
import shared.event.NewTweetEvent;
import shared.listener.EventListener;
import shared.model.User;
import shared.util.ShareImage;

public class NewTweetFXMLController {
    @FXML
    private TextField tweetingTextField;
    @FXML
    private TextField imageTextField;

    private EventListener listener;
    private User user;
    private long  commentedUnder = 0;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setType(long commentedUnder) {
        this.commentedUnder = commentedUnder;
    }

    public void submit()
    {
        NewTweetEvent tweet = new NewTweetEvent(tweetingTextField.getText(),
                ShareImage.imageToString(imageTextField.getText()), 0, commentedUnder);
        listener.listen(tweet);

        tweetingTextField.clear();
        imageTextField.clear();
    }

    public void prevStep()
    {
        listener.listen(new GetProfileEvent(user.getId()));
    }
}
