package controller.network;

import controller.logic.tweeter.SettingsController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.config.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler extends Thread{

    private final Config config;
    static private final Logger logger = LogManager.getLogger(SocketHandler.class);

    public SocketHandler(Config config)
    {
        this.config = config;
    }

    public void run()
    {
        try {
            logger.warn("a client connected to server " + "/ in socketHandler");

            int port = config.getProperty(Integer.class, "Port").orElse(8000);
            ServerSocket serverSocket = new ServerSocket(port);

            while(true)
            {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(new SocketResponder(socket));
                clientHandler.start();
            }
        }
        catch (IOException e) {
            logger.fatal("couldn't make the port " + "/ in socketHandler");
            System.err.println("couldn't make the port");
        }
    }
}
