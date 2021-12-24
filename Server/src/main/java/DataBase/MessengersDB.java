package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Messenger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

public class MessengersDB {
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();
    private Connection connection;
    static private final Logger logger = LogManager.getLogger(MessengersDB.class);


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean messengerExists(long messengerID)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `messengers` WHERE `messengerID` = ?");
            statement.setLong(1, messengerID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't check messenger by ID : " + messengerID + "/ in MessengersDB");
            System.err.println("couldn't check messenger by ID");
        }

        return false;
    }

    public Messenger get(long messengerID)
    {
        try
        {
            logger.warn("getting messenger by ID : " + messengerID + "/ in MessengersDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `messengers` WHERE `messengerID` = ?");
            statement.setLong(1, messengerID);
            ResultSet resultSet = statement.executeQuery();

            Messenger messenger = null;

            while (resultSet.next()) {
                messenger = new Messenger();
                messenger.setMessengerID(resultSet.getLong("messengerID"));
                messenger.setUserId(resultSet.getLong("userId"));
                messenger.setGroups(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("groups"), Long[].class))));
                messenger.setLists(new LinkedList<>(Arrays.asList(gson.fromJson(resultSet.getString("lists"), Long[].class))));
                messenger.setSavedMessages(resultSet.getLong("savedMessages"));

            }

            return messenger;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get messenger by ID : " + messengerID + "/ in MessengersDB");

            System.err.println("couldn't get messenger by ID");
        }

        return null;
    }

    public void save(Messenger messenger)
    {
        try
        {
            logger.warn("saving messenger : " + messenger.getMessengerID() + "/ in MessengersDB");

            PreparedStatement statement;

            if (messengerExists(messenger.getMessengerID())) {
                statement = connection.prepareStatement(
                        "UPDATE `messengers` SET `userId` = ?, `groups` = ?, `lists` = ?, `savedMessages` = ? " +
                                "WHERE `messengerID` = " + messenger.getMessengerID());
            }

            else {
                statement = connection.prepareStatement(
                        "INSERT INTO `messengers` (`userId`, `groups`, `lists`, `savedMessages`)" +
                                "VALUES (?, ?, ?, ?)");
            }

            statement.setLong(1, messenger.getUserId());
            statement.setString(2, gson.toJson(messenger.getGroups()));
            statement.setString(3, gson.toJson(messenger.getLists()));
            statement.setLong(4, messenger.getSavedMessages());

            statement.executeUpdate();
            statement.close();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't save messenger : " + messenger.getMessengerID() + "/ in MessengersDB");
            exception.printStackTrace();
        }
    }

    public long lastID()
    {
        try
        {
            logger.warn("getting last ID in messengers " + "/ in MessengersDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `messengers` WHERE `messengerID` = ( SELECT MAX(messengerID) FROM `messengers` ) ");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getLong("messengerID");

            else
                return 0;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get last ID in messengers " + "/ in MessengersDB");

            exception.printStackTrace();
        }

        return 0;
    }
}
