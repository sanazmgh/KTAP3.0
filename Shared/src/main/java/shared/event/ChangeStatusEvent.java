package shared.event;

import shared.response.Response;

public class ChangeStatusEvent extends Event {
    private final long ownerID;
    private final String type;

    public ChangeStatusEvent(long ownerID, String type)
    {
        this.ownerID = ownerID;
        this.type = type;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.changeStatus(authToken, ownerID, type);
    }
}
