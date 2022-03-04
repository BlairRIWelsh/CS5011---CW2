import java.util.ArrayList;

/**
 * Main game handler class.
 * Loads the map and runs the given agent.
 */
public class A2main {

	private static char[][] board; 					// Game board
	private static Agent agent;							// Agent
	private static boolean verbose=false;		// Print intermediate steps

	public static void main(String[] args) {

		// Prints agent's view at each step if true
		if (args.length>2 && args[2].equals("verbose") ){
			verbose=true;
		}

		System.out.println("-------------------------------------------\n");
		System.out.println("Agent " + args[0] + " plays " + args[1] + "\n");

		// Load board
		World world = World.valueOf(args[1]);
		board = world.map;
		printBoard(board);

		System.out.println("Start!");

		// Create agent which will start solving the obscured board
		agent = new Agent(args[0], obscureBoard(board), verbose);

	}

	/**
	 * Returns the character at the given x,y coords of the board.
	 * @param  x X coord
	 * @param  y Y coord
	 * @return   character at coord x,y
	 */
	public static char probeCell(int x, int y) {
		return board[x][y];
	}

	/**
	 * Called when the game is over. Prints the final board and the endMessage.
	 * @param b          final board to print
	 * @param endMessage end message to print
	 */
	public static void gameOver(char[][] b, String endMessage) {
		System.out.println("Final map");
		printBoard(b);
		System.out.println(endMessage);
		System.exit(0);
	}

	/**
	 * Obscures the board by returning a new board with '?' on every non-blocked
	 * square.
	 * @param  board unobscured board
	 * @return       obscured board
	 */
	public static char[][] obscureBoard(char[][] board) {

		char[][] temp = new char[board.length][board.length];

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
  			if (board[i][j] != 'b') {
					temp[i][j] = '?';
				} else {
					temp[i][j] = 'b';
				}
			}
		}

		return temp;
	}

	/**
	 * Prints the board in the required format - PLEASE DO NOT MODIFY
	 * @param board board to print
	 */
	public static void printBoard(char[][] board) {
		System.out.println();
		// first line
		System.out.print("    ");
		for (int j = 0; j < board[0].length; j++) {
			System.out.print(j + " "); // x indexes
		}
		System.out.println();
		// second line
		System.out.print("    ");
		for (int j = 0; j < board[0].length; j++) {
			System.out.print("- ");// separator
		}
		System.out.println();
		// the board
		for (int i = 0; i < board.length; i++) {
			System.out.print(" "+ i + "| ");// index+separator
			for (int j = 0; j < board[0].length; j++) {
				System.out.print(board[i][j] + " ");// value in the board
			}
			System.out.println();
		}
		System.out.println();
	}



}
