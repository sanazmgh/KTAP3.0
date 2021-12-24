package view.profile;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class ProfilePreViewPane {

    private Pane pane ;
    private final FXMLLoader loader;
    private static final String PREVIEW = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"profilePreViewPane").orElse("");

    public ProfilePreViewPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(PREVIEW)));

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

    public ProfilePreViewFXMLController getLoader() {
        return loader.getController();
    }
}
