package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class StraightLineRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        int rowDiff = context.rowDiff();
        int colDiff = context.colDiff();
        if (rowDiff != 0 && colDiff != 0) {
            return Optional.of(new Violation("此棋子必須走直線"));
        }
        if (rowDiff == 0 && colDiff == 0) {
            return Optional.of(new Violation("目的地需不同於起點"));
        }
        return Optional.empty();
    }
}
