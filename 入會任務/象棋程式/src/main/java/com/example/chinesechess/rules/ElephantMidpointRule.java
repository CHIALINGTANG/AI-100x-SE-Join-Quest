package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Position;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class ElephantMidpointRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        Position from = context.getFrom();
        int rowDiff = context.rowDiff();
        int colDiff = context.colDiff();
        Position mid = from.offset(rowDiff / 2, colDiff / 2);
        if (context.getBoard().hasPiece(mid)) {
            return Optional.of(new Violation("象眼被堵"));
        }
        return Optional.empty();
    }
}
