package puzzle.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    private Position position;

    private void assertPosition(int expectedRow, int expectedCol, Position position) {
        assertAll("position",
                () -> assertEquals(expectedRow, position.row()),
                () -> assertEquals(expectedCol, position.col()));
    }

    @BeforeEach
    void setUp() {
        position = new Position(1, 4);
    }

    @Test
    void move() {
        assertPosition(position.row() - 1, position.col(), position.move(Direction.UP));
        assertPosition(position.row(), position.col() + 1, position.move(Direction.RIGHT));
        assertPosition(position.row() + 1, position.col(), position.move(Direction.DOWN));
        assertPosition(position.row(), position.col() - 1, position.move(Direction.LEFT));
    }

    @Test
    void moveUp() {
        assertPosition(position.row() - 1, position.col(), position.moveUp());
    }

    @Test
    void moveRight() {
        assertPosition(position.row(), position.col() + 1, position.moveRight());
    }

    @Test
    void moveDown() {
        assertPosition(position.row() + 1, position.col(), position.moveDown());
    }

    @Test
    void moveLeft() {
        assertPosition(position.row(), position.col() - 1, position.moveLeft());
    }

    @Test
    void testToString() {
        assertEquals(String.format("(%d, %d)", position.row(), position.col()), position.toString());
    }
}