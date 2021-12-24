import Constants.Constants;
import DataBase.DataBase;
import controller.network.SocketHandler;
import shared.config.Config;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args)  throws SQLException {
        DataBase dataBase = DataBase.getDataBase();
        dataBase.connectToDb();
        dataBase.createDataBase();

        Config config = new Config(Constants.CONFIG_ADDRESS);
        SocketHandler socketHandler = new SocketHandler(config);
        socketHandler.start();
    }
}
