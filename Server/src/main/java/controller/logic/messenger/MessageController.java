package controller.logic.messenger;

import DataBase.DataBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Message;
import shared.model.User;

public class MessageController {
    private User user;
    private Message message;
    private final DataBase dataBase = DataBase.getDataBase();
    static private final Logger logger = LogManager.getLogger(MessageController.class);

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setMessage(long messageID) {

        this.message = dataBase.messages.get(messageID);
    }

    public String getName()
    {
        User sender = dataBase.users.get(message.getSenderId());
        return sender.getName() + " " + sender.getLastName();
    }

    public String getInfo()
    {
        String str = "";

        if(!message.getExtraInfo().equals(""))
            str += (message.isForwarded() ? "Forwarded from : " : "A tweet from : ") + message.getExtraInfo();

        return str;
    }

    public String getText()
    {
        return message.getText();
    }

    public String getProfile()
    {
        return user.getImageSTR();
    }

    public String getTweetImage()
    {
        return message.getImageSTR();
    }

    public void editText(String string)
    {
        logger.debug("Edit message : " + message.getMessageID() + "/ by user : " + user.getId() + "/ in messageController");

        message.setText(string);
        if(string.equals("Deleted message"))
            message.setDeleted(true);

        dataBase.messages.save(message);
    }

    public boolean isEditable()
    {
        return (user.getId() == message.getSenderId() && message.getExtraInfo().equals(""));
    }

    public boolean isDeleted()
    {
        return message.isDeleted();
    }
}
