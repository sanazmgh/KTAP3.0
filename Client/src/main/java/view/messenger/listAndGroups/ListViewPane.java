package view.messenger.listAndGroups;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class ListViewPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String LIST_VIEW = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"listViewPane").orElse("");

    public ListViewPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(LIST_VIEW)));

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

    public ListViewFXMLController getLoader() {
        return loader.getController();
    }
}
