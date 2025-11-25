package hu.amoba.service;

import java.io.File;
import java.util.Scanner;

import hu.amoba.init.GameInit;
import hu.amoba.model.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);
    private final Scanner scanner;
    private final FileService fileService;
    private final DatabaseService databaseService;
    private final XmlService xmlService;
    private final GameInit gameInit;

    public MenuService(Scanner scanner, FileService fileService,
                       DatabaseService databaseService, XmlService xmlService, GameInit gameInit) {
        this.scanner = scanner;
        this.fileService = fileService;
        this.databaseService = databaseService;
        this.xmlService = xmlService;
        this.gameInit = gameInit;
    }

    private void displayMenu() {
        LOGGER.info("\n=================================");
        LOGGER.info("       AMOBA GAME MENU");
        LOGGER.info("=================================");
        LOGGER.info("Select an option:");
        LOGGER.info("1. Check the high scores");
        LOGGER.info("2. Load the latest save game (TXT)");
        LOGGER.info("3. Load the latest save game (XML)");
        LOGGER.info("4. Start a new game");
        LOGGER.info("5. Exit");
        LOGGER.info("=================================");
        LOGGER.info("Your choice: ");
    }

    public GameState showMainMenu() {
        while (true) {
            displayMenu();
            String choice = scanner.next();

            switch (choice) {
                case "1":
                    showHighScores();
                    break;
                case "2":
                    GameState loadedGame = loadSaveGame();
                    if (loadedGame != null) {
                        return loadedGame;
                    }
                    break;
                case "3":
                    GameState loadedXmlGame = loadXmlSaveGame();
                    if (loadedXmlGame != null) {
                        return loadedXmlGame;
                    }
                    break;
                case "4":
                    return startNewGame();
                case "5":
                    LOGGER.info("Exiting the game. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    LOGGER.warn("Invalid option! Please select 1, 2, 3, 4, or 5.");
            }
        }
    }

    private void showHighScores() {
        LOGGER.info("\n=================================");
        databaseService.displayHighScores();
        LOGGER.info("=================================");
        waitForBack();
    }

    private GameState loadSaveGame() {
        File saveFile = new File(fileService.getDefaultSaveFile());
        if (!saveFile.exists()) {
            LOGGER.warn("No save game found!");
            waitForBack();
            return null;
        }

        GameState gameState = fileService.loadGameFromFile(fileService.getDefaultSaveFile());
        if (gameState != null) {
            LOGGER.info("Save game loaded successfully!");
            return gameState;
        } else {
            LOGGER.error("Failed to load save game!");
            waitForBack();
            return null;
        }
    }

    private GameState loadXmlSaveGame() {
        File saveFile = new File(xmlService.getDefaultXmlFile());
        if (!saveFile.exists()) {
            LOGGER.warn("No XML save game found!");
            waitForBack();
            return null;
        }

        GameState gameState = xmlService.loadGameFromXml(xmlService.getDefaultXmlFile());
        if (gameState != null) {
            LOGGER.info("XML save game loaded successfully!");
            return gameState;
        } else {
            LOGGER.error("Failed to load XML save game!");
            waitForBack();
            return null;
        }
    }

    private GameState startNewGame() {
        LOGGER.info("\nStarting a new game...");
        return gameInit.initializeGame();
    }

    private void waitForBack() {
        LOGGER.info("\nType 'back' to return to the main menu: ");
        while (true) {
            String input = scanner.next().toLowerCase();
            if ("back".equals(input)) {
                break;
            } else {
                LOGGER.warn("Invalid input! Type 'back' to return: ");
            }
        }
    }
}