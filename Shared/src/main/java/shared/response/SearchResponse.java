package shared.response;

import shared.form.ProfileInfoForm;

public class SearchResponse extends Response{
    private final ProfileInfoForm form;
    private final boolean successful;

    public SearchResponse(ProfileInfoForm form, boolean successful)
    {
        this.form = form;
        this.successful = successful;
    }

    @Override
    public void visit(ResponseVisitor responseVisitor) {
        responseVisitor.visitSearchResult(form, successful);
    }
}
