import java.io.*;
import java.net.*;

public class TicTacToeGame implements Runnable {
    private Socket player1Socket;
    private Socket player2Socket;

    public TicTacToeGame(Socket player1Socket, Socket player2Socket) {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out1 = new ObjectOutputStream(player1Socket.getOutputStream());
                ObjectInputStream in1 = new ObjectInputStream(player1Socket.getInputStream());
                ObjectOutputStream out2 = new ObjectOutputStream(player2Socket.getOutputStream());
                ObjectInputStream in2 = new ObjectInputStream(player2Socket.getInputStream());
        ) {
            // TODO: Implement game logic for both players

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close resources and handle player disconnection
            try {
                player1Socket.close();
                player2Socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
