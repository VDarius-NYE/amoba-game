package hu.amoba.display;

import hu.amoba.model.Board;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardDisplayer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardDisplayer.class);

    public void displayBoard(Board board) {
        LOGGER.info("\n{}", getPrettyPrint(board));
    }

    private String getPrettyPrint(Board board) {
        StringBuilder sb = new StringBuilder();
        int rows = board.getRows();
        int cols = board.getCols();

        sb.append("   ");
        for (int col = 0; col < cols; col++) {
            sb.append((char) ('a' + col)).append(" ");
        }
        sb.append("\n");

        for (int row = 0; row < rows; row++) {
            sb.append(String.format("%2d ", row + 1));
            for (int col = 0; col < cols; col++) {
                Player player = board.getPlayerAt(new Position(row, col));
                sb.append(player.getSymbol()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
