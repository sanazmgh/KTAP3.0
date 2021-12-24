package shared.event;

import shared.response.Response;

public class ViewGroupEvent extends Event {
    private final long gpID;

    public ViewGroupEvent(long gpID)
    {
        this.gpID = gpID;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getGroup(authToken, gpID);
    }
}
