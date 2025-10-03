package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Position;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class CannonScreenRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        long screens = context.getPath().stream()
                .filter(pos -> context.getBoard().hasPiece(pos))
                .count();
        if (context.isCapture()) {
            if (screens != 1) {
                return Optional.of(new Violation("炮吃子必須隔一子"));
            }
        } else {
            if (screens != 0) {
                return Optional.of(new Violation("炮平移時路徑需淨空"));
            }
        }
        return Optional.empty();
    }
}
