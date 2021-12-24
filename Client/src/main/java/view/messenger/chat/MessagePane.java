package view.messenger.chat;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class MessagePane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String MESSAGE = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"messagePane").orElse("");

    public MessagePane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(MESSAGE)));

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

    public MessageFXMLController getLoader() {
        return loader.getController();
    }
}
