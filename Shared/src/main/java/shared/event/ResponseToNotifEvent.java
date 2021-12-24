package shared.event;

import shared.response.Response;

public class ResponseToNotifEvent extends Event {
    private final String response;
    private final long notifID;

    public ResponseToNotifEvent(String response, long notifID)
    {
        this.response = response;
        this.notifID = notifID;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.responseToNotif(authToken, response, notifID);
    }
}
