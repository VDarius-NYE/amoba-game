package hu.amoba.service;

import hu.amoba.display.BoardDisplayer;
import hu.amoba.model.Board;
import hu.amoba.model.GameState;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private BoardDisplayer boardDisplayer;

    @Mock
    private MoveValidatorService moveValidator;

    @Mock
    private ComputerPlayerService computerPlayer;

    @Mock
    private FileService fileService;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private XmlService xmlService;

    @Mock
    private Scanner scanner;

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService(
                boardDisplayer,
                moveValidator,
                computerPlayer,
                fileService,
                databaseService,
                xmlService,
                scanner
        );
    }

    @Test
    void testStartGameWithGameAlreadyOver() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        gameState.setGameOver(true);
        gameState.setWinner(Player.HUMAN);

        gameService.startGame(gameState);

        verify(databaseService).recordWin("TestPlayer");
        verify(databaseService).displayHighScores();
        verify(boardDisplayer, atLeastOnce()).displayBoard(any(Board.class));
    }

    @Test
    void testStartGameWithComputerWin() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        gameState.setGameOver(true);
        gameState.setWinner(Player.COMPUTER);

        gameService.startGame(gameState);

        verify(databaseService, never()).recordWin("TestPlayer");
        verify(databaseService).displayHighScores();
    }

    @Test
    void testStartGameWithExit() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        when(scanner.next()).thenReturn("exit");

        gameService.startGame(gameState);

        assertTrue(gameState.isGameOver());
        verify(databaseService).displayHighScores();
    }

    @Test
    void testStartGameWithSave() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        when(scanner.next())
                .thenReturn("save")
                .thenReturn("exit");
        when(fileService.getDefaultSaveFile()).thenReturn("game_state.txt");

        gameService.startGame(gameState);

        verify(fileService).saveGameToFile(eq(gameState), eq("game_state.txt"));
        assertTrue(gameState.isGameOver());
    }

    @Test
    void testStartGameWithXmlSave() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        when(scanner.next())
                .thenReturn("savexml")
                .thenReturn("exit");
        when(xmlService.getDefaultXmlFile()).thenReturn("game_save.xml");

        gameService.startGame(gameState);

        verify(xmlService).saveGameToXml(eq(gameState), eq("game_save.xml"));
        assertTrue(gameState.isGameOver());
    }

    @Test
    void testStartGameWithInvalidInputShortString() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        when(scanner.next())
                .thenReturn("a")
                .thenReturn("exit");

        gameService.startGame(gameState);

        verify(boardDisplayer, atLeastOnce()).displayBoard(any(Board.class));
        assertTrue(gameState.isGameOver());
    }

    @Test
    void testStartGameWithInvalidInputNonNumeric() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        when(scanner.next())
                .thenReturn("aX")
                .thenReturn("exit");

        gameService.startGame(gameState);

        verify(boardDisplayer, atLeastOnce()).displayBoard(any(Board.class));
        assertTrue(gameState.isGameOver());
    }

    @Test
    void testStartGameDisplaysBoardAtStart() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");
        gameState.setGameOver(true);
        gameState.setWinner(Player.NONE);

        gameService.startGame(gameState);

        verify(boardDisplayer, atLeastOnce()).displayBoard(board);
    }

    @Test
    void testStartGameRecordsWinOnlyForHuman() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        gameState.setGameOver(true);
        gameState.setWinner(Player.HUMAN);

        gameService.startGame(gameState);

        verify(databaseService, times(1)).recordWin("TestPlayer");
    }

    @Test
    void testStartGameDoesNotRecordWinForComputer() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        gameState.setGameOver(true);
        gameState.setWinner(Player.COMPUTER);

        gameService.startGame(gameState);

        verify(databaseService, never()).recordWin(anyString());
    }

    @Test
    void testStartGameDoesNotRecordWinWhenNoWinner() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        gameState.setGameOver(true);
        gameState.setWinner(Player.NONE);

        gameService.startGame(gameState);

        verify(databaseService, never()).recordWin(anyString());
    }

    @Test
    void testStartGameAlwaysDisplaysHighScores() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        gameState.setGameOver(true);
        gameState.setWinner(Player.NONE);

        gameService.startGame(gameState);

        verify(databaseService).displayHighScores();
    }

    @Test
    void testStartGameWithSaveCommandDoesNotEndGame() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        when(scanner.next())
                .thenReturn("save")
                .thenReturn("exit");
        when(fileService.getDefaultSaveFile()).thenReturn("game_state.txt");

        gameService.startGame(gameState);

        verify(fileService).saveGameToFile(any(GameState.class), anyString());

        assertTrue(gameState.isGameOver());
    }

    @Test
    void testStartGameExitSetsGameOver() {
        Board board = new Board(10, 10);
        GameState gameState = new GameState(board, "TestPlayer");

        assertFalse(gameState.isGameOver());

        when(scanner.next()).thenReturn("exit");

        gameService.startGame(gameState);

        assertTrue(gameState.isGameOver());
    }
}