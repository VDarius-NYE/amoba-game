package hu.amoba.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(10,10);
    }

    @Test
    void testBoardInitialization() {
        assertEquals(10, board.getRows());
        assertEquals(10, board.getCols());
        assertTrue(board.isFirstMove());
    }

    @Test
    void TestValidPosition() {
        assertTrue(board.isValidPosition(new Position(0,0)));
        assertTrue(board.isValidPosition(new Position(9,9)));
        assertTrue(board.isValidPosition(new Position(1,0)));
        assertTrue(board.isValidPosition(new Position(0,1)));
        assertTrue(board.isValidPosition(new Position(9,3)));
        assertTrue(board.isValidPosition(new Position(0,9)));
    }

    @Test
    void testPlaceSymbol() {
        Position pos = new Position(5, 5);
        board.placeSymbol(pos, Player.HUMAN);
        assertEquals(Player.HUMAN, board.getPlayerAt(pos));
        assertFalse(board.isFirstMove());
    }

    @Test
    void testIsInMiddle() {
        assertTrue(board.isInMiddle(new Position(5,5)));
        assertTrue(board.isInMiddle(new Position(5,4)));
        assertFalse(board.isInMiddle(new Position(0,0)));
        assertFalse(board.isInMiddle(new Position(4,5)));
    }

    @Test
    void testCheckWinHorizontal() {
        board.placeSymbol(new Position(5,5), Player.HUMAN);
        board.placeSymbol(new Position(5,6), Player.HUMAN);
        board.placeSymbol(new Position(5,7), Player.HUMAN);
        Position lastMove = new Position(5,8);
        board.placeSymbol(lastMove, Player.HUMAN);

        assertTrue(board.checkWin(lastMove, Player.HUMAN));
    }

    @Test
    void testCheckWinDiagonal() {
        board.placeSymbol(new Position(5,5), Player.HUMAN);
        board.placeSymbol(new Position(6,6), Player.HUMAN);
        board.placeSymbol(new Position(7,7), Player.HUMAN);
        Position lastMove = new Position(8,8);
        board.placeSymbol(lastMove, Player.HUMAN);

        assertTrue(board.checkWin(lastMove, Player.HUMAN));
    }

    @Test
    void testNoWin() {
        board.placeSymbol(new Position(5,5), Player.HUMAN);
        board.placeSymbol(new Position(5,6), Player.HUMAN);
        board.placeSymbol(new Position(5,7), Player.HUMAN);
        Position lastMove = new Position(6,8);
        board.placeSymbol(lastMove, Player.HUMAN);

        assertFalse(board.checkWin(lastMove, Player.HUMAN));
    }
}
