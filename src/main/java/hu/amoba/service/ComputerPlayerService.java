package hu.amoba.service;

import hu.amoba.model.Board;
import hu.amoba.model.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayerService {
    private final Random random;
    private final MoveValidatorService moveValidator;

    public ComputerPlayerService(Random random, MoveValidatorService moveValidator) {
        this.random = random;
        this.moveValidator = moveValidator;
    }

    public Position generateMove(Board board) {
        List<Position> validMoves = getAllValidMoves(board);
        if (validMoves.isEmpty()) {
            return null;
        }
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    private List<Position> getAllValidMoves(Board board) {
        List<Position> validMoves = new ArrayList<>();

        if (board.isFirstMove()) {
            int middleRow = board.getRows() / 2;
            int middleCol = board.getCols() / 2;
            validMoves.add(new Position(middleRow, middleCol - 1));
            if (board.getCols() % 2 == 0) {
                validMoves.add(new Position(middleRow, middleCol - 1));
            }
        } else {
            for (int row = 0; row < board.getRows(); row++) {
                for (int col = 0; col < board.getCols(); col++) {
                    Position pos = new Position(row, col);
                    if (moveValidator.isValidMove(board, pos)) {
                        validMoves.add(pos);
                    }
                }
            }
        }
        return validMoves;
    }
}
