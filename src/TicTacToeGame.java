import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TicTacToeGame implements Runnable {
    private Socket player1;
    private Socket player2;
    private ObjectInputStream input1;
    private ObjectOutputStream output1;
    private ObjectInputStream input2;
    private ObjectOutputStream output2;
    private char[][] board;
    private boolean isGameOver;

    public TicTacToeGame(Socket player1, Socket player2) {
        this.player1 = player1;
        this.player2 = player2;

        try {
            output1 = new ObjectOutputStream(player1.getOutputStream());
            input1 = new ObjectInputStream(player1.getInputStream());
            output2 = new ObjectOutputStream(player2.getOutputStream());
            input2 = new ObjectInputStream(player2.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        board = new char[3][3];
        isGameOver = false;

        // Initialize the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    @Override
    public void run() {
        try {
            output1.writeObject(true); // Player 1's turn
            output2.writeObject(false); // Player 2's turn

            boolean player1Turn = true;

            while (!isGameOver) {
                int move;
                char symbol;

                if (player1Turn) {
                    move = (int) input1.readObject();
                    symbol = 'X';
                } else {
                    move = (int) input2.readObject();
                    symbol = 'O';
                }

                makeMove(move, symbol);
                player1Turn = !player1Turn;
                output1.writeObject(player1Turn); // Notify Player 1
                output2.writeObject(!player1Turn); // Notify Player 2
                updateClients();

                if (checkWin(symbol) || checkTie()) {
                    isGameOver = true;
                    sendGameOverMessage();
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void makeMove(int move, char symbol) {
        int row = move / 3;
        int col = move % 3;

        if (board[row][col] == ' ') {
            board[row][col] = symbol;
        }
    }

    private void updateClients() {
        try {
            output1.reset();
            output1.writeObject(board);
            output2.reset();
            output2.writeObject(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkWin(char symbol) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }

        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true;
        }

        return false;
    }

    private boolean checkTie() {
        // Check if the board is full
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false; // The board is not full
                }
            }
        }

        return true; // The board is full, and no one has won
    }

    private void sendGameOverMessage() {
        try {
            if (checkWin('X')) {
                output1.writeObject("Game over: X won!");
                output2.writeObject("Game over: X won!");
            } else if (checkWin('O')) {
                output1.writeObject("Game over: O won!");
                output2.writeObject("Game over: O won!");
            } else if (checkTie()) {
                output1.writeObject("Game over: It's a tie!");
                output2.writeObject("Game over: It's a tie!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}