public class Agent {

	static final boolean DEBUG = false;	// Print debugging information
	private char[][] board;							// Board
	private int gridSize;								// Size of board
	private int middleCoord;						// Middle coordinate of the board
	private KnowledgeBase kb;						// Knowledge Base
	private boolean verbose;						// Print intermediate maps steps

	public Agent(String p, char[][] b, boolean verbose) {

		// Establish globabl variables
		this.board = b;
		this.gridSize = b.length - 1;
		this.middleCoord = gridSize / 2;
		this.kb = new KnowledgeBase(b);
		this.verbose = verbose;

		// Select agent to use
		switch (p) {
			case "P1":
				basicAgent();
			case "P2":
				beginnerAgent();
			case "P3":
				//TODO: Part 3
			case "P4":
				//TODO: Part 4
			case "P5":
				//TODO: Part 5
		}

  }

	/**
	 * Agent for task P1.
	 * Searches throught the board from top left to bottom right without the
	 * ability to mark cells.
	 * //TODO fix this!!
	 */
	private void basicAgent() {

		Cell nextCell = kb.getNextCell();
		while (nextCell != null) {

			if (verbose) {A2main.printBoard(board);}
			if (DEBUG) {kb.printKnowledgeBase();}

			probeCell(nextCell);

			// Check if the game is finished
			if (basicAgentFinished()) {
				break;
			}

			// Get the next cell
			nextCell = kb.getNextCell();
		}

		// If we reach here the game is solved
		A2main.gameOver(board, "\nResult: Agent alive: all solved\n");

	}

	/**
	 * Agent for task P2.
	 * Uses single point search strategy.
	 */
	private void beginnerAgent() {

		// Used to catch when agent not terminated and prevent infinite loops
		Cell repeatCell = null;

		 // Get the next unknown cell from the knowledge base
		Cell nextCell = kb.getNextCell();

		// For every unknow cell in the knowledge base...
		while (nextCell != null) {

			// Used to test when a cell has been updated or marked
			boolean edited = false;

			// Print information
			if (verbose & repeatCell == null) {A2main.printBoard(board);}
			if (DEBUG) {kb.printKnowledgeBase();}


			// If cell is 0,0 or middle cell we know it is safe so we can probe it
			// immediately
			if ((nextCell.getX() == 0 & nextCell.getY() == 0) | (nextCell.getX() == middleCoord & nextCell.getY() == middleCoord))  {

				if (DEBUG) {System.out.println("\nUncovering: " + nextCell);} // Debug information

				probeCell(nextCell);					// Probe this cell
				nextCell = kb.getNextCell();	// Get the next cell
				continue;											// Repeat while loop

			}

			if (DEBUG) {System.out.println("\nChecking uncovered neighbours of: " + nextCell);} // Debug information

			// Check every uncovered adjacent nieghbour of nextCell
			for (Cell temp : kb.getUncoveredAdjacentNeighbours(nextCell)) {

				if (DEBUG) {System.out.println("\tChecking: " + temp);} // Debug information

				// Error checking in case null value returned
				if (temp == null) {continue;}

				// If #clue = #dangers marked...
				if (allFreeNeighbours(temp) == true) {
					probeNeighbours(temp);	// Probe all neighbouring cells
					edited = true;					// Change edited to true

				// If #?cells = #clue - #marked
				} else if (allMarkedNeighbours(temp) == true) {
					markNeighbours(temp);		// Mark all neighbouring cells
					edited = true;					// Change edited to true

				}
			}

			// If the cell has not been edited (marked or uncovered), move it to the
			// end of the unknownCells list in the knowledge base to be re-done later.
			if (!edited) {

				if (DEBUG) {System.out.println("Moved cell " + nextCell + " to end of queue");} // Debug information

				// Move cell to end of the unknownCells list in the knowledge base
				kb.moveCellToEndOfQueue(nextCell);

				// RepeatCell works as a check that the agent cannot terminate. If the
				// cell cannot be edited, and we loop through the entire unknownCells
				// list coming back to same cell while nothing else has been edited,
				// then we know the agent is stuck and cannot terminate.
				if (repeatCell == null) {

					// The first time we cannot edit a cell set it to repeatCell
					repeatCell = nextCell;

				// If repeatCell == nextCell we have looped all the way around
				// unknowCells in the knowledge base editing nothing so we know that
				// the agent cannot terminate.
				} else if (repeatCell == nextCell){

					// Last check that we are finished.
					beginnerAgentFinished();
				}

			// If we have managed to edit a cell set repeatCell to null.
			} else {
				repeatCell = null;
			}

			// Get the next cell from the knowledge base
			nextCell = kb.getNextCell();

		}

		// If we get here then unknownCells in the knowledge base has ran out. This
		// method is used to see if we have one the game or are stuck before
		// ending the game.
		beginnerAgentFinished();

	}

