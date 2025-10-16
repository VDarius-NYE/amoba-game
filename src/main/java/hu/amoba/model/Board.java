package hu.amoba.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int rows;
    private final int cols;
    private final Player[][] grid;
    private final List<Position> occupiedPositions;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Player[rows][cols];
        this.occupiedPositions = new ArrayList<>();
        InitializeBoard();
    }

    private void InitializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Player.NONE;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Player getPlayerAt(Position position) {
        return grid[position.getRow()][position.getCol()];
    }

    public boolean isValidPosition(Position position) {
        return position.getRow() >= 0 && position.getRow() < rows
                && position.getCol() >= 0 && position.getCol() < cols;
    }

    public boolean isEmpty(Position position) {
        return getPlayerAt(position) == Player.NONE;
    }

    public boolean isFirstMove() {
        return occupiedPositions.isEmpty();
    }

    public boolean isAdjacentToOccupied(Position position) {
        for (Position occupied : occupiedPositions) {
            if (position.isAdjacentTo(occupied)) {
                return true;
            }
        }
        return false;
    }

    public void placeSymbol(Position position, Player player) {
        grid[position.getRow()][position.getCol()] = player;
        occupiedPositions.add(position);
    }

    public List<Position> getOccupiedPositions() {
        return new ArrayList<>(occupiedPositions);
    }

    public boolean checkWin(Position lastMove, Player player) {
        return checkDirection(lastMove, player, 0, 1)
                || checkDirection(lastMove, player, 1, 0)
                || checkDirection(lastMove, player, 1, 1)
                || checkDirection(lastMove, player, 1, -1);
    }

    private boolean checkDirection(Position position, Player player, int rowDir, int colDir) {
        int count = 1;
        count += countInDirection(position, player, rowDir, colDir);
        count += countInDirection(position, player, -rowDir, -colDir);
        return count >= 4;
    }

    private int countInDirection(Position position, Player player, int rowDir, int colDir) {
        int count = 0;
        int row = position.getRow() + rowDir;
        int col = position.getCol() + colDir;

        while (row >= 0 && row < rows && col >= 0 && col < cols
                && grid[row][col] == player) {
            count++;
            row += rowDir;
            col += colDir;
        }

        return count;
    }
}
