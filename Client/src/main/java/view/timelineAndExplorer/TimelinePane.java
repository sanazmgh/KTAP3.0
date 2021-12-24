package view.timelineAndExplorer;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class TimelinePane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String TIMELINE = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"timelinePane").orElse("");

    public TimelinePane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(TIMELINE)));

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

    public TimeLineFXMLController getLoader() {
        return loader.getController();
    }
}
