package view.settings;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class SettingsPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String SETTINGS = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"settingsPane").orElse("");

    public SettingsPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(SETTINGS)));

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

    public SettingsFXMLController getLoader() {
        return loader.getController();
    }
}
