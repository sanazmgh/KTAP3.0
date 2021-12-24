package view;

import javafx.application.Platform;
import javafx.stage.Stage;
import listeners.BackOnlineListener;
import listeners.StringListener;
import shared.listener.EventListener;
import shared.model.User;
import view.authentication.LoginScene;
import view.authentication.SignUpScene;
import view.mainPage.MainPageScene;

public class MainStage {

    private final Stage stage;
    private final SignUpScene signUpScene = new SignUpScene();
    private final LoginScene loginScene = new LoginScene();
    private final MainPageScene mainPageScene = new MainPageScene();

    public MainStage(Stage stage, EventListener listener, BackOnlineListener backOnlineListener){
        this.stage = stage;

        StringListener stringListener = str -> {
            if(str.equals("Login"))
                stage.setScene(loginScene.getScene());

            if(str.equals("Sign up"))
                stage.setScene(signUpScene.getScene());
        };

        loginScene.setListener(listener, stringListener);
        signUpScene.setListener(listener, stringListener, backOnlineListener);
        mainPageScene.setListener(listener, backOnlineListener);

        stage.setTitle("KTAP");
        stage.setScene(loginScene.getScene());
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void enter(User user)
    {
        Platform.runLater(
                () ->
                {
                    mainPageScene.setUser(user);
                    stage.setScene(mainPageScene.getScene());
                }
        );
    }

    public  void setAuthenticationErrors(boolean tokenUsername, boolean tokenEmail, boolean invalidPassword)
    {
        if(stage.getScene().equals(loginScene.getScene()) && invalidPassword)
            loginScene.getLoader().visibleError();

        else
        {
            signUpScene.getLoader().resetErrors();
            signUpScene.getLoader().invalidUsername(tokenUsername);
            signUpScene.getLoader().invalidEmail(tokenEmail);
            signUpScene.getLoader().invalidPass(invalidPassword);
        }

    }

    public MainPageScene getMainPageScene() {
        return mainPageScene;
    }
}
