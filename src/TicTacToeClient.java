import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TicTacToeClient {
    private JFrame frame;
    private ArrayList<JButton> buttons;
    private boolean myTurn;
    private String symbol;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public TicTacToeClient() {
        frame = new JFrame("Tic Tac Toe");
        buttons = new ArrayList<>();
        myTurn = false;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 9; i++) {
            JButton button = new JButton();
            button.addActionListener(new ButtonListener());
            buttons.add(button);
            frame.add(button);
        }

        frame.setVisible(true);

        try {
            Socket socket = new Socket("localhost", 5555);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            myTurn = (boolean) input.readObject();
            symbol = myTurn ? "X" : "O";

            while (true) {
                Object received = input.readObject();

                if (received instanceof char[][]) {
                    char[][] board = (char[][]) received;
                    updateBoard(board);
                } else if (received instanceof Boolean) {
                    myTurn = (Boolean) received;
                } else if (received instanceof String) {
                    JOptionPane.showMessageDialog(frame, (String) received);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateBoard(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = buttons.get(i * 3 + j);
                char symbol = board[i][j];
                button.setText(symbol == ' ' ? "" : String.valueOf(symbol));
                button.setEnabled(symbol == ' ' && myTurn);
            }
        }
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (myTurn) {
                JButton button = (JButton) e.getSource();
                int index = buttons.indexOf(button);
                try {
                    output.writeObject(index); // Send the move to the server
                    myTurn = false;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new TicTacToeClient();
    }
}