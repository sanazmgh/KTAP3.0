package controller.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.event.Event;
import shared.gson.Deserializer;
import shared.gson.Serializer;
import shared.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketHandler {
    private final Socket socket;
    private final PrintStream printStream;
    private final Scanner scanner;
    private final Gson gson;

    public SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.scanner = new Scanner(socket.getInputStream());
        this.printStream = new PrintStream(socket.getOutputStream());
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Response.class, new Deserializer<>())
                .registerTypeAdapter(Event.class, new Serializer<>())
                .setDateFormat("MMM dd, yyyy, HH:mm")
                .create();
    }

    public Response send(Event event) {
        String eventString = gson.toJson(event, Event.class);
        printStream.println(eventString);
        String responseString = "";
        if(scanner.hasNext()) {
            responseString = scanner.nextLine();
        }
        return gson.fromJson(responseString, Response.class);
    }

    public void close() {
        try {
            printStream.close();
            scanner.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
