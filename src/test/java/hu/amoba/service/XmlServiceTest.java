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

class XmlServiceTest {

    private XmlService xmlService;
    private static final String TEST_XML_FILE = "test_game.xml";

    @BeforeEach
    void setUp() {
        xmlService = new XmlService();
    }

    @AfterEach
    void tearDown() {
        File xmlFile = new File(TEST_XML_FILE);
        if (xmlFile.exists()) {
            xmlFile.delete();
        }
    }

    @Test
    void testSaveAndLoadGame() {
        Board board = new Board(10, 10);
        board.placeSymbol(new Position(5, 5), Player.HUMAN);
        board.placeSymbol(new Position(5, 6), Player.COMPUTER);

        GameState originalState = new GameState(board, "TestPlayer");

        xmlService.saveGameToXml(originalState, TEST_XML_FILE);

        GameState loadedState = xmlService.loadGameFromXml(TEST_XML_FILE);

        assertNotNull(loadedState);
        assertEquals("TestPlayer", loadedState.getPlayerName());
        assertEquals(10, loadedState.getBoard().getRows());
        assertEquals(10, loadedState.getBoard().getCols());
        assertEquals(Player.HUMAN, loadedState.getBoard().getPlayerAt(new Position(5, 5)));
        assertEquals(Player.COMPUTER, loadedState.getBoard().getPlayerAt(new Position(5, 6)));
    }

    @Test
    void testLoadNonExistentFile() {
        GameState gameState = xmlService.loadGameFromXml("nonexistent.xml");
        assertNull(gameState);
    }

    @Test
    void testSaveCurrentPlayer() {
        Board board = new Board(10, 10);
        board.placeSymbol(new Position(5, 5), Player.HUMAN);

        GameState originalState = new GameState(board, "TestPlayer");
        originalState.switchPlayer();

        xmlService.saveGameToXml(originalState, TEST_XML_FILE);
        GameState loadedState = xmlService.loadGameFromXml(TEST_XML_FILE);

        assertNotNull(loadedState);
        assertEquals(Player.COMPUTER, loadedState.getCurrentPlayer());
    }
}