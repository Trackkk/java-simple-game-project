package puzzle.model;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import puzzle.State;
import java.util.*;

/**
 * Represents the state of the puzzle.
 */
public class PuzzleState implements State<Direction> {

    /**
     * The size of the board.
     */
    public static final int BOARD_SIZE = 7;

    private final ReadOnlyObjectWrapper<Position> ballPosition;
    private final ReadOnlyBooleanWrapper solved;

    private static final boolean[][] UP = new boolean[BOARD_SIZE][BOARD_SIZE];
    private static final boolean[][] RIGHT = new boolean[BOARD_SIZE][BOARD_SIZE];
    private static final boolean[][] DOWN = new boolean[BOARD_SIZE][BOARD_SIZE];
    private static final boolean[][] LEFT = new boolean[BOARD_SIZE][BOARD_SIZE];

    static {
        initializeWalls();
    }

    /**
     * Initializes the walls on the board.
     * The walls indicate the positions where movement is restricted.
     */
    private static void initializeWalls() {
        // It shows where we can't move through
        UP[1][2] = true;
        UP[1][6] = true;
        UP[3][1] = true;
        UP[4][3] = true;
        UP[4][6] = true;
        UP[5][0] = true;
        UP[5][4] = true;
        UP[6][2] = true;

        RIGHT[0][0] = true;
        RIGHT[0][3] = true;
        RIGHT[2][2] = true;
        RIGHT[2][5] = true;
        RIGHT[3][3] = true;
        RIGHT[3][4] = true;
        RIGHT[5][1] = true;
        RIGHT[5][2] = true;
        RIGHT[6][3] = true;
        RIGHT[6][5] = true;

        DOWN[0][2] = true;
        DOWN[0][6] = true;
        DOWN[2][1] = true;
        DOWN[3][3] = true;
        DOWN[3][6] = true;
        DOWN[4][0] = true;
        DOWN[4][4] = true;
        DOWN[5][2] = true;

        LEFT[0][1] = true;
        LEFT[0][4] = true;
        LEFT[2][3] = true;
        LEFT[2][6] = true;
        LEFT[3][4] = true;
        LEFT[3][5] = true;
        LEFT[5][2] = true;
        LEFT[5][3] = true;
        LEFT[6][4] = true;
        LEFT[6][6] = true;
    }

    /**
     * Creates a {@code PuzzleState} object that corresponds to the original
     * initial state of the puzzle.
     */
    public PuzzleState() {
        this(new Position(1, 4));
    }

    /**
     * Creates a {@code PuzzleState} object initializing the position of the
     * ball. The constructor expects the ball position or a {@code Position}
     * object.
     *
     * @param ballPosition the initial position of the ball.
     */
    public PuzzleState(Position ballPosition) {
        checkPositions(ballPosition);
        this.ballPosition = new ReadOnlyObjectWrapper<>(ballPosition);
        solved = new ReadOnlyBooleanWrapper();
        solved.bind(this.ballPosition.isEqualTo(getGoalPosition()));
    }

    /**
     * Represents the types of walls on the board.
     */
    public enum Wall {
        VERTICAL,
        HORIZONTAL
    }

    /**
     * Gets the wall positions in a set.
     *
     * @return the set of wall positions
     */
    public Set<WallPosition> getWalls() {
        Set<WallPosition> walls = new HashSet<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (UP[row][col]) walls.add(new WallPosition(row, col, Wall.HORIZONTAL));
                if (RIGHT[row][col]) walls.add(new WallPosition(row, col, Wall.VERTICAL));
            }
        }
        return walls;
    }

    /**
     * Gets the current position of the ball.
     *
     * @return the current position of the ball
     */
    public Position getBallPosition() {
        return ballPosition.get();
    }

    /**
     * Gets the goal position.
     *
     * @return the goal position
     */
    public Position getGoalPosition() {
        return new Position(5, 2);
    }

    /**
     * Checks if the puzzle is solved.
     *
     * @return true if the puzzle is solved, false otherwise
     */
    public boolean isSolved() {
        return solved.get();
    }

    /**
     * Gets the read-only property of the solved state.
     *
     * @return the read-only property of the solved state
     */
    public ReadOnlyBooleanProperty solvedProperty() {
        return solved.getReadOnlyProperty();
    }

    /**
     * {@return whether the move provided can be applied to the state}
     *
     * @param move represents the move to be made
     */
    @Override
    public boolean isLegalMove(Direction move) {
        return switch (move) {
            case UP -> canMoveUp();
            case RIGHT -> canMoveRight();
            case DOWN -> canMoveDown();
            case LEFT -> canMoveLeft();
        };
    }

    private void checkPositions(Position position) {
        if (!isOnBoard(position)) {
            throw new IllegalArgumentException("Invalid ball position.");
        }
    }

    /**
     * Applies the move provided to the state. This method should be called if
     * and only if {@link #isLegalMove(Direction)} returns {@code true}.
     *
     * @param move represents the move to be made
     */
    @Override
    public void makeMove(Direction move) {
        switch (move) {
            case UP -> moveUp();
            case RIGHT -> moveRight();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
        }
    }

    /**
     * Moves the ball in the specified direction until it cannot move further.
     *
     * @param direction the direction in which the ball should move
     */
    private void moveBall(Direction direction) {
        Position newPosition = getBallPosition().move(direction);
        while (isLegalMove(direction) && isOnBoard(newPosition)) {
            ballPosition.set(newPosition);
            newPosition = getBallPosition().move(direction);
        }
    }

    private void moveUp() {
        moveBall(Direction.UP);
    }

    private void moveRight() {
        moveBall(Direction.RIGHT);
    }

    private void moveDown() {
        moveBall(Direction.DOWN);
    }

    private void moveLeft() {
        moveBall(Direction.LEFT);
    }

    /**
     * Gets all legal moves available in the current state.
     *
     * @return a set containing all legal moves that can be applied to this state
     */
    @Override
    public Set<Direction> getLegalMoves() {
        var moves = EnumSet.noneOf(Direction.class);
        for (Direction direction : Direction.values()) {
            if (isLegalMove(direction)) {
                moves.add(direction);
            }
        }
        return moves;
    }

    private boolean isOnBoard(Position position) {
        return (position.row() >= 0 && position.row() < BOARD_SIZE) &&
                (position.col() >= 0 && position.col() < BOARD_SIZE);
    }

    private boolean canMoveUp() {
        return getBallPosition().row() > 0 && !UP[getBallPosition().row()][getBallPosition().col()];
    }

    private boolean canMoveRight() {
        return getBallPosition().col() < BOARD_SIZE - 1 && !RIGHT[getBallPosition().row()][getBallPosition().col()];
    }

    private boolean canMoveDown() {
        return getBallPosition().row() < BOARD_SIZE - 1 && !DOWN[getBallPosition().row()][getBallPosition().col()];
    }

    private boolean canMoveLeft() {
        return getBallPosition().col() > 0 && !LEFT[getBallPosition().row()][getBallPosition().col()];
    }

    public int hashCode() {
        return Objects.hash(ballPosition.get());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        PuzzleState state = (PuzzleState) o;
        return ballPosition.get().equals(state.ballPosition.get());
    }

    /**
     * Creates and returns a deep copy of the puzzle state.
     *
     * @return a new {@code PuzzleState} object representing an identical copy of the current state
     */
    @Override
    public PuzzleState clone() {
        return new PuzzleState(new Position(getBallPosition().row(),
                getBallPosition().col()));
    }

    public String toString() {
        var sj = new StringJoiner(",");
        sj.add(getBallPosition().toString());
        return sj.toString();
    }
}
