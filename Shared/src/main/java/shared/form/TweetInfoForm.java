package shared.form;

public class TweetInfoForm {
    private final String tweetSTR;
    private final String tweetPic;

    public TweetInfoForm(String tweetSTR, String tweetPic)
    {
        this.tweetSTR = tweetSTR;
        this.tweetPic = tweetPic;
    }

    public String getTweetSTR() {
        return tweetSTR;
    }

    public String getTweetPic() {
        return tweetPic;
    }
}
