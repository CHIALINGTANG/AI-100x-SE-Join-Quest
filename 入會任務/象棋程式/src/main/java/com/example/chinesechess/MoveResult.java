package com.example.chinesechess;

import java.util.Collections;
import java.util.List;

public class MoveResult {
    private final boolean legal;
    private final List<Violation> violations;
    private final Piece captured;
    private final GameStatus gameStatus;

    private MoveResult(boolean legal, List<Violation> violations, Piece captured, GameStatus status) {
        this.legal = legal;
        this.violations = violations;
        this.captured = captured;
        this.gameStatus = status;
    }

    public static MoveResult legal(Piece captured, GameStatus status) {
        return new MoveResult(true, Collections.emptyList(), captured, status);
    }

    public static MoveResult illegal(List<Violation> violations) {
        return new MoveResult(false, violations, null, GameStatus.IN_PROGRESS);
    }

    public boolean isLegal() {
        return legal;
    }

    public List<Violation> getViolations() {
        return violations;
    }

    public Piece getCaptured() {
        return captured;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
