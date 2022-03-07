import java.util.HashSet;
import java.util.LinkedList;

/**
 * Class represnting the Knowledge base an agent holds.
 */
public class KnowledgeBase {

  private int gridSize; // Size of grid

  // Lists representing cells, unkown, flagged, and uncovered in the KB
  private LinkedList<Cell> unknownCells = new LinkedList<Cell>();
  private LinkedList<Cell> flaggedCells = new LinkedList<Cell>();
  private LinkedList<Cell> uncoveredCells = new LinkedList<Cell>();

  /**
   * Constructor for KnowledgeBase.
   * @param b Obscured board
   */
  public KnowledgeBase(char[][] b) {

    this.gridSize = b.length - 1;
    setUpKnowledgeBase(b);

  }

  /**
  * Sets up the knowledge base by creating every unblocked cell on the board
  * and adding them to the unknownCells list.
  * @param b board
  */
  private void setUpKnowledgeBase(char[][] b) {

    int size = b.length;
    int middle = size/2 ;//+ size % 2;


     unknownCells.add(new Cell(0, 0));
     unknownCells.add(new Cell(middle, middle));

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
         if (b[i][j] != 'b' & !(i == 0 & j == 0) & !(i == middle & j == middle)) {
//        if (b[i][j] != 'b') {
          unknownCells.add(new Cell(i, j));
        }
      }
    }


  }

  // GET CELL METHODS

  /**
   * Get the next cell in the unknown cells list.
   * @return Next cell
   */
  public Cell getNextCell() {
    if (unknownCells.isEmpty()) {
      return null;
    } else {
      Cell temp = unknownCells.getFirst();
      return temp;
    }
  }

  /**
   * Get the cell given by x,y in the unkownCell list
   * @param  x x coordinate
   * @param  y y coordinate
   * @return   Cell
   */
  public Cell getUnknownCell(int x, int y) {
    for (Cell temp : unknownCells) {
        if (temp.getX() == x & temp.getY() == y) {
          return temp;
        }
     }
     return null;
  }

  /**
   * Get the cell given by x,y in the uncoveredCell list
   * @param  x x coordinate
   * @param  y y coordinate
   * @return   Cell
   */
  public Cell getUncoveredCell(int x, int y) {
    for (Cell temp : uncoveredCells) {
        if (temp.getX() == x & temp.getY() == y) {
          return temp;
        }
     }
     return null;
  }

  public Cell getFlaggedCell(int x, int y) {
    for (Cell temp : flaggedCells) {
      if (temp.getX() == x & temp.getY() == y) {
        return temp;
      }
    }
    return null;
  }

  /**
   * Get all the uncovered neigbours of the given cell.
   * @param  cell Given cell.
   * @return      List of all uncovered neighbours of the cell.
   */
  public LinkedList<Cell> getUncoveredAdjacentNeighbours(Cell cell) {

    int x = cell.getX();
    int y = cell.getY();

    LinkedList<Cell> temp = new LinkedList<Cell>();

    if (x != 0 & y != 0) {
      if (getUncoveredCell(x-1, y-1) != null) {temp.add(getUncoveredCell(x-1, y-1));}
    }

    if (y != 0) {
      if (getUncoveredCell(x, y-1) != null) {temp.add(getUncoveredCell(x, y-1));}
    }

    if (x != gridSize & y != 0) {
      if (getUncoveredCell(x+1, y-1) != null) {temp.add(getUncoveredCell(x+1, y-1));}
    }

    if (x != 0) {
      if (getUncoveredCell(x-1, y) != null) {temp.add(getUncoveredCell(x-1, y));}
    }

    if (x != gridSize) {
      if (getUncoveredCell(x+1, y) != null) {temp.add(getUncoveredCell(x+1, y));}
    }

    if (x != 0 & y != gridSize) {
      if (getUncoveredCell(x-1, y+1) != null) {temp.add(getUncoveredCell(x-1, y+1));}
    }

    if (y != gridSize) {
      if (getUncoveredCell(x, y+1) != null) {temp.add(getUncoveredCell(x, y+1));}
    }

    if (x != gridSize & y != gridSize) {
      if (getUncoveredCell(x+1, y+1) != null) {temp.add(getUncoveredCell(x+1, y+1));}
    }

    return temp;

  }

  /**
   * Get all the unkown neigbours of the given cell.
   * @param  cell Given cell.
   * @return      List of all unknown neighbours of the cell.
   */
  public LinkedList<Cell> getUnkownAdjacentNeighbours(Cell cell) {

    int x = cell.getX();
    int y = cell.getY();

    LinkedList<Cell> temp = new LinkedList<Cell>();

    if (x != 0 & y != 0) {
      if (getUnknownCell(x-1, y-1) != null) {temp.add(getUnknownCell(x-1, y-1));}
    }

    if (y != 0) {
      if (getUnknownCell(x, y-1) != null) {temp.add(getUnknownCell(x, y-1));}
    }

    if (x != gridSize & y != 0) {
      if (getUnknownCell(x+1, y-1) != null) {temp.add(getUnknownCell(x+1, y-1));}
    }

    if (x != 0) {
      if (getUnknownCell(x-1, y) != null) {temp.add(getUnknownCell(x-1, y));}
    }

    if (x != gridSize) {
      if (getUnknownCell(x+1, y) != null) {temp.add(getUnknownCell(x+1, y));}
    }

    if (x != 0 & y != gridSize) {
      if (getUnknownCell(x-1, y+1) != null) {temp.add(getUnknownCell(x-1, y+1));}
    }

    if (y != gridSize) {
      if (getUnknownCell(x, y+1) != null) {temp.add(getUnknownCell(x, y+1));}
    }

    if (x != gridSize & y != gridSize) {
      if (getUnknownCell(x+1, y+1) != null) {temp.add(getUnknownCell(x+1, y+1));}
    }

    return temp;

  }

  public LinkedList<Cell> getUnkownOrFlaggedAdjacentNeighbours(Cell cell) {

    int x = cell.getX();
    int y = cell.getY();

    LinkedList<Cell> temp = new LinkedList<Cell>();

    if (x != 0 & y != 0) {
      if (getUnknownCell(x-1, y-1) != null) {
        temp.add(getUnknownCell(x-1, y-1));
      }
      if (getFlaggedCell(x-1, y-1) != null) {
        temp.add(getFlaggedCell(x-1, y-1));
      }
    }

    if (y != 0) {
      if (getUnknownCell(x, y-1) != null) {
        temp.add(getUnknownCell(x, y-1));
      }
      if (getFlaggedCell(x, y-1) != null) {
        temp.add(getFlaggedCell(x, y-1));
      }
    }

    if (x != gridSize & y != 0) {
      if (getUnknownCell(x+1, y-1) != null) {
        temp.add(getUnknownCell(x+1, y-1));
      }
      if (getFlaggedCell(x+1, y-1) != null) {
        temp.add(getFlaggedCell(x+1, y-1));
      }
    }

    if (x != 0) {
      if (getUnknownCell(x-1, y) != null) {
        temp.add(getUnknownCell(x-1, y));
      }
      if (getFlaggedCell(x-1, y)!= null) {
        temp.add(getFlaggedCell(x-1, y));
      }
    }

    if (x != gridSize) {
      if (getUnknownCell(x+1, y) != null) {
        temp.add(getUnknownCell(x+1, y));
      }
      if (getFlaggedCell(x+1, y) != null) {
        temp.add(getFlaggedCell(x+1, y));
      }
    }

    if (x != 0 & y != gridSize) {
      if (getUnknownCell(x-1, y+1) != null) {
        temp.add(getUnknownCell(x-1, y+1));
      }
      if (getFlaggedCell(x-1, y+1) != null) {
        temp.add(getFlaggedCell(x-1, y+1));
      }
    }

    if (y != gridSize) {
      if (getUnknownCell(x, y+1) != null) {
        temp.add(getUnknownCell(x, y+1));
      }
      if (getFlaggedCell(x, y+1) != null) {
        temp.add(getFlaggedCell(x, y+1));
      }
    }

    if (x != gridSize & y != gridSize) {
      if (getUnknownCell(x+1, y+1) != null) {
        temp.add(getUnknownCell(x+1, y+1));
      }
      if (getFlaggedCell(x, y-1) != null) {
        temp.add(getFlaggedCell(x, y-1));
      }
    }

    return temp;
  }

  /**
   * Get the list of all non-zero uncovered cells.
   * @return  List of all non-zero uncovered cells.
   */
  public LinkedList<Cell> getNonZeroUncoveredCells() {

    LinkedList<Cell> temp = new LinkedList<Cell>();

    for (Cell uncoveredCell : uncoveredCells) {
      if (Character.getNumericValue(uncoveredCell.getChar()) != 0) {
        temp.add(uncoveredCell);
      }
    }
    return temp;
  }

  // EDIT KNOWLEDGE BASE LISTS METHODS

  /**
   * Moves a cell from the start to the end of the unkownCells list.
   * Used when a given cell cannot be marked or uncovered yet so is added to the
   * back of the 'queue'.
   * @param cell Cell to move.
   */
  public void moveCellToEndOfQueue(Cell cell) {

    unknownCells.remove(cell);
    unknownCells.add(cell);
  }

  /**
   * Move the given cell from the unkownCells list to the uncovered cells list.
   * @param cell Cell to move
   */
  public void moveFromUnknownToUncovered(Cell cell) {

    uncoveredCells.add(cell);
    unknownCells.remove(cell);

  }

  /**
   * Move the given cell from the unkownCells list to the flagged cells list.
   * @param cell Cell to move
   */
  public void moveFromUnknownToFlagged(Cell cell) {

    cell.setChar('*');
    flaggedCells.add(cell);
    unknownCells.remove(cell);

  }

  // PRINTING METHODS

  /**
   * Print the KnowledgeBase (for debugging).
   */
  public void printKnowledgeBase() {

    System.out.println("\nunknownCells: "  + llToString(unknownCells));
    System.out.println("flaggedCells: "  + llToString(flaggedCells));
    System.out.println("uncoveredCells: "  + llToString(uncoveredCells));

  }

  /**
   * Print a given linked list (used when printing the KnowledgeBase for
   * debugging).
   * @param  ll List to print.
   * @return    List as a string.
   */
  public String llToString(LinkedList<Cell> ll) {
    StringBuilder str = new StringBuilder();
    for (Cell temp : ll) {
        str.append(temp.toString() + ", ");
     }
     if (str.length() > 2) {
       str.setLength(str.length() - 2);
     }
     return str.toString();
  }

  // Needed??

  public LinkedList<Cell> getUnknownCells() {
      return unknownCells;
    }

  public LinkedList<Cell> getFlaggedCells() {
    return flaggedCells;
  }

  public LinkedList<Cell> getUncoveredCells() {
    return uncoveredCells;
  }


}
