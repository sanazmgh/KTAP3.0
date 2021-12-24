package view.messenger;

import controller.offline.OfflineController;
import util.Constants;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import listeners.StringListener;
import shared.config.Config;
import shared.event.GetListsEvent;
import shared.event.ViewGroupEvent;
import shared.form.GroupDataForm;
import shared.listener.EventListener;
import shared.model.Message;
import shared.model.User;
import shared.util.Loop;
import util.Status;
import view.messenger.chat.ChatPane;
import view.messenger.compose.ComposePane;
import view.messenger.listAndGroups.MessengerListsPane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class MessengerPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String MESSENGER = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"messengerPane").orElse("");
    private final MessengerListsPane messengerListsPane;
    private final ComposePane composePane;
    private final MessengerListsPane messengerGroupsPane;
    private final ChatPane chatPane;
    private long currentChat = 0;
    private EventListener listener;
    private final OfflineController offlineController = OfflineController.getOfflineController();
    private String currentPane = "";
    private Loop loop;

    public MessengerPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(MESSENGER)));

        try {
            pane = loader.load();

        }

        catch (IOException e) {
            e.printStackTrace();
        }

        messengerListsPane = new MessengerListsPane();
        messengerListsPane.getLoader().setType("Lists");
        composePane = new ComposePane();
        messengerGroupsPane = new MessengerListsPane();
        messengerGroupsPane.getLoader().setType("Groups");
        chatPane = new ChatPane();
    }

    public Pane getPane() {
        return pane;
    }

    public MessengerFXMLController getLoader() {
        return loader.getController();
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
        StringListener stringListener = str -> {
            if (str.equals("Compose"))
                setCompose();
        };

        getLoader().setListener(listener, stringListener);
        messengerListsPane.getLoader().setListener(listener);
        composePane.getLoader().setListener(listener);
        messengerGroupsPane.getLoader().setListener(listener);
        chatPane.getLoader().setListener(listener);
    }

    public void setUser(User user) {
        chatPane.getLoader().setUser(user);
    }

    public void setMessengerLists(LinkedList<GroupDataForm> forms, String type)
    {
        MessengerListsPane currentType = (type.equals("Lists") ? messengerListsPane : messengerGroupsPane);
        if (!currentPane.equals(type))
        {
            Platform.runLater(
                    () ->
                    {
                        if(loop != null)
                            loop.stop();

                        currentPane = type;
                        currentType.getLoader().setData(forms);

                        getLoader().getChatPane().getChildren().clear();
                        getLoader().getChatPane().getChildren().add(currentType.getPane());

                        loop = new Loop(1 , this::updateMessengerLists);
                        loop.start();
                    }
            );
        }

        else {

            Platform.runLater(
                    () -> currentType.getLoader().setData(forms)
            );
        }
    }

    public void setCompose()
    {
        if (!currentPane.equals("Compose"))
        {
            Platform.runLater(
                    () ->
                    {
                        if(loop != null)
                            loop.stop();

                        currentPane = "compose";

                        getLoader().getChatPane().getChildren().clear();
                        getLoader().getChatPane().getChildren().add(composePane.getPane());

                    }
            );
        }
    }

    public void setChat(String gpName, long gpID, LinkedList<String> usernames, LinkedList<Message> messages, boolean blocked)
    {
        currentChat = gpID;

        if (!currentPane.equals("Chat"))
        {
            Platform.runLater(
                    () ->
                    {
                        if(loop != null)
                            loop.stop();

                        currentPane = "Chat";
                        chatPane.getLoader().setData(gpName, gpID, usernames, messages, blocked);

                        getLoader().getChatPane().getChildren().clear();
                        getLoader().getChatPane().getChildren().add(chatPane.getPane());

                        loop = new Loop(1 , this::updateChat);

                        if(Status.isIsOnline())
                            loop.start();
                    }
            );
        }

        else {

            Platform.runLater(
                    () -> {
                        chatPane.getLoader().setData(gpName, gpID, usernames, messages, blocked);
                        System.out.println("updating");
                    }

            );
        }
    }

    public void updateMessengerLists()
    {
        listener.listen(new GetListsEvent(currentPane));
    }

    public void updateChat()
    {
        listener.listen(new ViewGroupEvent(currentChat));

        if(!Status.isIsOnline())
        {
            offlineController.getChat(currentChat);
        }
    }

    public void closeLoop()
    {
        if(loop != null)
        loop.stop();
    }
}
