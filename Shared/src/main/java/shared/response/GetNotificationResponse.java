package shared.response;

import shared.form.ProfileInfoForm;
import shared.model.Notification;

import java.util.LinkedList;

public class GetNotificationResponse extends Response {
    private final LinkedList<Notification> notifications;
    private final LinkedList<ProfileInfoForm> users;

    public GetNotificationResponse(LinkedList<Notification> notifications, LinkedList<ProfileInfoForm> users)
    {
        this.notifications = notifications;
        this.users = users;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitNotification(notifications, users);
    }
}
