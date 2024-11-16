package puzzle.model;

import java.util.Objects;

/**
 * Represents the position of a wall on the puzzle board.
 * Each wall has a specific row and column position, and a type
 * indicating whether it is a vertical or horizontal wall.
 */
public class WallPosition {
    private final int row;
    private final int col;
    private final PuzzleState.Wall wall;

    /**
     * Constructs a {@code WallPosition} object with the specified row, column, and wall type.
     *
     * @param row  the row position of the wall on the board.
     * @param col  the column position of the wall on the board.
     * @param wall the type of wall (vertical or horizontal).
     */
    public WallPosition(int row, int col, PuzzleState.Wall wall) {
        this.row = row;
        this.col = col;
        this.wall = wall;
    }

    /**
     * Gets the row position of the wall.
     *
     * @return the row position of the wall
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column position of the wall.
     *
     * @return the column position of the wall
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the type of the wall (vertical or horizontal).
     *
     * @return the type of the wall
     */
    public PuzzleState.Wall getWall() {
        return wall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WallPosition that = (WallPosition) o;
        return row == that.row &&
                col == that.col &&
                wall == that.wall;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, wall);
    }
}
