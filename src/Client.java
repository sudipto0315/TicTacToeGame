// Client.java
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("Connected to the server.");

            // TODO: Implement client-side game logic and communication
            // You may need a loop to continuously receive updates from the server and update the UI.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
