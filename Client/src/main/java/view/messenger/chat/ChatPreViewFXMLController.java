package view.messenger.chat;

import controller.offline.OfflineController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import shared.event.ViewGroupEvent;
import shared.listener.EventListener;
import util.Status;

public class ChatPreViewFXMLController {

    @FXML
    Label groupNameLabel;

    private EventListener listener;
    OfflineController offlineController = OfflineController.getOfflineController();
    private long gpID;

    public void setListener(EventListener listener)
    {
        this.listener = listener;
    }

    public void setData(long gpID, String name, int unread)
    {
        this.gpID = gpID;

        String gpLabel = name;

        if(unread != 0)
            gpLabel += " (" + unread + ")";

        groupNameLabel.setText(gpLabel);
    }

    public void view()
    {
        listener.listen(new ViewGroupEvent(gpID));

        if(!Status.isIsOnline())
            offlineController.getChat(gpID);
    }

}
