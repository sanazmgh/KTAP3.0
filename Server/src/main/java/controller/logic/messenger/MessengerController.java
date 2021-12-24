package controller.logic.messenger;

import DataBase.DataBase;
import shared.form.GroupDataForm;
import shared.model.*;
import shared.response.GetMessengerListsResponse;
import shared.response.GetMessengerResponse;
import shared.response.ViewChatResponse;
import shared.util.Pair;

import java.util.Date;
import java.util.LinkedList;

public class MessengerController {
    private User user;
    private Messenger messenger;
    private final ChatController chatController;
    private final ComposeController composeController;
    private final MessengerListsController messengerListsController;
    private final NewMessengerListController newMessengerListController;

    private final DataBase dataBase = DataBase.getDataBase();

    public MessengerController()
    {
        chatController = new ChatController();
        composeController = new ComposeController();
        messengerListsController = new MessengerListsController();
        newMessengerListController = new NewMessengerListController();
    }

    public void setUser(User user)
    {
        this.user = user;
        this.messenger = dataBase.messenger.get(user.getMessengerID());
        chatController.setUser(user);
        composeController.setUser(user);
        messengerListsController.setUser(user);
        newMessengerListController.setUser(user);
    }

    public User getUser() {
        return user;
    }

    public ViewChatResponse getGroup(long ind)
    {
        chatController.setGroup(ind);
        return chatController.getChat();
    }

    public LinkedList<GroupDataForm> getGroupForms()
    {
        LinkedList<GroupDataForm> forms = new LinkedList<>();
        for(long gpID : messenger.getGroups())
        {
            Group currentGP = dataBase.groups.get(gpID);
            String name = currentGP.getName();
            boolean blocked = false;
            if(currentGP.getMembers().size() == 2)
            {
                long otherUserID = currentGP.getMembers().get(0).getKey().equals(user.getId()) ?
                        currentGP.getMembers().get(1).getKey() : currentGP.getMembers().get(0).getKey();

                User otherUser = dataBase.users.get(otherUserID);
                name = otherUser.getUsername();
                blocked = otherUser.getBlocked().contains(user.getId());
            }

            LinkedList<String> usernames = new LinkedList<>();

            for(Pair users : currentGP.getMembers())
                usernames.add(dataBase.users.get(users.getKey()).getUsername());

            for(int i = currentGP.getMessages().size()-1 ; i>=0 ; i--)
            {
                Message currentMessage = dataBase.messages.get(currentGP.getMessages().get(i));
                if(currentMessage.getStatus() < 3)
                {
                    if(currentMessage.getSenderId() != user.getId()) {
                        currentMessage.setStatus(3);
                        dataBase.messages.save(currentMessage);
                    }
                }

                else
                    break;
            }

            LinkedList<Long> toRemove = new LinkedList<>();
            for(long scheduleID : currentGP.getScheduledMessages())
            {
                Message currentSchedule = dataBase.messages.get(scheduleID);
                Date currentDate = new Date();

                if(currentSchedule.getSchedule().before(currentDate) ) {
                    toRemove.add(scheduleID);
                    if(currentSchedule.getSenderId() != user.getId())
                    {
                        currentSchedule.setStatus(3);
                        dataBase.messages.save(currentSchedule);
                    }
                }
            }

            currentGP.getScheduledMessages().removeAll(toRemove);
            currentGP.getMessages().addAll(toRemove);
            dataBase.groups.save(currentGP);

            GroupDataForm currentForm = new GroupDataForm(name, gpID, currentGP.getUnread(user.getId()), usernames, currentGP.getMessages(), blocked);
            forms.add(currentForm);
        }

        //Collections.reverse(forms);
        return forms;
    }

    public GetMessengerResponse getChats()
    {
        rearrangeChats();

        return new GetMessengerResponse(getGroupForms());
    }

    public void rearrangeChats()
    {
        this.messenger = dataBase.messenger.get(user.getMessengerID());
        user = dataBase.users.get(user.getId());

        LinkedList<Long> newArrange = new LinkedList<>();

        for(int i=0 ; i<messenger.getGroups().size() ; i++)
        {
            Group group = dataBase.groups.get(messenger.getGroups().get(i));

            if(group.getUnread(user.getId()) > 0)
                newArrange.addFirst(group.getGroupID());

            else
                newArrange.addLast(group.getGroupID());
        }

        messenger.getGroups().clear();
        messenger.getGroups().addAll(newArrange);
    }

    public void changeMembersInGroup(String action, long gpID, String username)
    {
        chatController.setGroup(gpID);

        if(action.equals("Add"))
            chatController.addMember(username);

        else
            chatController.leaveTheChat();
    }

    public void changeMembersInList(String action, long listID, String username)
    {
        messengerListsController.setList(listID);

        if(action.equals("Add"))
            messengerListsController.add(username);

        else
            messengerListsController.remove(username);
    }

    public void sendAMessage(long editingID, String text, String imagePath, boolean forwarded, long forwardedFromID, long gpID, String date)
    {
        chatController.setGroup(gpID);
        chatController.sendMessage(editingID, text, imagePath, forwarded, forwardedFromID, date);
    }

    public GetMessengerListsResponse getLists(String type)
    {
        LinkedList<GroupDataForm> forms = new LinkedList<>();
        this.messenger = dataBase.messenger.get(user.getMessengerID());
        user = dataBase.users.get(user.getId());

        if(type.equals("Lists"))
        {
            for(long listID : messenger.getLists())
            {
                List currentList = dataBase.lists.get(listID);
                LinkedList<String> usernames = new LinkedList<>();

                for(long userID : currentList.getMembers())
                {
                    User currentUser = dataBase.users.get(userID);
                    usernames.add(currentUser.getUsername());
                }

                GroupDataForm currentForm = new GroupDataForm(currentList.getName(), listID, 0, usernames, null, false);
                forms.add(currentForm);
            }
        }

        else
        {
            forms = getGroupForms();
            System.out.println("getting groups");
        }

        return new GetMessengerListsResponse(forms, type);
    }

    public ChatController getChatController() {
        return chatController;
    }

    public ComposeController getComposeController() {
        return composeController;
    }

    public MessengerListsController getMessengerListsController() {
        return messengerListsController;
    }

    public NewMessengerListController getNewMessengerListController() {
        return newMessengerListController;
    }
}
