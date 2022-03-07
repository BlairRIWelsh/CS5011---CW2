import org.logicng.formulas.Variable;
import org.logicng.formulas.Literal;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Formula;
import org.logicng.datastructures.Assignment;
import org.logicng.datastructures.Tristate;
import org.logicng.io.parsers.*;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

public class SATSSolver {

  public static void main(String[] args) {
    final FormulaFactory f=new FormulaFactory(); // creates formulas


//    final PropositionalParser p=new PropositionalParser(f);
    try {

      final PropositionalParser p=new PropositionalParser(f);

//      String kbu = "(3 & ~6 & ~9) | (~3 & 6 & ~9) | (~3 & ~6 & 9) & (3 & ~6) | (~3 & 6)";
      String kbu = "3 & (3 & ~9) | (~3 & 9)";
      String q = kbu + " & " + "9";

//      String kbu = "(C & !F & !I) | (!C & F & !I) | (!C & !F & I) & (C & !F) | (!C & F)";
//      String q = kbu + " & " + "C";



      final Formula formula=p.parse(q);
      final SATSolver miniSat=MiniSat.miniSat(f);
      miniSat.add(formula);

      Assignment assignment = new Assignment();
      assignment.addLiteral(f.literal("9", true));

      boolean result = formula.evaluate(assignment);

//      final Tristate result=miniSat.sat();

      System.out.println(q);
      System.out.println(formula);
      System.out.println(result);


    } catch (ParserException e) {
      e.printStackTrace();
    }





  }



  //
  // public SATSolver() {
  //
  //   //probe 0,0
  //
  //   // while
  //
  //     // KBU = getKBUOfUnknownsAdjacentToClues
  //
  //
  //
  //
  // }
  //
//   public String getKBUOfUnknownsAdjacentToClues() {
//
//     StringBuilder kbu = new StringBuilder();
//
//     // For every uncovered cell next to an unknown cell
//     for (Cell uncoveredCell : getCellsWithAdjacentUnknowns()) {
//
//       kbu.append("[")
//
//       // Get the uncovered cells value
//       int uncoveredCellValue = Character.getNumericValue(uncoveredCell.getChar());
//
//       // Get the list of the covered neighbour cells
//       LinkedList<Cell> coveredNeighbours = getAdjacentUnkownCells(uncoveredCell)
//
//       StringBuilder kbuForUnCell = new StringBuilder();
//       kbuForUnCell.append("[");
//       /**
//        * For size = 2, we have:
//        *    [
//        * For size = 3, we have:
//        *    [
//        */
//
//       for (int i = 0; i < coveredNeighbours.size()) {
//
//         kbuForUnCell.append("(");
//         /**
//          * For size = 2, we have:
//          *    [(
//          * For size = 3, we have:
//          *    [(
//          */
//
//         for (Cell coveredCell : coveredNeighbours) {
//
//           kbuForUnCell.append("D" + coveredCell.toString() + " &");
//           /**
//            * For size = 2, here we add
//            *      D(2,0) &
//            *      D(2,1) &
//            * For size = 3, here we add
//            *      D(2,0) &
//            *      D(2,1) &
//            *      D(2,2) &
//            */
//
//         }
//         /**
//          * For size = 2, we have:
//          *    [(D(2,0) & D(2,1) &
//          * For size = 3, we have:
//          *    [(D(2,0) & D(2,1) & D(2,2) &
//          */
//
//         // Remove trailing " &"
//         kbuForUnCell.setLength(kbuForUnCell.length() - 2);
//         kbuForUnCell.append(") | ");
//         /**
//          * For size = 2, we have:
//          *    [(D(2,0) & D(2,1)) |
//          * For size = 3, we have:
//          *    [(D(2,0) & D(2,1) & D(2,2)) |
//          */
//
//       }
//       /**
//        * For size = 2, we have:
//        *    [(D(2,0) & D(2,1)) | (D(2,0) & D(2,1)) |
//        * For size = 3, we have:
//        *    [(D(2,0) & D(2,1) & D(2,2)) | (D(2,0) & D(2,1) & D(2,2)) | (D(2,0) & D(2,1) & D(2,2))
//        */
//
//       // Remove trailing " |"
//       kbuForUnCell.setLength(kbuForUnCell.length() - 2);
//       kbu.append("] & ")
//       /**
//        * For size = 2, we have:
//        *    [(D(2,0) & D(2,1)) | (D(2,0) & D(2,1))] &
//        * For size = 3, we have:
//        *    [(D(2,0) & D(2,1) & D(2,2)) | (D(2,0) & D(2,1) & D(2,2))] & | (D(2,0) & D(2,1) & D(2,2))] &
//        */
//
//     }
//
//     kbu.setLength(kbu.length() - 2);
//     /**
//      * For size = 2, we have:
//      *    [(D(2,0) & D(2,1)) | (D(2,0) & D(2,1))]
//      * For size = 3, we have:
//      *    [(D(2,0) & D(2,1) & D(2,2)) | (D(2,0) & D(2,1) & D(2,2)) | (D(2,0) & D(2,1) & D(2,2))]
//      */
//
//     if (coveredNeighbours.size() >= 2) {
//       kbu.insert(2, '!');
//       kbu.insert(32, '!');
//     }
//     if (coveredNeighbours.size() >= 3) {
//       kbu.insert(2, '!');
//       str.insert(41, '!');
//       str.insert(80, '!');
//     }
//
//   }
  //
  // /**
  //  * Get all the uncovered cells that are adjacent to unkown cells
  //  * @return [description]
  //  */
  // public LinkedList<Cell> getCellsWithAdjacentUnknowns() {
  //
  // }
  //
  // public LinkedList<Cell> getAdjacentUnkownCells(Cell cell) {
  //
  // }

}