	/**
	 * Probe the given cell. If it is a mine end the game, else update the cell
	 * and the board.
	 * @param cell cell to probe.
	 */
	public void probeCell(Cell cell) {

		// Get the character given by this cell
		char c = A2main.probeCell(cell.getX(), cell.getY());

		if (DEBUG) {System.out.println("\t\t\t\tProbing cell " + cell.toString() + " found " + c);}

		switch(c) {

			// If the character is a 'm' (mine), update the cell and end the game
			case 'm':
				updateCell(cell, '-');
				A2main.gameOver(board, "\nResult: Agent dead: found mine\n");
				break;
			case '0':
				updateCell(cell, c);
				probeNeighbours(cell);
				break;
			// If the character is anything else, update the cell
			default:
				updateCell(cell, c);
				// if clue = epty squares
				// create and add cell
		}

	}

	/**
	 * Update a given cell with the character c. Also updates the agent's view of
	 * the board alongside moving the cell from unknown to uncovered list in the
	 * Knowledge base.
	 * @param cell cell to update
	 * @param c    character
	 */
	public void updateCell(Cell cell, char c) {

		board[cell.getX()][cell.getY()] = c;
		cell.setChar(c);
		kb.moveFromUnknownToUncovered(cell);

	}

	/**
	 * Mark a given cell, update the Agents view of the board and move the cell
	 * from unknown to flagged list in the knowledge base.
	 * @param cell Cell to mark.
	 */
	public void markCell(Cell cell) {

		if (DEBUG) {System.out.println("\t\t\tMarking cell " + cell);}
		board[cell.getX()][cell.getY()] = '*';
		cell.setChar('*');
		kb.moveFromUnknownToFlagged(cell);

	}

