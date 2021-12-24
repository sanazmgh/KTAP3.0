package view.authentication;

import listeners.BackOnlineListener;
import util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import listeners.StringListener;
import shared.config.Config;
import shared.listener.EventListener;

import java.io.IOException;
import java.util.Objects;

public class SignUpScene{
    private final Scene scene ;
    private final FXMLLoader loader;
    private static final String SIGNUP = new Config(Constants.CONFIG_ADDRESS)
            .getProperty(String.class,"signUpScene").orElse("");

    public SignUpScene(){
        this.loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(SIGNUP)));
        Parent root = null;
        try
        {
            root = loader.load();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        assert root != null;
        scene = new Scene(root);
    }

    public void setListener(EventListener listener, StringListener stringListener, BackOnlineListener backOnlineListener)
    {
        getLoader().setListener(listener);
        getLoader().setStringListener(stringListener);
        getLoader().setBackOnlineListener(backOnlineListener);
    }

    public Scene getScene() {
        return scene;
    }

    public SignUpFXMLController getLoader() {
        return loader.getController();
    }
}
