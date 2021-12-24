package shared.event;

import shared.response.Response;

public class GetMessengerEvent extends Event{
    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getMessenger(authToken);
    }
}
