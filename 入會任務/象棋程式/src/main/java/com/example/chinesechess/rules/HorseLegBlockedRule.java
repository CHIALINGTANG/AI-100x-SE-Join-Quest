package com.example.chinesechess.rules;

import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Position;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class HorseLegBlockedRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        int rowDiff = context.rowDiff();
        int colDiff = context.colDiff();
        Position from = context.getFrom();
        Position legBlock;
        if (Math.abs(rowDiff) == 2) {
            legBlock = from.offset(rowDiff / 2, 0);
        } else {
            legBlock = from.offset(0, colDiff / 2);
        }
        if (context.getBoard().hasPiece(legBlock)) {
            return Optional.of(new Violation("馬腳被卡住"));
        }
        return Optional.empty();
    }
}
