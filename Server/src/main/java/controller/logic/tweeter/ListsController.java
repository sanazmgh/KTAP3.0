package controller.logic.tweeter;

import DataBase.DataBase;
import shared.form.ProfileInfoForm;
import shared.model.User;
import shared.response.GetUsersInfoFormResponse;

import java.util.LinkedList;

public class ListsController {
    private User user;
    private final DataBase dataBase = DataBase.getDataBase();

    public void setUser(User user) {
        this.user = user;
    }

    public GetUsersInfoFormResponse getList(String type)
    {
        user = dataBase.users.get(user.getId());
        LinkedList<ProfileInfoForm> forms = new LinkedList<>();

        LinkedList<Long> currentList = switch (type) {
            case "Followers" -> user.getFollowers();
            case "Followings" -> user.getFollowings();
            case "Muted" -> user.getMuted();
            case "Blocked" -> user.getBlocked();
            default -> new LinkedList<>();
        };

        ProfileController controller = new ProfileController();
        controller.setUser(user);

        for(long i : currentList)
        {
            User currentUser = dataBase.users.get(i);

            if(currentUser != null)
            {
                if(currentUser.isActive())
                {
                    controller.setOwner(i);
                    ProfileInfoForm currentForm = controller.makeForm();
                    forms.add(currentForm);
                }
            }
        }

        return new GetUsersInfoFormResponse(forms);

    }
}
