package view.messenger.listAndGroups;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import shared.event.NewGroupEvent;
import shared.listener.EventListener;

public class NewListFXMLController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Text typeText;

    private EventListener listener;
    private String type;

    public void setListener(EventListener listener)
    {
        this.listener = listener;
    }

    public void setType(String type) {
        this.type = type;
        typeText.setText(type+"'s name");
    }

    public void create()
    {
        //System.out.println("create");
        listener.listen(new NewGroupEvent(nameTextField.getText(), usernameTextField.getText(), type));
    }
}
