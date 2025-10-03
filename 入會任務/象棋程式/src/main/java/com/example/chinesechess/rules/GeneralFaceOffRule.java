package com.example.chinesechess.rules;

import com.example.chinesechess.Board;
import com.example.chinesechess.MoveContext;
import com.example.chinesechess.Piece;
import com.example.chinesechess.Position;
import com.example.chinesechess.Side;
import com.example.chinesechess.Violation;

import java.util.Optional;

public class GeneralFaceOffRule implements MovementRule {
    @Override
    public Optional<Violation> validate(MoveContext context) {
        Board preview = context.previewBoard();
        Optional<Piece> redGeneral = preview.findGeneral(Side.RED);
        Optional<Piece> blackGeneral = preview.findGeneral(Side.BLACK);
        if (redGeneral.isEmpty() || blackGeneral.isEmpty()) {
            return Optional.empty();
        }
        Position redPos = redGeneral.get().getPosition();
        Position blackPos = blackGeneral.get().getPosition();
        if (redPos.getCol() != blackPos.getCol()) {
            return Optional.empty();
        }
        long blockers = preview.positionsBetween(redPos, blackPos).stream()
                .filter(preview::hasPiece)
                .count();
        if (blockers == 0) {
            return Optional.of(new Violation("帥將不可照面"));
        }
        return Optional.empty();
    }
}
