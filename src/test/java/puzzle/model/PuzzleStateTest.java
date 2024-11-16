package puzzle.model;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleStateTest {
    PuzzleState state1 = new PuzzleState(); // the original initial state

    PuzzleState state2 = new PuzzleState(new Position(5, 2)); // a goal state

    PuzzleState state3 = new PuzzleState(new Position(3, 3)); // a non-goal state

    PuzzleState state4 = new PuzzleState(new Position(6, 6)); // one way state

    @Test
    void getWalls() {
        PuzzleState state = new PuzzleState();
        var walls = state.getWalls();

        assertTrue(walls.contains(new WallPosition(1, 2, PuzzleState.Wall.HORIZONTAL)));
        assertTrue(walls.contains(new WallPosition(1, 6, PuzzleState.Wall.HORIZONTAL)));
        assertTrue(walls.contains(new WallPosition(0, 0, PuzzleState.Wall.VERTICAL)));
        assertTrue(walls.contains(new WallPosition(0, 3, PuzzleState.Wall.VERTICAL)));
    }

    @Test
    void isSolved() {
        assertFalse(state1.isSolved());
        assertTrue(state2.isSolved());
        assertFalse(state3.isSolved());
        assertFalse(state4.isSolved());
    }

    @Test
    void isLegalMove_state1() {
        assertTrue(state1.isLegalMove(Direction.UP));
        assertTrue(state1.isLegalMove(Direction.RIGHT));
        assertTrue(state1.isLegalMove(Direction.DOWN));
        assertTrue(state1.isLegalMove(Direction.LEFT));
    }

    @Test
    void isLegalMove_state2() {
        assertTrue(state2.isLegalMove(Direction.UP));
        assertFalse(state2.isLegalMove(Direction.RIGHT));
        assertFalse(state2.isLegalMove(Direction.DOWN));
        assertFalse(state2.isLegalMove(Direction.LEFT));
    }

    @Test
    void isLegalMove_state3() {
        assertTrue(state3.isLegalMove(Direction.UP));
        assertFalse(state3.isLegalMove(Direction.RIGHT));
        assertFalse(state3.isLegalMove(Direction.DOWN));
        assertTrue(state3.isLegalMove(Direction.LEFT));
    }

    @Test
    void isLegalMove_state4() {
        assertTrue(state4.isLegalMove(Direction.UP));
        assertFalse(state4.isLegalMove(Direction.RIGHT));
        assertFalse(state4.isLegalMove(Direction.DOWN));
        assertFalse(state4.isLegalMove(Direction.LEFT));
    }

    @Test
    void makeMove_up_state1() {
        var stateBeforeMove = state1.clone();
        state1.makeMove(Direction.UP);
        assertEquals(stateBeforeMove.getBallPosition().moveUp(), state1.getBallPosition());
    }

    @Test
    void makeMove_right_state1() {
        var stateBeforeMove = state1.clone();
        state1.makeMove(Direction.RIGHT);
        assertEquals(stateBeforeMove.getBallPosition().moveRight().moveRight(), state1.getBallPosition());
    }

    @Test
    void makeMove_down_state1() {
        var stateBeforeMove = state1.clone();
        state1.makeMove(Direction.DOWN);
        assertEquals(stateBeforeMove.getBallPosition().moveDown().moveDown().moveDown(), state1.getBallPosition());
    }

    @Test
    void makeMove_left_state1() {
        var stateBeforeMove = state1.clone();
        state1.makeMove(Direction.LEFT);
        assertEquals(stateBeforeMove.getBallPosition().moveLeft().moveLeft().moveLeft().moveLeft(), state1.getBallPosition());
    }

    @Test
    void makeMove_up_state2() {
        var stateBeforeMove = state2.clone();
        state2.makeMove(Direction.UP);
        assertEquals(stateBeforeMove.getBallPosition().moveUp().moveUp().moveUp().moveUp(), state2.getBallPosition());
    }

    @Test
    void makeMove_up_state3() {
        var stateBeforeMove = state3.clone();
        state3.makeMove(Direction.UP);
        assertEquals(stateBeforeMove.getBallPosition().moveUp().moveUp().moveUp(), state3.getBallPosition());
    }

    @Test
    void makeMove_left_state3() {
        var stateBeforeMove = state3.clone();
        state3.makeMove(Direction.LEFT);
        assertEquals(stateBeforeMove.getBallPosition().moveLeft().moveLeft().moveLeft(), state3.getBallPosition());
    }

    @Test
    void makeMove_up_state4() {
        var stateBeforeMove = state4.clone();
        state4.makeMove(Direction.UP);
        assertEquals(stateBeforeMove.getBallPosition().moveUp().moveUp(), state4.getBallPosition());
    }

    @Test
    void getLegalMoves() {
        assertEquals(EnumSet.allOf(Direction.class), state1.getLegalMoves());
        assertEquals(EnumSet.of(Direction.UP), state2.getLegalMoves());
        assertEquals(EnumSet.of(Direction.UP, Direction.LEFT), state3.getLegalMoves());
        assertEquals(EnumSet.of(Direction.UP), state4.getLegalMoves());
    }

    @Test
    void testHashCode() {
        assertTrue(state1.hashCode() == state1.hashCode());
        assertTrue(state1.hashCode() == state1.clone().hashCode());
    }

    @Test
    void testEquals() {
        assertTrue(state1.equals(state1));

        var clone = state1.clone();
        clone.makeMove(Direction.RIGHT);
        assertFalse(clone.equals(state1));

        assertFalse(state1.equals(null));
        assertFalse(state1.equals("Hello, World!"));
        assertFalse(state1.equals(state2));
    }

    @Test
    void testClone() {
        var clone = state1.clone();
        assertTrue(clone.equals(state1));
        assertNotSame(clone, state1);
    }

    @Test
    void testToString() {
        assertEquals("(1, 4)", state1.toString());
        assertEquals("(5, 2)", state2.toString());
        assertEquals("(3, 3)", state3.toString());
        assertEquals("(6, 6)", state4.toString());
    }
}