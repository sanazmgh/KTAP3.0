package shared.response;

public abstract class Response {
    public abstract void visit(ResponseVisitor responseVisitor);
}
