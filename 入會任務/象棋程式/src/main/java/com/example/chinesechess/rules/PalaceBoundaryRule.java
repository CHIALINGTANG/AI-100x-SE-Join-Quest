package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Side;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class PalaceBoundaryRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        Side side = context.getPiece().getSide();
        if (!context.getTo().isInsidePalace(side)) {
            return Optional.of(new Violation("超出九宮格"));
        }
        return Optional.empty();
    }
}
