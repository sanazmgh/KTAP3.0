package view.Lists;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class ListsPane{
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String LISTS = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"listsPane").orElse("");

    public ListsPane(){
        this.loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(LISTS)));

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

    public ListsFXMLController getLoader() {
        return loader.getController();
    }
}
