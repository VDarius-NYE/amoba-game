package hu.amoba;

import hu.amoba.display.BoardDisplayer;
import hu.amoba.init.GameInit;
import hu.amoba.model.GameState;
import hu.amoba.service.ComputerPlayerService;
import hu.amoba.service.FileService;
import hu.amoba.service.GameService;
import hu.amoba.service.MoveValidatorService;
import java.util.Random;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileService fileService = new FileService();
        GameInit gameInit = new GameInit(scanner);

        GameState gameState = fileService.loadGameFromFile(fileService.getDefaultSaveFile());

        if (gameState == null) {
            gameState = gameInit.initializeGame();
        }

        BoardDisplayer boardDisplayer = new BoardDisplayer();
        MoveValidatorService moveValidator = new MoveValidatorService();
        Random random = new Random();
        ComputerPlayerService computerPlayer = new ComputerPlayerService(random, moveValidator);
        GameService gameService = new GameService(boardDisplayer, moveValidator, computerPlayer, fileService, scanner);
        gameService.startGame(gameState);

        scanner.close();
    }
}