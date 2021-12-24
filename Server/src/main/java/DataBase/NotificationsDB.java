package DataBase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationsDB {
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat("MM dd, yyyy, HH:mm").setPrettyPrinting().create();
    private Connection connection;
    static private final Logger logger = LogManager.getLogger(NotificationsDB.class);

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean notificationExists(long notifID)
    {
        try
        {
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM `notifications` WHERE `notifID` = ?");
            statement.setLong(1, notifID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't check notif by ID : " + notifID + "/ in NotificationsDB");

            System.err.println("couldn't check notif by ID");
        }

        return false;
    }

    public Notification get(Long notifID)
    {
        try
        {
            logger.warn("getting notif by ID : " + notifID + "/ in NotificationsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `notifications` WHERE `notifID` = ?");
            statement.setLong(1, notifID);
            ResultSet resultSet = statement.executeQuery();

            Notification notif = null;

            while (resultSet.next()) {
                notif = new Notification();
                notif.setNotifID(resultSet.getLong("notifID"));
                notif.setSenderID(resultSet.getLong("senderID"));
                notif.setText(resultSet.getString("text"));
            }

            return notif;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't get by ID : " + notifID + "/ in NotificationsDB");

            System.err.println("couldn't get notif by ID");
        }

        return null;
    }

    public void save(Notification notif)
    {
        try
        {
            logger.warn("saving notif : " + notif.getNotifID() + "/ in NotificationsDB");

            PreparedStatement statement;

            if (notificationExists(notif.getNotifID())) {
                statement = connection.prepareStatement(
                        "UPDATE `notifications` SET `senderID` = ?, `text` = ? " +
                                "WHERE `notifID` = " + notif.getNotifID());
            }

            else {
                statement = connection.prepareStatement(
                        "INSERT INTO `notifications` (`senderID`, `text`)" +
                                "VALUES (?, ?)");
            }

            statement.setLong(1, notif.getSenderID());
            statement.setString(2, notif.getText());

            statement.executeUpdate();
            statement.close();
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't save notif : " + notif.getNotifID() + "/ in NotificationsDB");

            System.err.println("couldn't save notif");
        }
    }

    public long lastID()
    {
        try
        {
            logger.warn("getting last ID in notifs" + "/ in NotificationsDB");

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `notifications` WHERE `notifID` = ( SELECT MAX(notifID) FROM `notifications` ) ");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getLong("notifID");

            else
                return 0;
        }

        catch (SQLException exception)
        {
            logger.fatal("couldn't gat last ID in notifs" + "/ in NotificationsDB");

            System.err.println("couldn't gat max ID in notifs");
        }

        return 0;
    }
}
