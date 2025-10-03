package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class ElephantRiverRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        if (context.getTo().isAcrossRiver(context.getPiece().getSide())) {
            return Optional.of(new Violation("象不可過河"));
        }
        return Optional.empty();
    }
}
