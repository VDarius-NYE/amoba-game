package hu.amoba.service;

import hu.amoba.model.Board;
import hu.amoba.model.GameState;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileServiceTest {

    private FileService fileService;
    private static final String TEST_FILE = "test_save.txt";

    @BeforeEach
    void setUp() {
        fileService = new FileService();
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveAndLoadGame() {
        Board board = new Board(10, 10);
        board.placeSymbol(new Position(5, 5), Player.HUMAN);
        board.placeSymbol(new Position(5, 6), Player.COMPUTER);

        GameState originalState = new GameState(board, "TestPlayer");

        fileService.saveGameToFile(originalState, TEST_FILE);

        GameState loadedState = fileService.loadGameFromFile(TEST_FILE);

        assertNotNull(loadedState);
        assertEquals("TestPlayer", loadedState.getPlayerName());
        assertEquals(10, loadedState.getBoard().getRows());
        assertEquals(10, loadedState.getBoard().getCols());
        assertEquals(Player.HUMAN, loadedState.getBoard().getPlayerAt(new Position(5, 5)));
        assertEquals(Player.COMPUTER, loadedState.getBoard().getPlayerAt(new Position(5, 6)));
    }

    @Test
    void testLoadNonExistentFile() {
        GameState gameState = fileService.loadGameFromFile("nonexistent.txt");
        assertNull(gameState);
    }

    @Test
    void testSaveCurrentPlayer() {
        Board board = new Board(10, 10);
        board.placeSymbol(new Position(5, 5), Player.HUMAN);

        GameState originalState = new GameState(board, "TestPlayer");
        originalState.switchPlayer();

        fileService.saveGameToFile(originalState, TEST_FILE);
        GameState loadedState = fileService.loadGameFromFile(TEST_FILE);

        assertNotNull(loadedState);
        assertEquals(Player.COMPUTER, loadedState.getCurrentPlayer());
    }

    @Test
    void testGetDefaultSaveFile() {
        String defaultFile = fileService.getDefaultSaveFile();
        assertEquals("game_state.txt", defaultFile);
    }
}