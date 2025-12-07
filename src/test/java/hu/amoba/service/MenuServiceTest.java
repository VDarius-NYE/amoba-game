package hu.amoba.service;

import hu.amoba.init.GameInit;
import hu.amoba.model.Board;
import hu.amoba.model.GameState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private Scanner scanner;

    @Mock
    private FileService fileService;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private XmlService xmlService;

    @Mock
    private GameInit gameInit;

    private MenuService menuService;

    private static final String TEST_SAVE_FILE = "test_game_state.txt";
    private static final String TEST_XML_FILE = "test_game_save.xml";

    @BeforeEach
    void setUp() {
        menuService = new MenuService(scanner, fileService, databaseService, xmlService, gameInit);
    }

    @AfterEach
    void tearDown() {
        File saveFile = new File(TEST_SAVE_FILE);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        File xmlFile = new File(TEST_XML_FILE);
        if (xmlFile.exists()) {
            xmlFile.delete();
        }
    }

    @Test
    void testShowMainMenuStartNewGame() {
        when(scanner.next()).thenReturn("4");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(gameInit.initializeGame()).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        assertNotNull(result);
        verify(gameInit).initializeGame();
    }

    @Test
    void testShowMainMenuShowHighScores() {
        when(scanner.next())
                .thenReturn("1")
                .thenReturn("back")
                .thenReturn("4");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(gameInit.initializeGame()).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        verify(databaseService).displayHighScores();
        assertNotNull(result);
    }

    @Test
    void testShowMainMenuLoadSaveGameSuccess() throws IOException {
        File testFile = new File(TEST_SAVE_FILE);
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("TestPlayer\n10\n10\nHUMAN\n");
        }

        when(fileService.getDefaultSaveFile()).thenReturn(TEST_SAVE_FILE);
        when(scanner.next()).thenReturn("2");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(fileService.loadGameFromFile(TEST_SAVE_FILE)).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        assertNotNull(result);
        verify(fileService).loadGameFromFile(TEST_SAVE_FILE);
    }

    @Test
    void testShowMainMenuLoadSaveGameNotFound() {
        when(fileService.getDefaultSaveFile()).thenReturn("nonexistent.txt");
        when(scanner.next())
                .thenReturn("2")
                .thenReturn("back")
                .thenReturn("4");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(gameInit.initializeGame()).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        assertNotNull(result);
        verify(fileService, never()).loadGameFromFile(anyString());
    }

    @Test
    void testShowMainMenuLoadSaveGameFailed() throws IOException {
        File testFile = new File(TEST_SAVE_FILE);
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("TestPlayer\n10\n10\nHUMAN\n");
        }

        when(fileService.getDefaultSaveFile()).thenReturn(TEST_SAVE_FILE);
        when(scanner.next())
                .thenReturn("2")
                .thenReturn("back")
                .thenReturn("4");
        when(fileService.loadGameFromFile(TEST_SAVE_FILE)).thenReturn(null);

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(gameInit.initializeGame()).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        assertNotNull(result);
        verify(fileService).loadGameFromFile(TEST_SAVE_FILE);
    }

    @Test
    void testShowMainMenuLoadXmlGameSuccess() throws IOException {
        File testFile = new File(TEST_XML_FILE);
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><GameState></GameState>");
        }

        when(xmlService.getDefaultXmlFile()).thenReturn(TEST_XML_FILE);
        when(scanner.next()).thenReturn("3");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(xmlService.loadGameFromXml(TEST_XML_FILE)).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        assertNotNull(result);
        verify(xmlService).loadGameFromXml(TEST_XML_FILE);
    }

    @Test
    void testShowMainMenuLoadXmlGameNotFound() {
        when(xmlService.getDefaultXmlFile()).thenReturn("nonexistent.xml");
        when(scanner.next())
                .thenReturn("3")
                .thenReturn("back")
                .thenReturn("4");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(gameInit.initializeGame()).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        assertNotNull(result);
        verify(xmlService, never()).loadGameFromXml(anyString());
    }

    @Test
    void testShowMainMenuInvalidOption() {
        when(scanner.next())
                .thenReturn("99")
                .thenReturn("4");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(gameInit.initializeGame()).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        assertNotNull(result);
    }

    @Test
    void testWaitForBackWithValidInput() {
        when(scanner.next())
                .thenReturn("1")
                .thenReturn("back")
                .thenReturn("4");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(gameInit.initializeGame()).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        verify(databaseService).displayHighScores();
        assertNotNull(result);
    }

    @Test
    void testWaitForBackWithInvalidInputs() {
        when(scanner.next())
                .thenReturn("1")
                .thenReturn("continue")
                .thenReturn("exit")
                .thenReturn("back")
                .thenReturn("4");

        Board board = new Board(10, 10);
        GameState expectedGameState = new GameState(board, "TestPlayer");
        when(gameInit.initializeGame()).thenReturn(expectedGameState);

        GameState result = menuService.showMainMenu();

        verify(databaseService).displayHighScores();
        assertNotNull(result);
    }

    // NOTE: testShowMainMenuExit() removed because it calls System.exit()
    // which terminates the JVM and causes the test suite to crash.
    // System.exit() cannot be reliably tested in unit tests without
    // using security managers or other complex workarounds.
}