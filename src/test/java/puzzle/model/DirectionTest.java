package puzzle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void of() {
        assertSame(Direction.UP, Direction.of(-1, 0));
        assertSame(Direction.RIGHT, Direction.of(0, 1));
        assertSame(Direction.DOWN, Direction.of(1, 0));
        assertSame(Direction.LEFT, Direction.of(0, -1));
    }

    @Test
    void of_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Direction.of(0, 0));
        assertThrows(IllegalArgumentException.class, () -> Direction.of(1, 1));
        assertThrows(IllegalArgumentException.class, () -> Direction.of(-1, -1));
    }
}