package hu.amoba.service;

import hu.amoba.model.Board;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComputerPlayerServiceTest {
    private ComputerPlayerService computerPlayer;
    private MoveValidatorService validator;
    private Board board;

    @BeforeEach
    void setUp() {
        validator = new MoveValidatorService();
        Random random = new Random();
        computerPlayer = new ComputerPlayerService(random, validator);
        board = new Board(10, 10);
    }

    @Test
    void testGenerateFirstMove() {
        Position move = computerPlayer.generateMove(board);
        assertNotNull(move);
        assertTrue(board.isInMiddle(move));
    }

    @Test
    void testGenerateMoveAfterFirstMove() {
        board.placeSymbol(new Position(5, 5), Player.HUMAN);

        Position move = computerPlayer.generateMove(board);
        assertNotNull(move);
        assertTrue(validator.isValidMove(board, move));
    }

    @Test
    void testGeneratedMoveIsValid() {
        board.placeSymbol(new Position(5, 5), Player.HUMAN);
        board.placeSymbol(new Position(5, 6), Player.COMPUTER);

        Position move = computerPlayer.generateMove(board);
        assertNotNull(move);
        assertTrue(validator.isValidMove(board, move));
        assertTrue(board.isEmpty(move));
    }
}
