package view.tweet;

import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import shared.config.Config;

import java.io.IOException;
import java.util.Objects;

public class TweetPane {
    private Pane pane ;
    private final FXMLLoader loader;
    private static final String TWEET = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"tweetPane").orElse("");

    public TweetPane(){
        loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(TWEET)));

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

    public TweetFXMLController getLoader() {
        return loader.getController();
    }
}
