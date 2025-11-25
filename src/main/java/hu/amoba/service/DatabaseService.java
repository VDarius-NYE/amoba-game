package hu.amoba.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseService.class);
    private static final String DB_URL = "jdbc:h2:./amoba_db";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public DatabaseService() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS player_stats ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "player_name VARCHAR(255) NOT NULL UNIQUE, "
                    + "wins INT DEFAULT 0"
                    + ")";

            stmt.execute(createTableSQL);
            LOGGER.info("Database initialized successfully.");

        } catch (SQLException e) {
            LOGGER.error("Failed to initialize database: {}", e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public void recordWin(String playerName) {
        String selectSQL = "SELECT wins FROM player_stats WHERE player_name = ?";
        String insertSQL = "INSERT INTO player_stats (player_name, wins) VALUES (?, 1)";
        String updateSQL = "UPDATE player_stats SET wins = wins + 1 WHERE player_name = ?";

        try (Connection conn = getConnection()) {
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                selectStmt.setString(1, playerName);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                        updateStmt.setString(1, playerName);
                        updateStmt.executeUpdate();
                        LOGGER.info("Updated win count for player: {}", playerName);
                    }
                } else {
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                        insertStmt.setString(1, playerName);
                        insertStmt.executeUpdate();
                        LOGGER.info("Added new player to database: {}", playerName);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to record win: {}", e.getMessage());
        }
    }

    public List<PlayerStats> getHighScores() {
        List<PlayerStats> highScores = new ArrayList<>();
        String sql = "SELECT player_name, wins FROM player_stats ORDER BY wins DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("player_name");
                int wins = rs.getInt("wins");
                highScores.add(new PlayerStats(name, wins));
            }

        } catch (SQLException e) {
            LOGGER.error("Failed to retrieve high scores: {}", e.getMessage());
        }

        return highScores;
    }

    public void displayHighScores() {
        List<PlayerStats> scores = getHighScores();

        if (scores.isEmpty()) {
            LOGGER.info("No high scores yet!");
            return;
        }

        LOGGER.info("=== HIGH SCORES ===");
        int rank = 1;
        for (PlayerStats stats : scores) {
            LOGGER.info("{}. {} - {} wins", rank++, stats.getPlayerName(), stats.getWins());
        }
        LOGGER.info("===================");
    }

    public static class PlayerStats {
        private final String playerName;
        private final int wins;

        public PlayerStats(String playerName, int wins) {
            this.playerName = playerName;
            this.wins = wins;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getWins() {
            return wins;
        }
    }
}