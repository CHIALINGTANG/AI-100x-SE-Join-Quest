package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class HorseMovementRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        int rowDiff = Math.abs(context.rowDiff());
        int colDiff = Math.abs(context.colDiff());
        boolean lShape = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
        if (!lShape) {
            return Optional.of(new Violation("馬必須走日字"));
        }
        return Optional.empty();
    }
}
