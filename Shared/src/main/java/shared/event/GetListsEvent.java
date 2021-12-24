package shared.event;

import shared.response.Response;

public class GetListsEvent extends Event{

    private final String type;

    public GetListsEvent(String type)
    {
        this.type = type;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getLists(authToken, type);
    }
}
