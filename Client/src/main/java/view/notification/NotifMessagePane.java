package view.notification;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class NotifMessagePane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String NOTIF_MESSAGE = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"NotifMessagePane").orElse("");

    public NotifMessagePane(){
        this.loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(NOTIF_MESSAGE)));

        try
        {
            pane = loader.load();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Pane getPane() {
        return pane;
    }

    public NotifMessageFXMLController getLoader() {
        return loader.getController();
    }
}
