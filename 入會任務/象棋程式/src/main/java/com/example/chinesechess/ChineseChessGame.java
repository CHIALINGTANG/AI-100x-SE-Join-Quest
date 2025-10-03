package com.example.chinesechess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChineseChessGame {
    private final Board board;
    private final MoveValidator validator;
    private final AtomicInteger idGenerator = new AtomicInteger();
    private GameStatus status = GameStatus.IN_PROGRESS;

    public ChineseChessGame() {
        this.board = new Board();
        this.validator = new MoveValidator();
    }

    public void reset() {
        board.clear();
        status = GameStatus.IN_PROGRESS;
        idGenerator.set(0);
    }

    public Piece placePiece(PieceType type, Side side, Position position) {
        Piece piece = new Piece(nextId(type), type, side, position);
        board.placePiece(piece);
        return piece;
    }

    private String nextId(PieceType type) {
        return type.name() + "-" + idGenerator.incrementAndGet();
    }

    public Board getBoard() {
        return board;
    }

    public GameStatus getStatus() {
        return status;
    }

    public MoveResult applyMove(MoveCommand command) {
        List<Violation> violations = new ArrayList<>();
        Position from = command.getFrom();
        Position to = command.getTo();

        if (!from.isInsideBoard() || !to.isInsideBoard()) {
            violations.add(new Violation("超出棋盤"));
            return MoveResult.illegal(violations);
        }

        Piece piece = board.getPiece(from).orElse(null);
        if (piece == null) {
            violations.add(new Violation("起始位置沒有棋子"));
            return MoveResult.illegal(violations);
        }

        if (piece.getSide() != command.getRequestSide()) {
            violations.add(new Violation("不能移動對方棋子"));
            return MoveResult.illegal(violations);
        }

        Piece target = board.getPiece(to).orElse(null);
        if (target != null && target.getSide() == piece.getSide()) {
            violations.add(new Violation("不可吃己方棋"));
            return MoveResult.illegal(violations);
        }

        List<Position> path = board.positionsBetween(from, to);
        MoveContext context = new MoveContext(board, piece, from, to, target, path);

        violations.addAll(validator.validate(context));
        if (!violations.isEmpty()) {
            return MoveResult.illegal(violations);
        }

        Piece captured = board.movePiece(from, to);
        if (captured != null && captured.getType() == PieceType.GENERAL) {
            status = piece.getSide() == Side.RED ? GameStatus.RED_WIN : GameStatus.BLACK_WIN;
        }
        return MoveResult.legal(captured, status);
    }
}
