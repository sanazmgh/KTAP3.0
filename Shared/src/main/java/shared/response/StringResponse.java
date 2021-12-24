package shared.response;

public class StringResponse extends Response{
    private final String str;

    public StringResponse(String str)
    {
        this.str = str;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {

    }
}
