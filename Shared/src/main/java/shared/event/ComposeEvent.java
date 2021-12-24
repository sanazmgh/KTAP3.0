package shared.event;

import shared.response.Response;

import java.util.Arrays;
import java.util.LinkedList;

public class ComposeEvent extends Event {
    private final String text;
    private final String attachmentPath;
    private final LinkedList<String> names = new LinkedList<>();
    private final boolean forwarded;
    private final String extraInfo;

    public ComposeEvent(String text, String attachmentPath, String names, boolean forwarded, String extraInfo) {
        this.text = text;
        this.attachmentPath = attachmentPath;
        String[] parts = names.split("-");
        this.names.addAll(Arrays.asList(parts));
        this.forwarded = forwarded;
        this.extraInfo = extraInfo;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.composeMessage(authToken, text, attachmentPath, names, forwarded, extraInfo);
    }
}
