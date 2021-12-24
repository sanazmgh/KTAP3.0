package shared.event;

import shared.response.Response;

public class GetNotificationsEvent extends Event {

    private final String type;

    public GetNotificationsEvent(String type)
    {
        this.type = type;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getNotification(authToken, type);
    }
}
