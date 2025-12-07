package hu.amoba.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.amoba.model.Board;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComputerPlayerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerPlayerService.class);

    private final Random random;
    private final MoveValidatorService moveValidator;

    public ComputerPlayerService(Random random, MoveValidatorService moveValidator) {
        this.random = random;
        this.moveValidator = moveValidator;
    }

    public Position generateMove(Board board) {
        if (board.isFirstMove()) {
            return generateFirstMove(board);
        } else {
            return generateSubsequentMove(board);
        }
    }

    private Position generateFirstMove(Board board) {
        int middleRow = board.getRows() / 2;
        int middleCol1 = (board.getCols() / 2) - 1;
        int middleCol2 = board.getCols() / 2;

        Position pos1 = new Position(middleRow, middleCol1);
        if (board.isValidPosition(pos1) && board.isEmpty(pos1)) {
            LOGGER.info("First move: ({}, {}) = {}{}",
                    middleRow, middleCol1, (char) ('a' + middleCol1), middleRow + 1);
            return pos1;
        }

        Position pos2 = new Position(middleRow, middleCol2);
        if (board.isValidPosition(pos2) && board.isEmpty(pos2)) {
            LOGGER.info("First move: ({}, {}) = {}{}",
                    middleRow, middleCol2, (char) ('a' + middleCol2), middleRow + 1);
            return pos2;
        }

        LOGGER.warn("Cannot generate first move!");
        return null;
    }

    private Position generateSubsequentMove(Board board) {
        List<Position> validMoves = new ArrayList<>();

        List<Position> occupied = board.getOccupiedPositions();

        if (occupied.isEmpty()) {
            LOGGER.warn("No occupied positions found, but isFirstMove is false!");
            return null;
        }

        LOGGER.debug("Scanning adjacent positions to {} occupied cells", occupied.size());

        for (Position occupiedPos : occupied) {
            int row = occupiedPos.getRow();
            int col = occupiedPos.getCol();

            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) {
                        continue;
                    }

                    int newRow = row + dr;
                    int newCol = col + dc;
                    Position newPos = new Position(newRow, newCol);

                    if (board.isValidPosition(newPos) &&
                            board.getPlayerAt(newPos) == Player.NONE &&
                            !validMoves.contains(newPos)) {
                        validMoves.add(newPos);
                    }
                }
            }
        }

        LOGGER.info("Found {} valid adjacent positions", validMoves.size());

        if (validMoves.isEmpty()) {
            LOGGER.warn("No valid moves found!");
            return null;
        }

        Position selectedMove = validMoves.get(random.nextInt(validMoves.size()));
        LOGGER.info("Selected move: ({}, {}) = {}{}",
                selectedMove.getRow(), selectedMove.getCol(),
                (char) ('a' + selectedMove.getCol()), selectedMove.getRow() + 1);

        return selectedMove;
    }
}