package com.example.chinesechess;

public class Piece {
    private final String id;
    private final PieceType type;
    private final Side side;
    private Position position;

    public Piece(String id, PieceType type, Side side, Position position) {
        this.id = id;
        this.type = type;
        this.side = side;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public PieceType getType() {
        return type;
    }

    public Side getSide() {
        return side;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Piece copy() {
        return new Piece(id, type, side, position);
    }
}
