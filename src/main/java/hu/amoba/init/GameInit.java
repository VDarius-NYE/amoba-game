package hu.amoba.init;

import hu.amoba.model.Board;
import hu.amoba.model.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class GameInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameInit.class);
    private static final int MIN_SIZE = 5;
    private static final int MAX_SIZE = 25;
    private final Scanner scanner;

    public GameInit(Scanner scanner) {
        this.scanner = scanner;
    }

    public GameState initializeGame() {
        String playerName = getPlayerName();
        Board board = createBoard();
        return new GameState(board, playerName);
    }

    private String getPlayerName() {
        LOGGER.info("Name: ");
        return scanner.next();
    }

    private Board createBoard() {
        int rows = getBoardSize("Number of rows (5-25): ");
        int cols = getBoardSize("Number of columns (5-25): ");
        return new Board(rows, cols);
    }

    private int getBoardSize(String message) {
        int size;
        do {
            LOGGER.info(message);
            size = scanner.nextInt();
            if (size < MIN_SIZE || size > MAX_SIZE) {
                LOGGER.warn("Invalid map size, please provide the map size between these numbers: {} and {}", MIN_SIZE, MAX_SIZE);
            }
        } while (size < MIN_SIZE || size > MAX_SIZE);
        return size;
    }
}
