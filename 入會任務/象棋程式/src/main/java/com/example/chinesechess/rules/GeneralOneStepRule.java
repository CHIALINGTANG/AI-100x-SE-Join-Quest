package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class GeneralOneStepRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        int rowDiff = Math.abs(context.rowDiff());
        int colDiff = Math.abs(context.colDiff());
        if (rowDiff + colDiff != 1) {
            return Optional.of(new Violation("將軍只能直走一格"));
        }
        return Optional.empty();
    }
}
