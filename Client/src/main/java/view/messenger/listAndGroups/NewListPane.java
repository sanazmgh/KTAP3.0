package view.messenger.listAndGroups;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class NewListPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String NEW_LIST = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"newListPane").orElse("");

    public NewListPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(NEW_LIST)));

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

    public NewListFXMLController getLoader() {
        return loader.getController();
    }
}
