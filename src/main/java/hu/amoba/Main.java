package hu.amoba;

import hu.amoba.display.BoardDisplayer;
import hu.amoba.init.GameInit;
import hu.amoba.model.GameState;
import hu.amoba.service.ComputerPlayerService;
import hu.amoba.service.DatabaseService;
import hu.amoba.service.FileService;
import hu.amoba.service.GameService;
import hu.amoba.service.MoveValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileService fileService = new FileService();
        DatabaseService databaseService = new DatabaseService();
        GameInit gameInit = new GameInit(scanner);

        GameState gameState = null;

        File saveFile = new File(fileService.getDefaultSaveFile());
        if (saveFile.exists()) {
            LOGGER.info("A saved game was found! Do you want to load it? (yes/no): ");
            String response = scanner.next().toLowerCase();

            if ("yes".equals(response) || "y".equals(response)) {
                gameState = fileService.loadGameFromFile(fileService.getDefaultSaveFile());
            }
        }

        if (gameState == null) {
            gameState = gameInit.initializeGame();
        }

        BoardDisplayer boardDisplayer = new BoardDisplayer();
        MoveValidatorService moveValidator = new MoveValidatorService();
        Random random = new Random();
        ComputerPlayerService computerPlayer = new ComputerPlayerService(random, moveValidator);
        GameService gameService = new GameService(boardDisplayer, moveValidator,
                computerPlayer, fileService, databaseService, scanner);

        gameService.startGame(gameState);

        scanner.close();
    }
}