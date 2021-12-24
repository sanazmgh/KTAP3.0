package view.messenger.chat;

import DataBase.Context;
import controller.offline.OfflineController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import listeners.IntListener;
import shared.event.ChangeInMembersEvent;
import shared.event.SubmitMessageEvent;
import shared.listener.EventListener;
import shared.model.Message;
import shared.model.User;
import shared.util.ShareImage;
import util.Status;
import view.viewTools.ShowLists;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ChatFXMLController implements Initializable {

    @FXML
    private Text groupText;
    @FXML
    private Text membersText;
    @FXML
    private TextField addUserTextField;
    @FXML
    private Pane message1Pane;
    @FXML
    private Pane message2Pane;
    @FXML
    private Pane message3Pane;
    @FXML
    private Pane message4Pane;
    @FXML
    private Pane message5Pane;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private TextField messageTextField;
    @FXML
    private TextField attachmentTextField;
    @FXML
    private Button addButton;
    @FXML
    private Button submitButton;
    @FXML
    private TextField timeTextField;

    private EventListener listener;
    private IntListener intListener;
    private final OfflineController offlineController = OfflineController.getOfflineController();
    private final Context context = Context.getContext();
    private long gpID = 0;
    private long editingID = 0;
    private User user;
    private ShowLists showLists;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        LinkedList<Pane> mainPanes = new LinkedList<>();
        mainPanes.add(message1Pane);
        mainPanes.add(message2Pane);
        mainPanes.add(message3Pane);
        mainPanes.add(message4Pane);
        mainPanes.add(message5Pane);

        showLists = new ShowLists();

        showLists.setMainPanes(mainPanes);
        showLists.setButtons(backButton, nextButton);
    }

    public void setListener(EventListener listener)
    {
        this.listener = listener;
        intListener = (text, id) -> {
                editingID = id;
                messageTextField.setText(text);
        };
    }

    public void setUser(User user)
    {
        this.user = user;
    }
    public void setData(String gpName, long gpID, LinkedList<String> usernames, LinkedList<Message> messages, boolean blocked)
    {
        this.gpID = gpID;

        groupText.setText(gpName);
        addButton.setVisible(!gpName.equals("Saved message"));
        addUserTextField.setVisible(!gpName.equals("Saved message"));

        StringBuilder names = new StringBuilder();
        for(String name : usernames)
            names.append(name).append(", ");
        names = new StringBuilder(names.substring(0, names.length() - 2));

        membersText.setText(names.toString());
        submitButton.setDisable(blocked);
        messageTextField.setDisable(blocked);
        attachmentTextField.setDisable(blocked);

        LinkedList<Pane> showPanes = new LinkedList<>();
        for(Message message : messages)
        {
            MessagePane messagePane = new MessagePane();
            messagePane.getLoader().setListener(listener, intListener);
            messagePane.getLoader().setUser(user);
            messagePane.getLoader().setData(message);

            showPanes.add(messagePane.getPane());
        }

        showLists.setShowPanes(showPanes);
        showLists.setPanes();
    }

    public void next()
    {
        showLists.nextPage();
    }

    public void back()
    {
        showLists.prevPage();
    }

    public void addMember()
    {
        listener.listen(new ChangeInMembersEvent("Add", "Group", gpID, addUserTextField.getText()));
    }

    public void submitMessage()
    {
        if(editingID != 0)
            listener.listen(new SubmitMessageEvent(editingID, messageTextField.getText()));

        else
        {
            listener.listen(new SubmitMessageEvent(
                    messageTextField.getText(),
                    ShareImage.imageToString(attachmentTextField.getText()),
                    false,
                    0,
                    gpID,
                    timeTextField.getText()));
        }

        if(!Status.isIsOnline())
        {
            offlineController.sendMessage(
                    messageTextField.getText(),
                    ShareImage.imageToString(attachmentTextField.getText()),
                    false,
                    0,
                    gpID,
                    timeTextField.getText());
        }

        messageTextField.clear();
        attachmentTextField.clear();
        timeTextField.clear();

        editingID = 0;
    }

}
