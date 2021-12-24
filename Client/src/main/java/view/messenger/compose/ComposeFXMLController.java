package view.messenger.compose;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import shared.event.ComposeEvent;
import shared.listener.EventListener;
import shared.util.ShareImage;


public class ComposeFXMLController {

    @FXML
    private TextField messageTextField;
    @FXML
    private TextField attachmentTextField;
    @FXML
    private TextField composeTextField;

    private EventListener listener;

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public void send()
    {
        listener.listen(new ComposeEvent(messageTextField.getText(),
                ShareImage.imageToString(attachmentTextField.getText()),
                composeTextField.getText(), false, ""));

        messageTextField.clear();
        attachmentTextField.clear();
        composeTextField.clear();
    }
}
