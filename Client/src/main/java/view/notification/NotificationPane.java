package view.notification;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class NotificationPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String NOTIFICATION = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"notificationPane").orElse("");

    public NotificationPane(){
        this.loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(NOTIFICATION)));

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

    public NotificationFXMLController getLoader() {
        return loader.getController();
    }
}
