package hu.amoba.model;

public class GameState {
    private final Board board;
    private final String playerName;
    private Player currentPlayer;
    private boolean isGameOver;
    private Player winner;

    public GameState(Board board, String playerName) {
        this.board = board;
        this.playerName = playerName;
        this.currentPlayer = Player.HUMAN;
        this.isGameOver = false;
        this.winner = Player.NONE;
    }

    public Board getBoard() {
        return board;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        this.currentPlayer = currentPlayer.getOpponent();
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
