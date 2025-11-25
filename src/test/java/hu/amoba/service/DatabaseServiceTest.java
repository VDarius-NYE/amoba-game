package hu.amoba.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseServiceTest {

    private DatabaseService databaseService;

    @BeforeEach
    void setUp() {
        databaseService = new DatabaseService();
    }

    @AfterEach
    void tearDown() {
        File dbFile = new File("amoba_db.mv.db");
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    void testRecordWin() {
        databaseService.recordWin("TestPlayer");

        List<DatabaseService.PlayerStats> scores = databaseService.getHighScores();
        assertFalse(scores.isEmpty());
        assertEquals("TestPlayer", scores.get(0).getPlayerName());
        assertEquals(1, scores.get(0).getWins());
    }

    @Test
    void testRecordMultipleWins() {
        databaseService.recordWin("Player1");
        databaseService.recordWin("Player1");
        databaseService.recordWin("Player1");

        List<DatabaseService.PlayerStats> scores = databaseService.getHighScores();
        assertEquals(1, scores.size());
        assertEquals(3, scores.get(0).getWins());
    }

    @Test
    void testHighScoresOrdering() {
        databaseService.recordWin("Player1");
        databaseService.recordWin("Player2");
        databaseService.recordWin("Player2");
        databaseService.recordWin("Player3");
        databaseService.recordWin("Player3");
        databaseService.recordWin("Player3");

        List<DatabaseService.PlayerStats> scores = databaseService.getHighScores();
        assertEquals(3, scores.size());
        assertEquals("Player3", scores.get(0).getPlayerName());
        assertEquals(3, scores.get(0).getWins());
        assertEquals("Player2", scores.get(1).getPlayerName());
        assertEquals(2, scores.get(1).getWins());
    }

    @Test
    void testGetHighScoresEmpty() {
        List<DatabaseService.PlayerStats> scores = databaseService.getHighScores();
        assertTrue(scores.isEmpty());
    }
}