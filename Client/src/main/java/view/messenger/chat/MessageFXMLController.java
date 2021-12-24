package view.messenger.chat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import listeners.IntListener;
import shared.event.SubmitMessageEvent;
import shared.listener.EventListener;
import shared.model.Message;
import shared.model.User;
import shared.util.ShareImage;

import java.io.ByteArrayInputStream;

public class MessageFXMLController {

    @FXML
    private Text nameText;
    @FXML
    private Text sentFromText;
    @FXML
    private Label messageLabel;
    @FXML
    private ImageView profileImageView;
    @FXML
    private ImageView attachmentImageView;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private TextField editTextField;
    @FXML
    private Button submitButton;
    @FXML
    private Text messageStatusText;

    private EventListener listener;
    private IntListener editListener;
    private User user;
    private Message message;

    public void setListener(EventListener listener, IntListener editListener) {
        this.listener = listener;
        this.editListener = editListener;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setData(Message message)
    {
        this.message = message;

        nameText.setText(message.getSenderName());
        if(message.isForwarded())
            sentFromText.setText("a tweet from @" + message.getExtraInfo());

        else
            sentFromText.setText("");

        deleteButton.setVisible(message.getSenderId() == user.getId());
        deleteButton.setDisable(message.isDeleted());
        editButton.setDisable(message.isDeleted());
        editButton.setVisible(message.getSenderId() == user.getId() && !message.isForwarded());
        messageLabel.setText(message.getText());
        messageStatusText.setText("* ".repeat(Math.max(0, message.getStatus())));

        if(!message.getSenderImageSTR().equals(""))
        {
            byte[] imageByte = ShareImage.decodeFromBase64(message.getSenderImageSTR());
            profileImageView.setImage(new Image(new ByteArrayInputStream(imageByte)));
        }

        if(!message.getImageSTR().equals(""))
        {
            byte[] imageByte = ShareImage.decodeFromBase64(message.getImageSTR());
            attachmentImageView.setImage(new Image(new ByteArrayInputStream(imageByte)));
        }
    }

    public void edit()
    {
        editListener.listen(message.getText() , message.getMessageID());
    }

    public void submit()
    {
        listener.listen(new SubmitMessageEvent(message.getMessageID(), editTextField.getText()));
        submitButton.setVisible(false);
        editTextField.setVisible(false);
    }

    public void delete()
    {
        listener.listen(new SubmitMessageEvent(message.getMessageID(), "Deleted message"));
    }

}
