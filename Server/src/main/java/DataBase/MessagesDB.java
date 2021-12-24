package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MessagesDB {
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();
    private Connection connection;
    static private final Logger logger = LogManager.getLogger(MessagesDB.class);


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean messageExists(long messageID)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `messages` WHERE `messageID` = ?");
            statement.setLong(1, messageID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't check message by ID : " + messageID + "/ in MessagesDB");
            System.err.println("couldn't check message by ID");
        }

        return false;
    }

    public Message get(long messageID)
    {
        try
        {
            logger.warn("getting message by ID : " + messageID + "/ in MessagesDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `messages` WHERE `messageID` = ?");
            statement.setLong(1, messageID);
            ResultSet resultSet = statement.executeQuery();

            Message message = null;

            while (resultSet.next()) {
                message = new Message();
                message.setMessageID(resultSet.getLong("messageID"));
                message.setSenderId(resultSet.getLong("senderId"));
                message.setSenderImageSTR(resultSet.getString("senderImageSTR"));
                message.setText(resultSet.getString("text"));
                message.setExtraInfo(resultSet.getString("extraInfo"));
                message.setForwarded(resultSet.getBoolean("forwarded"));
                message.setImageSTR(resultSet.getString("imageSTR"));
                message.setDeleted(resultSet.getBoolean("deleted"));
                message.setStatus(resultSet.getInt("status"));
                message.setSchedule(new Date(resultSet.getLong("schedule")));
            }

            return message;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get message by ID : " + messageID + "/ in MessagesDB");
            System.err.println("couldn't get message by ID");
        }

        return null;
    }

    public void save(Message message)
    {
        try
        {
            logger.warn("saving message : " + message.getMessageID() + "/ in MessagesDB");

            PreparedStatement statement;

            if (messageExists(message.getMessageID())) {
                statement = connection.prepareStatement(
                        "UPDATE `messages` SET `senderId` = ?, `senderImageSTR` = ?, `text` = ?, `extraInfo` = ?,  " +
                                "`forwarded` = ?, `imageSTR` = ?, `deleted` = ?, `status` = ?, `schedule` = ? " +
                                "WHERE `messageID` = " + message.getMessageID());
            } else {
                statement = connection.prepareStatement(
                        "INSERT INTO `messages` (`senderId`, `senderImageSTR`, `text`, `extraInfo`,  " +
                                "`forwarded`, `imageSTR`, `deleted`, `status`, `schedule`)" +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }

            statement.setLong(1, message.getSenderId());
            statement.setString(2, message.getSenderImageSTR());
            statement.setString(3, message.getText());
            statement.setString(4, message.getExtraInfo());
            statement.setBoolean(5, message.isForwarded());
            statement.setString(6, message.getImageSTR());
            statement.setBoolean(7, message.isDeleted());
            statement.setInt(8, message.getStatus());
            statement.setLong(9, message.getSchedule().getTime());

            statement.executeUpdate();
            statement.close();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't save message : " + message.getMessageID() + "/ in MessagesDB");
            exception.printStackTrace();
        }
    }

    public long lastID()
    {
        try
        {
            logger.warn("getting last ID in messages " + "/ in MessagesDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `messages` WHERE `messageID` = ( SELECT MAX(messageID) FROM `messages` ) ");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getLong("messageID");

            else
                return 0;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't gat last ID in messages : " + "/ in MessagesDB");
            System.err.println("couldn't gat max ID in messages");
        }

        return 0;
    }
}
