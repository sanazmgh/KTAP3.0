package view.settings;

import controller.offline.OfflineController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import shared.event.SettingsEvent;
import shared.listener.EventListener;
import util.Status;

public class SettingsFXMLController {
    @FXML
    private RadioButton publicRadioButton;
    @FXML
    private RadioButton privateRadioButton;
    @FXML
    private RadioButton everyoneRadioButton;
    @FXML
    private RadioButton followersRadioButton;
    @FXML
    private RadioButton nobodyRadioButton;
    @FXML
    private PasswordField deletePasswordField;
    @FXML
    private PasswordField deactivePasswordField;
    @FXML
    private Text wrongPassText;
    @FXML
    private Text offline1Text;
    @FXML
    private Text offline2Text;
    @FXML
    private Button deleteButton;
    @FXML
    private Button deactiveButton;

    private EventListener listener;
    private final OfflineController offlineController = OfflineController.getOfflineController();

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setData(boolean isPrivate, int lastSeen) {

        if(isPrivate)
            privateRadioButton.setSelected(true);

        else
            publicRadioButton.setSelected(true);

        if(lastSeen == 0)
            nobodyRadioButton.setSelected(true);

        else if(lastSeen == 1)
            everyoneRadioButton.setSelected(true);

        else
            followersRadioButton.setSelected(true);

        offline1Text.setVisible(!Status.isIsOnline());
        offline2Text.setVisible(!Status.isIsOnline());
        deleteButton.setDisable(!Status.isIsOnline());
        deactiveButton.setDisable(!Status.isIsOnline());
    }

    public void privacy()
    {
        listener.listen(new SettingsEvent("Privacy", publicRadioButton.isSelected() ? "Public" : "Private"));

        if(!Status.isIsOnline())
            offlineController.setPrivacy(publicRadioButton.isSelected() ? "Public" : "Private");
    }

    public void lastSeen()
    {
        String action = "Nobody";

        if(everyoneRadioButton.isSelected())
            action = "Everyone";

        else if(followersRadioButton.isSelected())
            action = "Followers";

        listener.listen(new SettingsEvent("LastSeen", action));

        if(!Status.isIsOnline())
            offlineController.setLastSeen(action);
    }

    public void deleteAccount()
    {
        listener.listen(new SettingsEvent("DeleteAccount", deletePasswordField.getText()));
    }

    public void deactiveAccount()
    {
        listener.listen(new SettingsEvent("DeactiveAccount" , deactivePasswordField.getText()));
    }

    public void setResponse(boolean successful)
    {
        if(successful)
            logOut();

        wrongPassText.setVisible(true);
    }

    public void logOut()
    {
        Platform.exit();
        System.exit(0);
    }
}
