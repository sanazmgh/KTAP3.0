package shared.event;

import shared.response.Response;

public class PreProfileEvent extends Event{
    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getPrevStep(authToken);
    }
}
