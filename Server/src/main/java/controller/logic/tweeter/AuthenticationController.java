package controller.logic.tweeter;

import Constants.Constants;
import DataBase.DataBase;
import controller.logic.messenger.ComposeController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.config.Config;
import shared.form.LoginForm;
import shared.form.UserInfoForm;
import shared.model.Group;
import shared.model.Messenger;
import shared.model.User;
import shared.response.AuthenticationResponse;
import shared.util.ShareImage;

public class AuthenticationController {

    private final Config config = new Config(Constants.CONFIG_ADDRESS);
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(AuthenticationController.class);

    private User user ;

    public User getUser() {
        return user;
    }

    public AuthenticationResponse login(LoginForm loginForm)
    {
        boolean validLogin = validLogin(loginForm);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(false, false, !validLogin);
        authenticationResponse.setUser(getUser());

        if(validLogin) {
            user = dataBase.users.getByUsername(loginForm.getUsername());
            user.setActive(true);
            dataBase.users.save(user);
        }

        return authenticationResponse;
    }

    public boolean validLogin(LoginForm loginForm)
    {
        logger.debug("Checking valid login for user : " + loginForm.getUsername() + "/ in authenticationController");

        User user = dataBase.users.getByUsername(loginForm.getUsername());

        if(user != null)
            return user.getPassword().equals(loginForm.getPass());

        logger.warn("invalid login for user : " + loginForm.getUsername() + " with pass : " + loginForm.getPass() + "/ in authenticationController");

        return false;
    }

    public AuthenticationResponse signUp (UserInfoForm signUpForm)
    {
        logger.warn("Checking valid sing up for user : " + signUpForm.getUsername() + "/ in authenticationController");

        boolean invalidUsername = checkValidUsername(signUpForm.getUsername());
        boolean invalidEmail = checkValidEmail(signUpForm.getEmail());
        boolean invalidPass = checkValidPass(signUpForm.getPass(), signUpForm.getPass2());
        boolean successful;

        AuthenticationResponse response = new AuthenticationResponse(invalidUsername, invalidEmail, invalidPass);
        successful = !invalidUsername && !invalidEmail && !invalidPass ;

        if(signUpForm.getUsername().equals("") || signUpForm.getPass().equals("") || signUpForm.getPass2().equals("") ||
                signUpForm.getName().equals("") || signUpForm.getLastName().equals("") || signUpForm.getEmail().equals(""))
            successful = false;

        if(!successful) {
            logger.warn("invalid sing up for user : " + signUpForm.getUsername() + "/ in authenticationController");

            return response;
        }

        user = new User(signUpForm);
        user.setId(dataBase.users.lastID()+1);

        Group savedMessage = new Group("Saved message", user.getId());
        savedMessage.setGroupID(dataBase.groups.lastID() + 1);

        Messenger messenger = new Messenger(user, savedMessage.getGroupID());
        messenger.setMessengerID(dataBase.messenger.lastID() + 1);
        user.setMessengerID(messenger.getMessengerID());

        user.setImageSTR(ShareImage.imageToString(config.getProperty(String.class, "DefaultPic").orElse("")));

        response.setUser(user);

        dataBase.users.save(user);
        dataBase.groups.save(savedMessage);
        dataBase.messenger.save(messenger);

        return response;
    }

    public boolean checkValidUsername(String username)
    {
        return dataBase.users.getByUsername(username) != null;
    }

    public boolean checkValidEmail(String email)
    {
        return dataBase.users.getByEmail(email) != null;
    }

    public boolean checkValidPass(String pass1, String pass2)
    {
        return !pass1.equals(pass2);
    }
}
