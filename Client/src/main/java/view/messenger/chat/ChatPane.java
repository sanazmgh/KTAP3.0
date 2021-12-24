package view.messenger.chat;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class ChatPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String CHAT = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"chatPane").orElse("");

    public ChatPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(CHAT)));

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

    public ChatFXMLController getLoader() {
        return loader.getController();
    }
}
