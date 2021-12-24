package shared.response;

import shared.model.User;

public class AuthenticationResponse extends Response {
    private User user = null;
    private int autToken;
    private final boolean tokenUsername;
    private final boolean tokenEmail;
    private final boolean invalidPassword;

    public AuthenticationResponse(boolean tokenUsername, boolean tokenEmail, boolean invalidPassword) {
        this.tokenUsername = tokenUsername;
        this.tokenEmail = tokenEmail;
        this.invalidPassword = invalidPassword;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAutToken(int autToken) {
        this.autToken = autToken;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitAuthentication(user, autToken, tokenUsername, tokenEmail, invalidPassword);
    }
}
