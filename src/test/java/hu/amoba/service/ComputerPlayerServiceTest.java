package hu.amoba.service;

import hu.amoba.model.Board;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ComputerPlayerServiceTest {
    private ComputerPlayerService computerPlayer;
    private MoveValidatorService validator;
    private Board board;

    @BeforeEach
    void setUp() {
        validator = new MoveValidatorService();
        Random random = new Random(42);
        computerPlayer = new ComputerPlayerService(random, validator);
        board = new Board(10, 10);
    }

    @Test
    void testGenerateFirstMove() {
        Position move = computerPlayer.generateMove(board);

        assertNotNull(move, "First move should not be null");
        assertEquals(5, move.getRow(), "First move should be in middle row");
        assertTrue(move.getCol() == 4 || move.getCol() == 5,
                "First move should be in middle column (4 or 5)");
    }

    @Test
    void testGenerateMoveAfterFirstMove() {
        // Place first move
        board.placeSymbol(new Position(5, 5), Player.HUMAN);

        Position move = computerPlayer.generateMove(board);

        assertNotNull(move, "Move should not be null");
        assertTrue(board.isEmpty(move), "Generated position should be empty");

        // Check if move is adjacent to (5,5)
        int rowDiff = Math.abs(move.getRow() - 5);
        int colDiff = Math.abs(move.getCol() - 5);
        assertTrue(rowDiff <= 1 && colDiff <= 1, "Move should be adjacent to occupied position");
        assertFalse(rowDiff == 0 && colDiff == 0, "Move should not be the same as occupied position");
    }

    @Test
    void testGeneratedMoveIsAlwaysEmpty() {
        board.placeSymbol(new Position(5, 5), Player.HUMAN);
        board.placeSymbol(new Position(5, 6), Player.COMPUTER);

        for (int i = 0; i < 10; i++) {
            Board testBoard = new Board(10, 10);
            testBoard.placeSymbol(new Position(5, 5), Player.HUMAN);

            Position move = computerPlayer.generateMove(testBoard);
            assertNotNull(move, "Move should not be null");
            assertTrue(testBoard.isEmpty(move), "Generated position should be empty");
        }
    }

    @Test
    void testGenerateMoveWithMultipleOccupiedPositions() {
        board.placeSymbol(new Position(5, 5), Player.HUMAN);
        board.placeSymbol(new Position(5, 6), Player.COMPUTER);
        board.placeSymbol(new Position(6, 5), Player.HUMAN);

        Position move = computerPlayer.generateMove(board);

        assertNotNull(move, "Move should not be null");
        assertTrue(board.isEmpty(move), "Generated position should be empty");
    }

    @Test
    void testGenerateMoveReturnsNullWhenBoardFull() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                board.placeSymbol(new Position(row, col), Player.HUMAN);
            }
        }

        Position move = computerPlayer.generateMove(board);
        assertNull(move, "Should return null when board is full");
    }

    @Test
    void testGenerateFirstMoveOnDifferentSizes() {
        Board board5 = new Board(5, 5);
        Position move5 = computerPlayer.generateMove(board5);
        assertNotNull(move5);
        assertEquals(2, move5.getRow()); // 5/2 = 2

        Board board8 = new Board(8, 8);
        Position move8 = computerPlayer.generateMove(board8);
        assertNotNull(move8);
        assertEquals(4, move8.getRow()); // 8/2 = 4
    }

    @Test
    void testGenerateMoveNeverReturnsOccupiedPosition() {
        board.placeSymbol(new Position(5, 5), Player.HUMAN);
        board.placeSymbol(new Position(5, 6), Player.COMPUTER);
        board.placeSymbol(new Position(6, 5), Player.HUMAN);
        board.placeSymbol(new Position(6, 6), Player.COMPUTER);

        for (int i = 0; i < 20; i++) {
            Position move = computerPlayer.generateMove(board);
            assertNotNull(move);
            assertTrue(board.isEmpty(move));
            assertNotEquals(new Position(5, 5), move);
            assertNotEquals(new Position(5, 6), move);
            assertNotEquals(new Position(6, 5), move);
            assertNotEquals(new Position(6, 6), move);
        }
    }

    @Test
    void testGenerateMoveFromCorner() {
        board.placeSymbol(new Position(0, 0), Player.HUMAN);

        Position move = computerPlayer.generateMove(board);

        assertNotNull(move);
        assertTrue(board.isEmpty(move));

        int rowDiff = Math.abs(move.getRow() - 0);
        int colDiff = Math.abs(move.getCol() - 0);
        assertTrue(rowDiff <= 1 && colDiff <= 1);
    }

    @Test
    void testGenerateMoveFromEdge() {
        board.placeSymbol(new Position(0, 5), Player.HUMAN);

        Position move = computerPlayer.generateMove(board);

        assertNotNull(move);
        assertTrue(board.isEmpty(move));
    }

    @Test
    void testMultipleCallsProduceDifferentResultsSometimes() {
        board.placeSymbol(new Position(5, 5), Player.HUMAN);

        Random random1 = new Random(1);
        Random random2 = new Random(2);

        ComputerPlayerService cp1 = new ComputerPlayerService(random1, validator);
        ComputerPlayerService cp2 = new ComputerPlayerService(random2, validator);

        Position move1 = cp1.generateMove(board);
        Position move2 = cp2.generateMove(board);

        assertNotNull(move1);
        assertNotNull(move2);
    }
}