package hu.amoba.model;

public enum Player {
    HUMAN('X'),
    COMPUTER('0'),
    NONE(' ');

    private final char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public Player getOpponent() {
        if (this == HUMAN) {
            return COMPUTER;
        } else if (this == COMPUTER) {
            return HUMAN;
        }
        return NONE;
    }
}
