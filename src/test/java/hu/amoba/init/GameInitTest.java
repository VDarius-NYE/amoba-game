package hu.amoba.init;

import hu.amoba.model.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameInitTest {

    @Mock
    private Scanner scanner;

    private GameInit gameInit;

    @BeforeEach
    void setUp() {
        gameInit = new GameInit(scanner);
    }

    @Test
    void testInitializeGameWithValidInput() {
        when(scanner.next()).thenReturn("TestPlayer");
        when(scanner.nextInt()).thenReturn(10, 10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals("TestPlayer", gameState.getPlayerName());
        assertEquals(10, gameState.getBoard().getRows());
        assertEquals(10, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithMinimumSize() {
        when(scanner.next()).thenReturn("Player1");
        when(scanner.nextInt()).thenReturn(5, 5);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals("Player1", gameState.getPlayerName());
        assertEquals(5, gameState.getBoard().getRows());
        assertEquals(5, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithMaximumSize() {
        when(scanner.next()).thenReturn("Player2");
        when(scanner.nextInt()).thenReturn(25, 25);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals("Player2", gameState.getPlayerName());
        assertEquals(25, gameState.getBoard().getRows());
        assertEquals(25, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithDifferentRowsAndCols() {
        when(scanner.next()).thenReturn("Player3");
        when(scanner.nextInt()).thenReturn(15, 20);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals("Player3", gameState.getPlayerName());
        assertEquals(15, gameState.getBoard().getRows());
        assertEquals(20, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithInvalidRowsThenValid() {
        when(scanner.next()).thenReturn("Player4");
        when(scanner.nextInt())
                .thenReturn(3)
                .thenReturn(10)
                .thenReturn(10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals("Player4", gameState.getPlayerName());
        assertEquals(10, gameState.getBoard().getRows());
        assertEquals(10, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithInvalidRowsTooLarge() {
        when(scanner.next()).thenReturn("Player5");
        when(scanner.nextInt())
                .thenReturn(30)
                .thenReturn(10)
                .thenReturn(10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals(10, gameState.getBoard().getRows());
    }

    @Test
    void testInitializeGameWithInvalidColsThenValid() {
        when(scanner.next()).thenReturn("Player6");
        when(scanner.nextInt())
                .thenReturn(10)
                .thenReturn(2)
                .thenReturn(10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals("Player6", gameState.getPlayerName());
        assertEquals(10, gameState.getBoard().getRows());
        assertEquals(10, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithInvalidColsTooLarge() {
        when(scanner.next()).thenReturn("Player7");
        when(scanner.nextInt())
                .thenReturn(10)
                .thenReturn(50)
                .thenReturn(10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals(10, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithMultipleInvalidInputs() {
        when(scanner.next()).thenReturn("Player8");
        when(scanner.nextInt())
                .thenReturn(0)
                .thenReturn(-5)
                .thenReturn(100)
                .thenReturn(10)
                .thenReturn(1)
                .thenReturn(30)
                .thenReturn(10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals("Player8", gameState.getPlayerName());
        assertEquals(10, gameState.getBoard().getRows());
        assertEquals(10, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithBoundaryValues() {
        when(scanner.next()).thenReturn("Player9");
        when(scanner.nextInt())
                .thenReturn(4)
                .thenReturn(5)
                .thenReturn(26)
                .thenReturn(25);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals("Player9", gameState.getPlayerName());
        assertEquals(5, gameState.getBoard().getRows());
        assertEquals(25, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameCreatesEmptyBoard() {
        when(scanner.next()).thenReturn("Player10");
        when(scanner.nextInt()).thenReturn(8, 8);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertTrue(gameState.getBoard().isFirstMove());
    }

    @Test
    void testInitializeGameWithLongPlayerName() {
        String longName = "VeryLongPlayerNameWithManyCharacters";
        when(scanner.next()).thenReturn(longName);
        when(scanner.nextInt()).thenReturn(10, 10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals(longName, gameState.getPlayerName());
    }

    @Test
    void testInitializeGameScannerIsCalledCorrectly() {
        when(scanner.next()).thenReturn("TestPlayer");
        when(scanner.nextInt()).thenReturn(10, 10);

        gameInit.initializeGame();

        verify(scanner, times(1)).next();
        verify(scanner, times(2)).nextInt();
    }

    @Test
    void testInitializeGameWithNegativeNumbers() {
        when(scanner.next()).thenReturn("Player11");
        when(scanner.nextInt())
                .thenReturn(-10)
                .thenReturn(10)
                .thenReturn(-5)
                .thenReturn(10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals(10, gameState.getBoard().getRows());
        assertEquals(10, gameState.getBoard().getCols());
    }

    @Test
    void testInitializeGameWithZeroSize() {
        when(scanner.next()).thenReturn("Player12");
        when(scanner.nextInt())
                .thenReturn(0)
                .thenReturn(10)
                .thenReturn(0)
                .thenReturn(10);

        GameState gameState = gameInit.initializeGame();

        assertNotNull(gameState);
        assertEquals(10, gameState.getBoard().getRows());
        assertEquals(10, gameState.getBoard().getCols());
    }
}