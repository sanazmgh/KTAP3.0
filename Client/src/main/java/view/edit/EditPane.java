package view.edit;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class EditPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String EDIT = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"editPane").orElse("");

    public EditPane(){
        this.loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(EDIT)));

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

    public EditFXMLController getLoader() {
        return loader.getController();
    }
}
