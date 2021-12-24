package view.messenger.listAndGroups;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class MessengerListsPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String LIST = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"messengerListsPane").orElse("");

    public MessengerListsPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(LIST)));

        try {
            pane = loader.load();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pane getPane() {
        return pane;
    }

    public MessengerListsFXMLController getLoader() {
        return loader.getController();
    }
}
