package hu.amoba.service;

import hu.amoba.model.Board;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
class MoveValidatorServiceTest {

    private MoveValidatorService validator;
    private Board board;

    @BeforeEach
    void setUp() {
        validator = new MoveValidatorService();
        board = new Board(10, 10);
    }

    @Test
    void testFirstMoveMustBeInMiddle() {
        assertTrue(validator.isValidMove(board, new Position(5, 5)));
        assertTrue(validator.isValidMove(board, new Position(5, 4)));
        assertFalse(validator.isValidMove(board, new Position(0, 0)));
    }

    @Test
    void testInvalidPosition() {
        assertFalse(validator.isValidMove(board, new Position(-1, 5)));
        assertFalse(validator.isValidMove(board, new Position(10, 5)));
    }

    @Test
    void testOccupiedPosition() {
        Position pos = new Position(5, 5);
        board.placeSymbol(pos, Player.HUMAN);
        assertFalse(validator.isValidMove(board, pos));
    }

    @Test
    void testAdjacentMove() {
        board.placeSymbol(new Position(5, 5), Player.HUMAN);

        assertTrue(validator.isValidMove(board, new Position(5, 6)));
        assertTrue(validator.isValidMove(board, new Position(6, 5)));
        assertTrue(validator.isValidMove(board, new Position(6, 6)));
        assertFalse(validator.isValidMove(board, new Position(0, 0)));
    }
}