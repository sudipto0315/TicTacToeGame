import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 2;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_PLAYERS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running. Waiting for players...");

            while (true) {
                try {
                    // Wait for the first player
                    Socket player1Socket = serverSocket.accept();
                    System.out.println("Player 1 connected: " + player1Socket);

                    // Wait for the second player
                    Socket player2Socket = serverSocket.accept();
                    System.out.println("Player 2 connected: " + player2Socket);

                    // Create a game session for both players
                    executorService.submit(new TicTacToeGame(player1Socket, player2Socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