	/**
	 * Checks that the number of neigbours already marked equals the clue in the
	 * given cell.
	 * @param  cell Cell to check
	 * @return      True or false if clue = number of marked neigbours
	 */
	private boolean allFreeNeighbours(Cell cell) {

		// Get cell value as an integer
		int cellVal = Character.getNumericValue(cell.getChar());

		// Get the number of marked adjacent cells
		int c = numberOfCInAdjacent(cell, '*');

		if (DEBUG) {System.out.println("\t\t" + cellVal + "(val) = " + c + "(*)?");}

		if (cellVal == c) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Checks that the number of unmarked and covered neighbour cells equals the
	 * cell clue subtract the number of already marked neigbour cells.
	 * @param  cell Cell to check
	 * @return      True or false if #unmarked&covered cells = clue - #marked cells
	 */
	private boolean allMarkedNeighbours(Cell cell) {

		// Get cell value as an integer
		int cellVal = Character.getNumericValue(cell.getChar());

		// Get the number of marked adjacent cells
		int c = numberOfCInAdjacent(cell, '*');

		// Get the number of unkown/covered adjacent cells
		int q = numberOfCInAdjacent(cell, '?');

		if (DEBUG) {System.out.println("\t\t" + q + "(?) = " + cellVal + "(val) - " + c + " (*)?");}

		if (numberOfCInAdjacent(cell, '?') == cellVal - numberOfCInAdjacent(cell, '*')) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Probe all uncovered neigbours of the given cell.
	 * @param cell Cell whose neighborus to probe.
	 */
	private void probeNeighbours(Cell cell) {

		// Get cell x and y coords.
		int x = cell.getX();
		int y = cell.getY();

		if (DEBUG) {System.out.println("\t\t\tProbing neighbours");}

		// (After boundry checking) If NW neighbour is uncovered (?) - probe it
		if (x != 0 & y != 0) {
      if (board[x-1][y-1] == '?') {probeCell(kb.getUnknownCell(x-1, y-1));}
    }

		// (After boundry checking) If N neighbour is uncovered (?) - probe it
    if (y != 0) {
			if (board[x][y-1] == '?') {probeCell(kb.getUnknownCell(x, y-1));}
    }

		// (After boundry checking) If NE neighbour is uncovered (?) - probe it
    if (x != gridSize & y != 0) {
			if (board[x+1][y-1] == '?') {probeCell(kb.getUnknownCell(x+1, y-1));}
    }

		// (After boundry checking) If W neighbour is uncovered (?) - probe it
    if (x != 0) {
			if (board[x-1][y] == '?') {probeCell(kb.getUnknownCell(x-1, y));}
    }

		// (After boundry checking) If E neighbour is uncovered (?) - probe it
    if (x != gridSize) {
			if (board[x+1][y] == '?') {probeCell(kb.getUnknownCell(x+1, y));}
    }

		// (After boundry checking) If SW neighbour is uncovered (?) - probe it
    if (x != 0 & y != gridSize) {
			if (board[x-1][y+1] == '?') {probeCell(kb.getUnknownCell(x-1, y+1));}
    }

		// (After boundry checking) If S neighbour is uncovered (?) - probe it
    if (y != gridSize) {
			if (board[x][y+1] == '?') {probeCell(kb.getUnknownCell(x, y+1));}
    }

		// (After boundry checking) If SE neighbour is uncovered (?) - probe it
    if (x != gridSize & y != gridSize) {
			if (board[x+1][y+1] == '?') {probeCell(kb.getUnknownCell(x+1, y+1));}
    }
	}

	/**
	 * Mark all uncovered neighbours of the given cell.
	 * @param cell Cell whose neighbours to mark.
	 */
	private void markNeighbours(Cell cell) {

		// Get cell x and y coords.
		int x = cell.getX();
		int y = cell.getY();

		// (After boundry checking) If NW neighbour is uncovered (?) - mark it
		if (x != 0 & y != 0) {
      if (board[x-1][y-1] == '?') {markCell(kb.getUnknownCell(x-1, y-1));}
    }

		// (After boundry checking) If N neighbour is uncovered (?) - mark it
    if (y != 0) {
			if (board[x][y-1] == '?') {markCell(kb.getUnknownCell(x, y-1));}
    }

		// (After boundry checking) If NE neighbour is uncovered (?) - mark it
    if (x != gridSize & y != 0) {
			if (board[x+1][y-1] == '?') {markCell(kb.getUnknownCell(x+1, y-1));}
    }

		// (After boundry checking) If W neighbour is uncovered (?) - mark it
    if (x != 0) {
			if (board[x-1][y] == '?') {markCell(kb.getUnknownCell(x-1, y));}
    }

		// (After boundry checking) If E neighbour is uncovered (?) - mark it
    if (x != gridSize) {
			if (board[x+1][y] == '?') {markCell(kb.getUnknownCell(x+1, y));}
    }

		// (After boundry checking) If SW neighbour is uncovered (?) - mark it
    if (x != 0 & y != gridSize) {
			if (board[x-1][y+1] == '?') {markCell(kb.getUnknownCell(x-1, y+1));}
    }

		// (After boundry checking) If S neighbour is uncovered (?) - mark it
    if (y != gridSize) {
			if (board[x][y+1] == '?') {markCell(kb.getUnknownCell(x, y+1));}
    }

		// (After boundry checking) If SE neighbour is uncovered (?) - mark it
    if (x != gridSize & y != gridSize) {
			if (board[x+1][y+1] == '?') {markCell(kb.getUnknownCell(x+1, y+1));}
    }
	}

	/**
	 * Find the number of a character in the adjcent cells of the given cell.
	 * @param  cell Cell
	 * @param  c    Character to count in adjacent neighbours
	 * @return      Conunt of c in adjacent neighbours
	 */
	private int numberOfCInAdjacent(Cell cell, char c) {

		int count = 0;
		int x = cell.getX();
		int y = cell.getY();

		if (x != 0 & y != 0) {
      if (board[x-1][y-1] == c) {count++;}
    }

    if (y != 0) {
			if (board[x][y-1] == c) {count++;}
    }

    if (x != gridSize & y != 0) {
			if (board[x+1][y-1] == c) {count++;}
    }

    if (x != 0) {
			if (board[x-1][y] == c) {count++;}
    }

    if (x != gridSize) {
			if (board[x+1][y] == c) {count++;}
    }

    if (x != 0 & y != gridSize) {
			if (board[x-1][y+1] == c) {count++;}
    }

    if (y != gridSize) {
			if (board[x][y+1] == c) {count++;}
    }

    if (x != gridSize & y != gridSize) {
			if (board[x+1][y+1] == c) {count++;}
    }

		return count;
	}

	/**
	 * Check if the game when using a basicAgent (p1) is complete.
	 * @return true or false if the game is finished
	 */
	private boolean basicAgentFinished() {

		// Loop through the board, scanning for every uncovered square that has a
		// digit on it. For each, check if the number uncovered nieghbours (squares
		// with a ?) equals the digit/clue of the square. If it does, continue to
		// the next square, else return false.
		for (int i = 0; i <= gridSize; i++) {
			for (int j = 0; j <= gridSize; j++) {

				// Get the integer value of the cell
				int cellValue = Character.getNumericValue(board[i][j]);

				if (cellValue >= 0 && cellValue <= 8) { // If square is 0-8

					// If number of ? neighbour cells = value of cell retrun false
					if (numberOfCInAdjacent(kb.getUncoveredCell(i, j), '?') != cellValue) {
						return false;

					}
				}
			}
		}

		return true;

	}

	/**
	 * Count if there are still uncovered cells in the board to test if we have
	 * solved the game or are stuck, then end the game.
	 */
	private void beginnerAgentFinished() {

		// Loop through the board. If we find a '?' (uncovered cell), we know we
		// have not finished the game so failed = true.
		boolean failed = false;
		for (int i = 0; i <= gridSize; i++) {
			for (int j = 0; j <= gridSize; j++) {
  			if (board[i][j] == '?') {
					failed = true;
					break;
				}
			}
		}

		// Call gameOver changing the ending message depending on if failed = true.
		if (failed) {
			A2main.gameOver(board, "\nResult: Agent not terminated\n");
		} else {
			A2main.gameOver(board, "\nResult: Agent alive: all solved\n");
		}


	}

}
