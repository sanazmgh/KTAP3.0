package shared.response;

public class EditInfoResponse extends Response {
    private final boolean tokenUsername;
    private final boolean tokenEmail;
    private final boolean invalidPassword;
    private final boolean successful;

    public EditInfoResponse(boolean tokenUsername, boolean tokenEmail, boolean invalidPassword, boolean successful)
    {
        this.tokenUsername = tokenUsername;
        this.tokenEmail = tokenEmail;
        this.invalidPassword = invalidPassword;
        this.successful = successful;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitEditedInfo(tokenUsername, tokenEmail, invalidPassword, successful);
    }
}
