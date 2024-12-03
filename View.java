// View.java
public class View {
    public void displayBoard(char[][] board) {
        System.out.println(" 0 1 2 3 4 5 6");
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(" " + cell);
            }
            System.out.println();
        }
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }
}
