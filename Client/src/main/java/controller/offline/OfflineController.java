package controller.offline;

import DataBase.Context;
import controller.MainController;
import shared.event.LoginEvent;
import shared.event.SettingsEvent;
import shared.event.SubmitMessageEvent;
import shared.form.GroupDataForm;
import shared.form.LoginForm;
import shared.form.UserInfoForm;
import shared.model.Message;
import shared.model.User;

import java.util.*;


public class OfflineController {
    public static OfflineController offlineController;
    private final Context context = Context.getContext();
    private MainController mainController;
    private UserInfoForm userInfo;

    private OfflineController() {}

    public static OfflineController getOfflineController()
    {
        if(offlineController == null)
            offlineController = new OfflineController();

        return offlineController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void validAuthentication(String username, String password)
    {
        UserInfoForm currentForm = context.users.getByUsername(username);
        if(currentForm == null)
            mainController.visitAuthentication(null, 0, false, false, true);

        else if(!currentForm.getPass().equals(password))
            mainController.visitAuthentication(null, 0, false, false, true);

        else
        {
            this.userInfo = currentForm;

            User user = new User(currentForm);
            user.setId(currentForm.getUserID());

            mainController.visitAuthentication(user, 0, false, false, false);
            mainController.addCashed(new LoginEvent(new LoginForm(username, password)));
        }
    }

    public void getMessenger()
    {
        LinkedList<GroupDataForm> groupForms = new LinkedList<>();

        for(long ID : userInfo.getGroups())
        {
            GroupDataForm currentForm = context.groups.get(ID);

            if(currentForm != null)
                groupForms.add(currentForm);
        }

        mainController.visitGetMessenger(groupForms);
    }

    public void getChat(long ID)
    {
        GroupDataForm currentForm = context.groups.get(ID);
        LinkedList<Message> messages = new LinkedList<>();

        for(long messageID : currentForm.getMessages())
        {
            Message currentMessage = context.messages.get(messageID);
            messages.add(currentMessage);
        }

        Collections.reverse(messages);
        mainController.visitChat(currentForm.getGpName(), currentForm.getGpID(), currentForm.getUsernames(), messages, currentForm.isBlocked());
    }

    public void sendMessage(String text, String imagePath, boolean forwarded, long forwardedFromID, long gpID, String date)
    {
        Message message = new Message (userInfo.getUserID(), userInfo.getPicSTR(), text, imagePath, false, "");

        if(date.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"))
        {
            String[] times = date.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));

            message.setSchedule(calendar.getTime());
        }

        else
            message.setSchedule(new Date());

        message.setMessageID(context.messages.lastID() + 1);

        GroupDataForm currentGroup = context.groups.get(gpID);
        currentGroup.getMessages().add(message.getMessageID());

        context.messages.add(message);
        context.groups.update(currentGroup);

        getChat(gpID);

        mainController.addCashed(new SubmitMessageEvent(text, imagePath, forwarded, forwardedFromID, gpID, date));

    }

    public void getSettings()
    {
        mainController.visitSettingsInfo(userInfo.isPrivate(), userInfo.getLastSeenMode());
    }

    public void setPrivacy(String privacy)
    {
        userInfo.setPrivate(privacy.equals("Private"));
        mainController.addCashed(new SettingsEvent("Privacy", privacy));
    }

    public void setLastSeen(String lastSeenMode)
    {
        int mode = 2;

        if(lastSeenMode.equals("Nobody"))
            mode = 0;

        if(lastSeenMode.equals("Everyone"))
            mode = 1;

        userInfo.setLastSeenMode(mode);
        mainController.addCashed(new SettingsEvent("LastSeen", lastSeenMode));
    }
}
