package view.messenger.listAndGroups;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import shared.event.ChangeInMembersEvent;
import shared.form.GroupDataForm;
import shared.listener.EventListener;

public class ListViewFXMLController {

    @FXML
    private Text listNameText;
    @FXML
    private Text usersNameText;
    @FXML
    private TextField addTextField;
    @FXML
    private TextField removeTextField;
    @FXML
    private Button removeButton;

    private EventListener listener;
    private long listID;
    private String type;

    public void setListListener(EventListener listener) {
        this.listener = listener;
    }

    public void setType(String type)
    {
        this.type = type;

        if(type.equals("Groups"))
        {
            removeTextField.setVisible(false);
            removeButton.setText("Leave the group");
        }

        else
        {
            removeTextField.setVisible(true);
            removeButton.setText("Remove from list");
        }
    }

    public void setData(GroupDataForm form)
    {
        listNameText.setText(form.getGpName());
        this.listID = form.getGpID();

        StringBuilder names = new StringBuilder();
        for(String name : form.getUsernames())
            names.append(name).append(", ");
        names = new StringBuilder(names.substring(0, names.length() - 2));

        usersNameText.setText(names.toString());
    }

    public void add()
    {
        listener.listen(new ChangeInMembersEvent("Add", type, listID, addTextField.getText() ));
    }

    public void remove()
    {
        listener.listen(new ChangeInMembersEvent("Remove", type, listID, removeTextField.getText()));

    }
}
