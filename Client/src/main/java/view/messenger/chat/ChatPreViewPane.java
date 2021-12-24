package view.messenger.chat;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class ChatPreViewPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String CHAT_PRE = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"chatPreViewPane").orElse("");

    public ChatPreViewPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(CHAT_PRE)));

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

    public ChatPreViewFXMLController getLoader() {
        return loader.getController();
    }
}
