package view.authentication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import listeners.BackOnlineListener;
import listeners.StringListener;
import shared.event.SignUpEvent;
import shared.form.UserInfoForm;
import shared.listener.EventListener;
import util.Status;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpFXMLController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField password1TextField;
    @FXML
    private TextField password2TextField;
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
    private Button submitButton;
    @FXML
    private Button onlineButton;
    @FXML
    private Text offlineText;

    private EventListener listener;
    private StringListener stringListener;
    private BackOnlineListener backOnlineListener;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setStringListener(StringListener stringListener) {
        this.stringListener = stringListener;
    }

    public void setBackOnlineListener(BackOnlineListener backOnlineListener) {
        this.backOnlineListener = backOnlineListener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        offlineText.setVisible(!Status.isIsOnline());
        onlineButton.setVisible(!Status.isIsOnline());
        submitButton.setDisable(!Status.isIsOnline());
    }

    public void login()
    {
        stringListener.listen("Login");
    }

    public void submitInfo()
    {
        UserInfoForm form = new UserInfoForm();
        form.setUsername(usernameTextField.getText());
        form.setName(nameTextField.getText());
        form.setLastName(lastNameTextField.getText());
        form.setPass(password1TextField.getText());
        form.setPass2(password2TextField.getText());
        form.setBio(bioTextField.getText());
        form.setDateOfBirth(datePicker.getValue());
        form.setVisibleDate(dateVisibleCheck.isSelected());
        form.setPhone(phoneTextField.getText());
        form.setVisiblePhone(phoneVisibleCheck.isSelected());
        form.setEmail(emailTextField.getText());
        form.setVisibleEmail(emailVisibleCheck.isSelected());

        listener.listen(new SignUpEvent(form));
    }

    public void resetErrors()
    {
        usernameError.setVisible(false);
        passError.setVisible(false);
        emailError.setVisible(false);

        if(Status.isIsOnline())
        {
            offlineText.setVisible(false);
            submitButton.setDisable(false);
        }
    }

    public void invalidUsername(boolean status)
    {
        usernameError.setVisible(status);
    }

    public void invalidPass(boolean status)
    {
        passError.setVisible(status);
    }

    public void invalidEmail(boolean status)
    {
        emailError.setVisible(status);
    }

    public void backOnline()
    {
        backOnlineListener.checkOnline();
        resetErrors();
    }
}
