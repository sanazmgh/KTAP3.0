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

public class SocketResponder{
    private final Socket socket;
    private final PrintStream printStream;
    private final Scanner scanner;
    private final Gson gson;

    public SocketResponder(Socket socket) throws IOException {
        this.socket = socket;
        this.scanner = new Scanner(socket.getInputStream());
        this.printStream = new PrintStream(socket.getOutputStream());
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Event.class, new Deserializer<>())
                .registerTypeAdapter(Response.class, new Serializer<>())
                .setDateFormat("MMM dd, yyyy, HH:mm")
                .create();
    }

    public Event getEvent() {
        String eventString = "";
        if(scanner.hasNext()) {
            eventString = scanner.nextLine();
        }
        return gson.fromJson(eventString, Event.class);
    }

    public void sendResponse(Response response) {
        printStream.println(gson.toJson(response, Response.class));
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
