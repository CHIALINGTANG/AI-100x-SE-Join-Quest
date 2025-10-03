package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class GuardDiagonalRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        int rowDiff = Math.abs(context.rowDiff());
        int colDiff = Math.abs(context.colDiff());
        if (rowDiff != 1 || colDiff != 1) {
            return Optional.of(new Violation("士只能斜走一格"));
        }
        return Optional.empty();
    }
}
