package view.notification;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import shared.event.ResponseToNotifEvent;
import shared.listener.EventListener;
import shared.model.Notification;

public class NotifMessageFXMLController {
    @FXML
    private Text notificationText;
    @FXML
    private Button acceptButton;
    @FXML
    private Button notifiedDenyButton;
    @FXML
    private Button nonNotifiedDenyButton;

    private EventListener listener;
    private Notification notification;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void setData(Notification notification, String type)
    {
        this.notification = notification;

        notificationText.setText(notification.getText());

        if(type.equals("Request"))
        {
            acceptButton.setVisible(true);
            nonNotifiedDenyButton.setVisible(true);
            notifiedDenyButton.setVisible(true);

        }

        else
        {
            acceptButton.setVisible(false);
            nonNotifiedDenyButton.setVisible(false);
            notifiedDenyButton.setVisible(false);
        }

    }

    public void accept()
    {
        listener.listen(new ResponseToNotifEvent("Accept", notification.getNotifID()));
        acceptButton.setVisible(false);
        nonNotifiedDenyButton.setVisible(false);
        notifiedDenyButton.setVisible(false);
    }

    public void notifiedDeny()
    {
        listener.listen(new ResponseToNotifEvent("NotifiedDeny", notification.getNotifID()));
        acceptButton.setVisible(false);
        nonNotifiedDenyButton.setVisible(false);
        notifiedDenyButton.setVisible(false);
    }

    public void nonNotifiedDeny()
    {
        listener.listen(new ResponseToNotifEvent("NonNotifiedDeny", notification.getNotifID()));
        acceptButton.setVisible(false);
        nonNotifiedDenyButton.setVisible(false);
        notifiedDenyButton.setVisible(false);
    }
}
