package puzzle.solver;

import puzzle.model.Direction;
import puzzle.model.PuzzleState;

/**
 * The entry point for the puzzle solver application.
 * <p>
 * This class demonstrates solving a puzzle using a breadth-first search (BFS) algorithm.
 */
public class Main {

    /**
     * The main method that initiates the puzzle solving process.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        BreadthFirstSearch<Direction> bfs = new BreadthFirstSearch<>();
        bfs.solveAndPrintSolution(new PuzzleState());
    }
}
