package shared.response;

import shared.form.GroupDataForm;
import shared.form.ProfileInfoForm;
import shared.model.Message;
import shared.model.Notification;
import shared.model.Tweet;
import shared.model.User;

import java.util.LinkedList;

public interface ResponseVisitor {
    void visitAuthentication(User user, int autToken, boolean tokenUsername, boolean tokenEmail, boolean invalidPassword);
    void visitGetProfile(ProfileInfoForm form, boolean available);
    void visitEditedInfo(boolean tokenUsername, boolean tokenEmail, boolean invalidPassword, boolean successful);
    void visitLists(LinkedList<ProfileInfoForm> forms);
    void visitCommenting(long tweetID);
    void visitTweets(LinkedList<Tweet> tweets, LinkedList<ProfileInfoForm> users, String where, boolean isRootList);
    void visitSearchResult(ProfileInfoForm form, boolean successful);
    void visitNotification(LinkedList<Notification> notifications, LinkedList<ProfileInfoForm> users);
    void visitSettingsResponse(boolean successful);
    void visitSettingsInfo(boolean isPrivate, int lastSeen);
    void visitGetMessenger(LinkedList<GroupDataForm> forms);
    void visitChat(String gpName, long gpID, LinkedList<String> usernames, LinkedList<Message> messages, boolean blocked);
    void visitMessengerLists(LinkedList<GroupDataForm> forms, String type);
}
