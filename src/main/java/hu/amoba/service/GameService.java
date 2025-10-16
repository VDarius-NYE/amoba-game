package hu.amoba.service;

import hu.amoba.display.BoardDisplayer;
import hu.amoba.model.GameState;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class GameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);
    private final BoardDisplayer boardDisplayer;
    private final MoveValidatorService moveValidator;
    private final ComputerPlayerService computerPlayer;
    private final FileService fileService;
    private final Scanner scanner;

    public GameService(BoardDisplayer boardDisplayer, MoveValidatorService moveValidator, ComputerPlayerService computerPlayer, FileService fileService, Scanner scanner) {
        this.boardDisplayer = boardDisplayer;
        this.moveValidator = moveValidator;
        this.computerPlayer = computerPlayer;
        this.fileService = fileService;
        this.scanner = scanner;
    }

    public void startGame(GameState gameState) {
        LOGGER.info("Welcome {}, the game will begin.", gameState.getPlayerName());
        LOGGER.info("The goal of the game: place 4 of your symbols horizontally, vertically or diagonally!");

        while (!gameState.isGameOver()) {
            boardDisplayer.displayBoard(gameState.getBoard());

            if (gameState.getCurrentPlayer() == Player.HUMAN) {
                handleHumanTurn(gameState);
            } else {
                handleComputerTurn(gameState);
            }
        }
        displayGameResults(gameState);
    }

    public void handleHumanTurn(GameState gameState) {
        LOGGER.info("{}'s turn (X). Enter a position (e.g. a5) or 'save' to save, 'exit' to exit:", gameState.getPlayerName());
        String input = scanner.next().toLowerCase();

        if ("exit".equals(input)) {
            LOGGER.info("The game has been stopped.");
            gameState.setGameOver(true);
            return;
        }

        if ("save".equals(input)) {
            fileService.saveGameToFile(gameState, fileService.getDefaultSaveFile());
            return;
        }

        Position position = parsePosition(input, gameState.getBoard());
        if (position != null && moveValidator.isValidMove(gameState.getBoard(), position)) {
            makeMove(gameState, position);
        }
    }

    private void handleComputerTurn(GameState gameState) {
        LOGGER.info("The computer's turn (0)..");
        Position position = computerPlayer.generateMove(gameState.getBoard());
        if (position != null) {
            makeMove(gameState, position);
            LOGGER.info("The computer's choice: {}{}", (char) ('a' + position.getCol()), position.getRow() + 1);
        }
    }

    private void makeMove(GameState gameState, Position position) {
        Player currentPlayer = gameState.getCurrentPlayer();
        gameState.getBoard().placeSymbol(position, currentPlayer);

        if (gameState.getBoard().checkWin(position, currentPlayer)) {
            gameState.setGameOver(true);
            gameState.setWinner(currentPlayer);
        } else {
            gameState.switchPlayer();
        }
    }

    private Position parsePosition(String input, hu.amoba.model.Board board) {
        if (input.length() < 2) {
            LOGGER.warn("Invalid format! Use e.g. a5");
            return null;
        }

        try {
            char colChar = input.charAt(0);
            int col = colChar - 'a';
            int row = Integer.parseInt(input.substring(1)) - 1;

            return new Position(row, col);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid format! Use e.g. a5");
            return null;
        }
    }

    private void displayGameResults(GameState gameState) {
        boardDisplayer.displayBoard((gameState.getBoard()));
        if (gameState.getWinner() == Player.HUMAN) {
            LOGGER.info("Congratulations {}! You won!", gameState.getPlayerName());
        } else if (gameState.getWinner() == Player.COMPUTER) {
            LOGGER.info("The computer won! Better luck next time!");
        } else {
            LOGGER.info("See you soon!");
        }
    }
}
