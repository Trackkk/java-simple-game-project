package puzzle.model;

/**
 * Represents a 2D position.
 *
 * @param row the row coordinate of the position
 * @param col the column coordinate of the position
 */
public record Position(int row, int col) {

    /**
     * Moves the position in the specified direction.
     *
     * @param direction the direction specifying the change in coordinates
     * @return the new position after moving in the specified direction
     */
    public Position move(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Convenience method that is equivalent to {@code move(Direction.UP)}.
     *
     * @return the position above this position
     */
    public Position moveUp() {
        return move(Direction.UP);
    }

    /**
     * Convenience method that is equivalent to {@code move(Direction.RIGHT)}.
     *
     * @return the position to the right of this position
     */
    public Position moveRight() {
        return move(Direction.RIGHT);
    }

    /**
     * Convenience method that is equivalent to {@code move(Direction.DOWN)}.
     *
     * @return the position below this position
     */
    public Position moveDown() {
        return move(Direction.DOWN);
    }

    /**
     * Convenience method that is equivalent to {@code move(Direction.LEFT)}.
     *
     * @return the position to the left of this position
     */
    public Position moveLeft() {
        return move(Direction.LEFT);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }
}
