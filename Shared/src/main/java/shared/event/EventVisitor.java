package shared.event;

import shared.form.LoginForm;
import shared.form.UserInfoForm;
import shared.response.Response;

import java.util.LinkedList;

public interface EventVisitor {
    Response signUp(UserInfoForm form);
    Response login(LoginForm form);
    Response getProfile(long autToken, long ownerID);
    Response newTweet(long autToken, String text, String picPath, long retweetFromID, long commentedUnderID);
    Response changeInfo(long autToken, UserInfoForm form);
    Response changeStatus(long autToken, long ownerID, String type);
    Response getLists(long autToken, String type);
    Response tweetActions(long autToken, String action, long tweetID);
    Response newComment(long autToken, long tweetID);
    Response getTweetList(long autToken, String where, long tweetID, String action);
    Response getTimelineTweets(long autToken, String type);
    Response searchUsername(long autToken, String username);
    Response getNotification(long autToken, String type);
    Response responseToNotif(long autToken, String response, long notifID);
    Response getPrevStep(long autToken);
    Response getSettingsInfo(long autToken);
    Response getSettingsAction(long autToken, String type, String action);
    Response getMessenger(long autToken);
    Response submitMessage(long autToken, long editingID, String text, String imagePath, boolean forwarded, long forwardedFromID, long gpID, String date);
    Response composeMessage(long autToken, String text, String attachmentPath, LinkedList<String> names, boolean forwarded, String extraInfo);
    Response getGroup(long autToken, long gpID);
    Response newGroup(long autToken, String gpName, String username, String type);
    Response changeMembers(long autToken, String action, String type, long gpID, String username);
}
