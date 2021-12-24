package view.newTweet;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class NewTweetPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String NEW_TWEET = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"newTweetPane").orElse("");

    public NewTweetPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(NEW_TWEET)));

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

    public NewTweetFXMLController getLoader() {
        return loader.getController();
    }
}
