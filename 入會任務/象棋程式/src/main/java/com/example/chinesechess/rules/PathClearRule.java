package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Position;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class PathClearRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        for (Position position : context.getPath()) {
            if (context.getBoard().hasPiece(position)) {
                return Optional.of(new Violation("路徑上不可有棋子"));
            }
        }
        return Optional.empty();
    }
}
