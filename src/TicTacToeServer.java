import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TicTacToeServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("Server is running...");

            while (true) {
                Socket player1 = serverSocket.accept();
                System.out.println("Player 1 connected.");

                Socket player2 = serverSocket.accept();
                System.out.println("Player 2 connected.");

                // Start a new thread for the Tic Tac Toe game
                new Thread(new TicTacToeGame(player1, player2)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
