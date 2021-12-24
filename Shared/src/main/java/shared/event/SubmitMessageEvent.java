package shared.event;

import shared.response.Response;

public class SubmitMessageEvent extends Event {
    private long editingID = 0;
    private final String text;
    private String imagePath = "";
    private boolean forwarded = false;
    private long forwardedFromID = 0;
    private long gpID = 0;
    private String date = "";

    public SubmitMessageEvent(String text, String imagePath, boolean forwarded, long forwardedFromID, long gpID, String date) {
        this.text = text;
        this.imagePath = imagePath;
        this.forwarded = forwarded;
        this.forwardedFromID = forwardedFromID;
        this.gpID = gpID;
        this.date = date;
    }

    public SubmitMessageEvent(long editingID, String text) {
        this.editingID = editingID;
        this.text = text;
    }

    @Override
    public Response visit(EventVisitor eventVisitor) {
        return eventVisitor.submitMessage(authToken, editingID, text, imagePath, forwarded, forwardedFromID, gpID, date);
    }
}
