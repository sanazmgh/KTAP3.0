package shared.model;

import java.util.Comparator;
import java.util.Date;

public class Message implements Comparator<Message> {
    private long messageID;
    private long senderId;
    private String senderName;
    private String senderImageSTR;
    private String text;
    private String extraInfo;
    private boolean forwarded;
    private String imageSTR;
    private boolean deleted;
    private int status = 1;
    private Date schedule;

    public Message() {}

    public Message(long userId, String senderImageSTR, String text, String imageSTR, boolean forwarded, String extraInfo)
    {
        this.senderId = userId;
        this.senderImageSTR = senderImageSTR;
        this.text = text;
        this.forwarded = forwarded;
        this.extraInfo = extraInfo;
        this.imageSTR = imageSTR;
        this.deleted = false;
        schedule = new Date();
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImageSTR() {
        return senderImageSTR;
    }

    public void setSenderImageSTR(String senderImageSTR) {
        this.senderImageSTR = senderImageSTR;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public boolean isForwarded() {
        return forwarded;
    }

    public void setForwarded(boolean forwarded) {
        this.forwarded = forwarded;
    }

    public String getImageSTR() {

        return imageSTR;
    }

    public void setImageSTR(String imageSTR) {
        this.imageSTR = imageSTR;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }


    @Override
    public int compare(Message message1, Message message2) {
        if(message1.getSchedule().before(message2.getSchedule()))
            return 1;

        return -1;
    }
}
