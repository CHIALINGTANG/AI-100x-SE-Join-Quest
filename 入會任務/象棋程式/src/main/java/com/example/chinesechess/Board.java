package com.example.chinesechess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Board {
    private final Map<Position, Piece> pieces = new HashMap<>();

    public void clear() {
        pieces.clear();
    }

    public void placePiece(Piece piece) {
        pieces.put(piece.getPosition(), piece);
    }

    public Optional<Piece> getPiece(Position position) {
        return Optional.ofNullable(pieces.get(position));
    }

    public boolean hasPiece(Position position) {
        return pieces.containsKey(position);
    }

    public Piece removePiece(Position position) {
        return pieces.remove(position);
    }

    public Piece movePiece(Position from, Position to) {
        Piece piece = pieces.remove(from);
        if (piece == null) {
            return null;
        }
        Piece captured = pieces.remove(to);
        piece.setPosition(to);
        pieces.put(to, piece);
        return captured;
    }

    public List<Position> positionsBetween(Position from, Position to) {
        List<Position> positions = new ArrayList<>();
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        if (rowDiff == 0 && colDiff == 0) {
            return positions;
        }

        int stepRow = Integer.compare(rowDiff, 0);
        int stepCol = Integer.compare(colDiff, 0);

        if (rowDiff != 0 && colDiff != 0 && Math.abs(rowDiff) != Math.abs(colDiff)) {
            return positions;
        }

        int currentRow = from.getRow() + stepRow;
        int currentCol = from.getCol() + stepCol;
        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            positions.add(new Position(currentRow, currentCol));
            currentRow += stepRow;
            currentCol += stepCol;
        }
        return positions;
    }

    public Board copy() {
        Board duplicate = new Board();
        for (Piece piece : pieces.values()) {
            Piece clone = piece.copy();
            duplicate.placePiece(new Piece(clone.getId(), clone.getType(), clone.getSide(), clone.getPosition()));
        }
        return duplicate;
    }

    public Optional<Piece> findGeneral(Side side) {
        return pieces.values().stream()
                .filter(p -> p.getType() == PieceType.GENERAL && p.getSide() == side)
                .findFirst();
    }

    public List<Piece> getPieces() {
        return new ArrayList<>(pieces.values());
    }
}
