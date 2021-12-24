package view.messenger.compose;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class ComposePane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String COMPOSE = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"composePane").orElse("");

    public ComposePane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(COMPOSE)));

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

    public ComposeFXMLController getLoader() {
        return loader.getController();
    }
}
