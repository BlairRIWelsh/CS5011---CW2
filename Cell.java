/**
 * Class representing a cell.
 */
public class Cell {

    // X and Y coordinates
    private int x;
    private int y;

    // Character in the given cell.
    private char ch;

    public Cell(int x, int y) {

        // Set up global variables
        this.x = x;
        this.y = y;

        // All created cells are unknown (?) at the start
        this.ch = '?';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getChar() {
      return ch;
    }

    public void setChar(char c) {
      this.ch = c;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
