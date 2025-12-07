package hu.amoba;

import java.util.Random;
import java.util.Scanner;

import hu.amoba.display.BoardDisplayer;
import hu.amoba.init.GameInit;
import hu.amoba.model.GameState;
import hu.amoba.service.ComputerPlayerService;
import hu.amoba.service.DatabaseService;
import hu.amoba.service.FileService;
import hu.amoba.service.GameService;
import hu.amoba.service.MenuService;
import hu.amoba.service.MoveValidatorService;
import hu.amoba.service.XmlService;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileService fileService = new FileService();
        DatabaseService databaseService = new DatabaseService();
        XmlService xmlService = new XmlService();
        GameInit gameInit = new GameInit(scanner);

        MenuService menuService = new MenuService(scanner, fileService,
                databaseService, xmlService, gameInit);

        BoardDisplayer boardDisplayer = new BoardDisplayer();
        MoveValidatorService moveValidator = new MoveValidatorService();
        Random random = new Random();
        ComputerPlayerService computerPlayer = new ComputerPlayerService(random, moveValidator);
        GameService gameService = new GameService(boardDisplayer, moveValidator,
                computerPlayer, fileService, databaseService, xmlService, scanner);

        while (true) {
            GameState gameState = menuService.showMainMenu();
            if (gameState == null) {
                break;
            }

            gameService.startGame(gameState);
        }

        scanner.close();
    }
}