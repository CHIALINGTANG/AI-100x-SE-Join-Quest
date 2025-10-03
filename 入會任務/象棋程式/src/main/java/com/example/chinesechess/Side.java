package com.example.chinesechess;

public enum Side {
    RED(1),
    BLACK(-1);

    private final int forwardDirection;

    Side(int forwardDirection) {
        this.forwardDirection = forwardDirection;
    }

    public int forward() {
        return forwardDirection;
    }
}
