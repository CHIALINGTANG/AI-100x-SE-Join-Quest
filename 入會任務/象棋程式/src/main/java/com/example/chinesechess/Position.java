package com.example.chinesechess;

import java.util.Objects;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position offset(int dr, int dc) {
        return new Position(row + dr, col + dc);
    }

    public boolean isInsideBoard() {
        return row >= 1 && row <= 10 && col >= 1 && col <= 9;
    }

    public boolean isInsidePalace(Side side) {
        if (side == Side.RED) {
            return row >= 1 && row <= 3 && col >= 4 && col <= 6;
        }
        return row >= 8 && row <= 10 && col >= 4 && col <= 6;
    }

    public boolean isAcrossRiver(Side side) {
        if (side == Side.RED) {
            return row >= 6;
        }
        return row <= 5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
