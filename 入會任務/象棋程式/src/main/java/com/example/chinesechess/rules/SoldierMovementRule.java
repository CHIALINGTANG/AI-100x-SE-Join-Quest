package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Side;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class SoldierMovementRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        Side side = context.getPiece().getSide();
        int rowDiff = context.rowDiff();
        int colDiff = context.colDiff();
        boolean crossed = context.getFrom().isAcrossRiver(side);

        if (!crossed) {
            if (rowDiff != side.forward() || colDiff != 0) {
                return Optional.of(new Violation("兵過河前只能直進"));
            }
            return Optional.empty();
        }

        boolean forward = rowDiff == side.forward() && colDiff == 0;
        boolean horizontal = rowDiff == 0 && Math.abs(colDiff) == 1;
        if (!forward && !horizontal) {
            return Optional.of(new Violation("兵過河後不可後退"));
        }
        return Optional.empty();
    }
}
