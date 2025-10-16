package hu.amoba.service;

import hu.amoba.model.Board;
import hu.amoba.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveValidatorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveValidatorService.class);

    public boolean isValidMove(Board board, Position position) {
        if (!board.isValidPosition(position)) {
            LOGGER.warn("The position is not on the board!");
            return false;
        }

        if (!board.isEmpty(position)) {
            LOGGER.warn("This position is already occupied!");
            return false;
        }

        if (board.isFirstMove()) {
            if (!board.isInMiddle(position)) {
                LOGGER.warn("The first move has to be in the middle of the board!");
                return false;
            }
        } else {
            if (!board.isAdjacentToOccupied(position)) {
                LOGGER.warn("The move must be adjacent to an already occupied position!");
                return false;
            }
        }
        return true;
    }
}
