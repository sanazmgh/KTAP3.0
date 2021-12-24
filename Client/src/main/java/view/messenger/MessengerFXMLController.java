package view.messenger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import listeners.StringListener;
import shared.event.GetListsEvent;
import shared.form.GroupDataForm;
import shared.listener.EventListener;
import view.messenger.chat.ChatPreViewPane;
import view.viewTools.ShowLists;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MessengerFXMLController implements Initializable {

    @FXML
    private Pane chat0Pane;
    @FXML
    private Pane chat1Pane;
    @FXML
    private Pane chat2Pane;
    @FXML
    private Pane chat3Pane;
    @FXML
    private Pane chat4Pane;
    @FXML
    private Pane chat5Pane;
    @FXML
    private Pane chat6Pane;
    @FXML
    private Pane chat7Pane;
    @FXML
    private Pane chat8Pane;
    @FXML
    private Pane chat9Pane;
    @FXML
    private Pane chat10Pane;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private Pane chatPane;

    private EventListener listener;
    private StringListener stringListener;
    private ShowLists showLists;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LinkedList<Pane> mainPanes = new LinkedList<>();
        mainPanes.add(chat0Pane);
        mainPanes.add(chat1Pane);
        mainPanes.add(chat2Pane);
        mainPanes.add(chat3Pane);
        mainPanes.add(chat4Pane);
        mainPanes.add(chat5Pane);
        mainPanes.add(chat6Pane);
        mainPanes.add(chat7Pane);
        mainPanes.add(chat8Pane);
        mainPanes.add(chat9Pane);
        mainPanes.add(chat10Pane);

        showLists = new ShowLists();
        showLists.setMainPanes(mainPanes);
        showLists.setButtons(backButton, nextButton);
    }


    public void setListener(EventListener listener, StringListener stringListener)
    {
        this.listener = listener;
        this.stringListener = stringListener;
    }

    public void setData(LinkedList<GroupDataForm> forms)
    {
        LinkedList<Pane> showPanes = new LinkedList<>();

        for(GroupDataForm form : forms)
        {
            ChatPreViewPane chatPreView = new ChatPreViewPane();
            chatPreView.getLoader().setListener(listener);
            chatPreView.getLoader().setData(form.getGpID(), form.getGpName(), form.getUnread());

            showPanes.add(chatPreView.getPane());
        }

        showLists.setShowPanes(showPanes);
        showLists.setPanes();
    }

    public void nextPage()
    {
        showLists.nextPage();
    }

    public void backPage()
    {
        showLists.prevPage();
    }

    public void lists()
    {
        listener.listen(new GetListsEvent("Lists"));
    }

    public void compose()
    {
        stringListener.listen("Compose");
    }

    public void newGroup()
    {
        listener.listen(new GetListsEvent("Groups"));
    }

    public Pane getChatPane() {
        return chatPane;
    }
}
