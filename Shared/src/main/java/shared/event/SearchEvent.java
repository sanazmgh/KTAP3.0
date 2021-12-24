package shared.event;

import shared.response.Response;

public class SearchEvent extends Event{

    private final String searchedUsername;

    public SearchEvent(String username)
    {
        this.searchedUsername = username;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.searchUsername(authToken, searchedUsername);
    }
}
