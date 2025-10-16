package hu.amoba.service;

import hu.amoba.model.Board;
import hu.amoba.model.GameState;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    private static final String DEFAULT_SAVE_FILE = "game-save.vd";

    public GameState loadGameFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            LOGGER.info("No saved game found, starting from an empty map.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String playerName = reader.readLine();
            int rows = Integer.parseInt(reader.readLine());
            int cols = Integer.parseInt(reader.readLine());
            String currentPlayerStr = reader.readLine();

            Board board = new Board(rows, cols);
            GameState gameState = new GameState(board, playerName);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                char symbol = parts[2].charAt(0);

                Player player = symbol == 'X' ? Player.HUMAN : Player.COMPUTER;
                board.placeSymbol(new Position(row, col), player);
            }

            if ("COMPUTER".equals(currentPlayerStr)) {
                gameState.switchPlayer();
            }

            LOGGER.info("Game loaded from {} file.", filename);
            return gameState;
        } catch (IOException e) {
            LOGGER.error("Failed to read save file: {}", e.getMessage());
            return null;
        }
    }

    public void saveGameToFile(GameState gameState, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(gameState.getPlayerName());
            writer.newLine();
            writer.write(String.valueOf(gameState.getBoard().getRows()));
            writer.newLine();
            writer.write(String.valueOf(gameState.getBoard().getCols()));
            writer.newLine();
            writer.write(gameState.getCurrentPlayer().name());
            writer.newLine();

            for (Position pos : gameState.getBoard().getOccupiedPositions()) {
                Player player = gameState.getBoard().getPlayerAt(pos);
                writer.write(pos.getRow() + "," + pos.getCol() + "," + player.getSymbol());
            }

            LOGGER.info("The game has been save to: {} file.", filename);
        } catch (IOException e) {
            LOGGER.error("Error creating save file: {}", e.getMessage());
        }
    }

    public String getDefaultSaveFile() {
        return DEFAULT_SAVE_FILE;
    }
}
