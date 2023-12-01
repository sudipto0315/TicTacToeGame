import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons;
    private char currentPlayer;
    private char player1Symbol;
    private char player2Symbol;

    public TicTacToeGUI() {
        setTitle("Tic Tac Toe");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buttons = new JButton[3][3];
        currentPlayer = ' ';
        player1Symbol = ' ';
        player2Symbol = ' ';

        initializeButtons();
        setupGame();
    }

    private void initializeButtons() {
        setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonClickListener());
                add(buttons[i][j]);
            }
        }
    }

    private void setupGame() {
        String[] options = {"X", "O"};

        int result = JOptionPane.showOptionDialog(null, "Player 1, choose your symbol",
                "Symbol Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (result == 0) {
            player1Symbol = 'X';
            player2Symbol = 'O';
        } else {
            player1Symbol = 'O';
            player2Symbol = 'X';
        }

        currentPlayer = player1Symbol;
        JOptionPane.showMessageDialog(null, "Player 1: " + player1Symbol + "\nPlayer 2: " + player2Symbol);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton buttonClicked = (JButton) e.getSource();

            if (buttonClicked.getText().equals("")) {
                buttonClicked.setText(String.valueOf(currentPlayer));
                if (checkForWin()) {
                    JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " wins!");
                    resetGame();
                } else if (isBoardFull()) {
                    JOptionPane.showMessageDialog(null, "It's a draw!");
                    resetGame();
                } else {
                    currentPlayer = (currentPlayer == player1Symbol) ? player2Symbol : player1Symbol;
                }
            }
        }
    }

    private boolean checkForWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (checkThreeCells(buttons[i][0], buttons[i][1], buttons[i][2])) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (checkThreeCells(buttons[0][i], buttons[1][i], buttons[2][i])) {
                return true;
            }
        }

        // Check diagonals
        if (checkThreeCells(buttons[0][0], buttons[1][1], buttons[2][2]) ||
                checkThreeCells(buttons[0][2], buttons[1][1], buttons[2][0])) {
            return true;
        }

        return false;
    }

    private boolean checkThreeCells(JButton b1, JButton b2, JButton b3) {
        return b1.getText().equals(b2.getText()) && b2.getText().equals(b3.getText()) && !b1.getText().equals("");
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        setupGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToeGUI ttt = new TicTacToeGUI();
            ttt.setVisible(true);
        });
    }
}
