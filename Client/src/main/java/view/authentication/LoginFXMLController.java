package view.authentication;

import DataBase.Context;
import controller.offline.OfflineController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import listeners.StringListener;
import shared.event.LoginEvent;
import shared.form.LoginForm;
import shared.listener.EventListener;
import util.Status;

public class LoginFXMLController {
    @FXML
    private TextField usernameTextField ;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text warningText;

    private EventListener listener;
    private StringListener stringListener;
    private final OfflineController offlineController = OfflineController.getOfflineController();
    private final Context context = Context.getContext();

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setStringListener(StringListener stringListener) {
        this.stringListener = stringListener;
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordTextField.getText();
    }

    public void signUp()
    {
        stringListener.listen("Sign up");
    }

    public void submitInfo()
    {
        LoginForm form = new LoginForm(getUsername(), getPassword());
        listener.listen(new LoginEvent(form));

        if(!Status.isIsOnline())
            offlineController.validAuthentication(getUsername(), getPassword());
    }

    public void visibleError()
    {
        warningText.setVisible(true);
    }

}
