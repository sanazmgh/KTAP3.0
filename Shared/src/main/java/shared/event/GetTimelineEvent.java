package shared.event;

import shared.response.Response;

public class GetTimelineEvent extends Event {

    private final String type;

    public GetTimelineEvent(String type)
    {
        this.type = type;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.getTimelineTweets(authToken, type);
    }
}
