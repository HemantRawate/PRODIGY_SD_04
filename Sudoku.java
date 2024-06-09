import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Sudoku extends JFrame {
    private JTextField[][] grid;
    private JButton solveButton, clearButton;
    private JPanel panel;

    public Sudoku() {
        createView();

        setTitle("Sudoku Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createView() {
        panel = new JPanel(new GridLayout(10, 9));
        getContentPane().add(panel);

        grid = new JTextField[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row][col] = new JTextField();
                grid[row][col].setHorizontalAlignment(JTextField.CENTER);
                grid[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                grid[row][col].setBorder(createCellBorder(row, col));
                panel.add(grid[row][col]);
            }
        }

        solveButton = new JButton("Solve");
        solveButton.addActionListener(new SolveButtonActionListener());
        panel.add(solveButton);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearButtonActionListener());
        panel.add(clearButton);
    }

    private Border createCellBorder(int row, int col) {
        int top = 1, left = 1, bottom = 1, right = 1;

        if (row % 3 == 0) top = 3;
        if (col % 3 == 0) left = 3;
        if (row == 8) bottom = 3;
        if (col == 8) right = 3;

        return BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK);
    }

    private class SolveButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[][] board = new int[9][9];
            try {
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        String text = grid[row][col].getText();
                        if (!text.isEmpty()) {
                            board[row][col] = Integer.parseInt(text);
                        } else {
                            board[row][col] = 0;
                        }
                    }
                }
                if (SudokuSolver.solveSudoku(board)) {
                    for (int row = 0; row < 9; row++) {
                        for (int col = 0; col < 9; col++) {
                            grid[row][col].setText(String.valueOf(board[row][col]));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(Sudoku.this, "No solution exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Sudoku.this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ClearButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    grid[row][col].setText("");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Sudoku().setVisible(true);
        });
    }
}

class SudokuSolver {
    private static final int SIZE = 9;

    public static boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;

                            if (solveSudoku(board)) {
                                return true;
                            }

                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }
}