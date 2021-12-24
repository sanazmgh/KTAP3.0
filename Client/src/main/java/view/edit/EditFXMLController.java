package view.edit;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import shared.event.EditInfoEvent;
import shared.event.GetProfileEvent;
import shared.form.UserInfoForm;
import shared.listener.EventListener;
import shared.model.User;
import shared.util.ShareImage;

import java.io.ByteArrayInputStream;

public class EditFXMLController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField pass1PasswordField;
    @FXML
    private PasswordField pass2PasswordField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField bioTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox dateVisibleCheck;
    @FXML
    private TextField phoneTextField;
    @FXML
    private CheckBox phoneVisibleCheck;
    @FXML
    private TextField emailTextField;
    @FXML
    private CheckBox emailVisibleCheck;
    @FXML
    private Text usernameError;
    @FXML
    private Text passError;
    @FXML
    private Text emailError;
    @FXML
    private TextField imagePath;
    @FXML
    private ImageView profileImageView;

    private EventListener listener;
    private User user;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    public void setData()
    {
        usernameTextField.setText(user.getUsername());
        nameTextField.setText(user.getName());
        lastNameTextField.setText(user.getLastName());
        bioTextField.setText(user.getBio());

        if(!user.getImageSTR().equals("")) {
            byte[] imageByte = ShareImage.decodeFromBase64(user.getImageSTR());
            profileImageView.setImage(new Image(new ByteArrayInputStream(imageByte)));
        }
    }

    public void submitInfo()
    {
        UserInfoForm form = new UserInfoForm();
        form.setUsername(usernameTextField.getText());
        form.setName(nameTextField.getText());
        form.setLastName(lastNameTextField.getText());
        form.setPass(pass1PasswordField.getText());
        form.setPass2(pass2PasswordField.getText());
        form.setBio(bioTextField.getText());
        form.setDateOfBirth(datePicker.getValue());
        form.setVisibleDate(dateVisibleCheck.isSelected());
        form.setPhone(phoneTextField.getText());
        form.setVisiblePhone(phoneVisibleCheck.isSelected());
        form.setEmail(emailTextField.getText());
        form.setVisibleEmail(emailVisibleCheck.isSelected());
        form.setPicSTR(ShareImage.imageToString(imagePath.getText()));

        listener.listen(new EditInfoEvent(form));
    }

    public void setResponse(boolean tokenUsername, boolean tokenEmail, boolean invalidPassword, boolean successful)
    {
        if(successful) {
            listener.listen(new GetProfileEvent(user.getId()));
            return;
        }

        usernameError.setVisible(tokenUsername);
        emailError.setVisible(tokenEmail);
        passError.setVisible(invalidPassword);
    }

    public void prevStep()
    {
        listener.listen(new GetProfileEvent(user.getId()));
    }

}
