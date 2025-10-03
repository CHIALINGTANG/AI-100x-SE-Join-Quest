package com.example.chinesechess;

public class MoveCommand {
    private final Position from;
    private final Position to;
    private final Side requestSide;

    public MoveCommand(Position from, Position to, Side requestSide) {
        this.from = from;
        this.to = to;
        this.requestSide = requestSide;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Side getRequestSide() {
        return requestSide;
    }
}
