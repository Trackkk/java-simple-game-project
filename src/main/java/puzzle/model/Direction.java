package puzzle.model;

/**
 * Represents the four main directions.
 */
public enum Direction {
    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1);

    private final int rowChange;
    private final int colChange;

    /**
     * Constructs a Direction with the specified row and column changes.
     *
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     */
    Direction(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * Gets the change in the row coordinate when moving in this direction.
     *
     * @return the change in the row coordinate
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * Gets the change in the column coordinate when moving in this direction.
     *
     * @return the change in the column coordinate
     */
    public int getColChange() {
        return colChange;
    }

    /**
     * Returns the direction that corresponds to the specified row and column changes.
     *
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     * @return the direction corresponding to the specified changes
     * @throws IllegalArgumentException if no direction corresponds to the changes
     */
    public static Direction of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.getRowChange() == rowChange && direction.getColChange() == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException("No direction with row change " + rowChange + " and column change " + colChange);
    }
}
