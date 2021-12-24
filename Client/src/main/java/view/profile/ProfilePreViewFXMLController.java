package view.profile;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.event.ChangeStatusEvent;
import shared.event.GetProfileEvent;
import shared.form.ProfileInfoForm;
import shared.listener.EventListener;
import shared.model.User;
import shared.util.ShareImage;

import java.io.ByteArrayInputStream;

public class ProfilePreViewFXMLController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView prePicImageView;
    @FXML
    private Button blockButton;
    @FXML
    private Button muteButton;
    @FXML
    private Button viewProfileButton;

    private EventListener listener;
    private User user;
    private ProfileInfoForm form;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setData(ProfileInfoForm form) {

        this.form = form;

        nameLabel.setText(form.getName() + " " + form.getLastName());
        usernameLabel.setText("@" + form.getUsername());

        blockButton.setVisible(!form.isMyProfile());
        muteButton.setVisible(!form.isMyProfile());
        viewProfileButton.setVisible(!form.isMyProfile());

        if(!form.getImageSTR().equals(""))
        {
            byte[] imageByte = ShareImage.decodeFromBase64(form.getImageSTR());
            prePicImageView.setImage(new Image(new ByteArrayInputStream(imageByte)));
        }
    }

    public void ViewProfile()
    {
        listener.listen(new GetProfileEvent(form.getUserID()));
    }

    public void blockUser()
    {
        listener.listen(new ChangeStatusEvent(form.getUserID(), "Block"));
    }

    public void muteUser()
    {
        listener.listen(new ChangeStatusEvent(form.getUserID(), "Mute"));
    }
}
