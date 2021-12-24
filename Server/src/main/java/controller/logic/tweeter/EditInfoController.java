package controller.logic.tweeter;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.form.UserInfoForm;
import shared.model.User;
import shared.response.EditInfoResponse;

public class EditInfoController {

    static private final Logger logger = LogManager.getLogger(EditInfoController.class);
    private final DataBase dataBase = DataBase.getDataBase();
    private User user;

    public void setUser (User user) {
        this.user = user;
    }

    public EditInfoResponse checkValidation (UserInfoForm form)
    {
        logger.warn("Checking valid info in edit for user : " + form.getUsername() + "/ in editInfoController");

        user = dataBase.users.get(user.getId());

        boolean invalidUsername = !(checkValidUsername(form.getUsername()) || form.getUsername().equals(user.getUsername()));
        boolean invalidEmail = !(checkValidEmail(form.getEmail()) || form.getEmail().equals(user.getEmail()));
        boolean invalidPass = !(checkValidPass(form.getPass(), form.getPass2()));
        boolean successful;

        successful = !invalidUsername && !invalidEmail && !invalidPass ;

        if(form.getUsername().equals("") || form.getPass().equals("") || form.getPass2().equals("") ||
                form.getName().equals("") || form.getLastName().equals("") || form.getEmail().equals(""))
            successful = false;

        if(successful)
        {
            user.updateInfo(form);
            dataBase.users.save(user);
        }

        else
            logger.warn("invalid info in edit for user : " + form.getUsername() + "/ in editInfoController");

        return new EditInfoResponse(invalidUsername, invalidEmail, invalidPass, successful);
    }

    public boolean checkValidUsername(String username)
    {
        return dataBase.users.getByUsername(username) == null;
    }

    public boolean checkValidEmail(String email)
    {
        return dataBase.users.getByEmail(email) == null;
    }

    public boolean checkValidPass(String pass1, String pass2)
    {
        return pass1.equals(pass2);
    }
}
