package org.example.viergewinnt;

// Model.java
public class Model {
    private final char[][] board;
    private final int rows = 6;
    private final int columns = 7;
    private final char empty = '.';

    public Model() {
        board = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = empty;
            }
        }
    }

    public boolean dropToken(int column, char token) {
        for (int i = rows - 1; i >= 0; i--) {
            if (board[i][column] == empty) {
                board[i][column] = token;
                return true;
            }
        }
        return false;
    }

    public boolean isColumnFull(int column) {
        return board[0][column] != empty;
    }

    public boolean isBoardFull() {
        for (int j = 0; j < columns; j++) {
            if (!isColumnFull(j)) return false;
        }
        return true;
    }

    public char checkWinner() {
        // Check horizontally, vertically, and diagonally
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char token = board[i][j];
                if (token == empty) continue;
                if (checkDirection(i, j, 1, 0, token) || // Horizontal
                        checkDirection(i, j, 0, 1, token) || // Vertical
                        checkDirection(i, j, 1, 1, token) || // Diagonal right
                        checkDirection(i, j, 1, -1, token)) // Diagonal left
                    return token;
            }
        }
        return empty;
    }

    private boolean checkDirection(int row, int col, int rowStep, int colStep, char token) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int r = row + i * rowStep;
            int c = col + i * colStep;
            if (r >= 0 && r < rows && c >= 0 && c < columns && board[r][c] == token) {
                count++;
            } else {
                break;
            }
        }
        return count == 4;
    }

    public char[][] getBoard() {
        return board;
    }
}
