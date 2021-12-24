package shared.response;

public class SettingsResponse extends Response{

    private final boolean successfull;

    public SettingsResponse(boolean successfull)
    {
        this.successfull = successfull;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitSettingsResponse(successfull);
    }
}
