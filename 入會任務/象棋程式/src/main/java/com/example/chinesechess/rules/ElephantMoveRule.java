package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class ElephantMoveRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        int rowDiff = Math.abs(context.rowDiff());
        int colDiff = Math.abs(context.colDiff());
        if (rowDiff != 2 || colDiff != 2) {
            return Optional.of(new Violation("象只能走田字"));
        }
        return Optional.empty();
    }
}
