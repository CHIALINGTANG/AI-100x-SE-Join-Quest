package com.example.chinesechess;

import java.util.List;

public class MoveContext {
    private final Board board;
    private final Piece piece;
    private final Position from;
    private final Position to;
    private final Piece target;
    private final List<Position> path;

    public MoveContext(Board board, Piece piece, Position from, Position to, Piece target, List<Position> path) {
        this.board = board;
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.target = target;
        this.path = path;
    }

    public Board getBoard() {
        return board;
    }

    public Piece getPiece() {
        return piece;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getTarget() {
        return target;
    }

    public List<Position> getPath() {
        return path;
    }

    public int rowDiff() {
        return to.getRow() - from.getRow();
    }

    public int colDiff() {
        return to.getCol() - from.getCol();
    }

    public boolean isCapture() {
        return target != null;
    }

    public Board previewBoard() {
        Board snapshot = board.copy();
        snapshot.movePiece(from, to);
        return snapshot;
    }
}
