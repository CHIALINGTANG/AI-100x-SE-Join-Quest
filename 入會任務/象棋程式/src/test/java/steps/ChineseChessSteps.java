package steps;

import com.example.chinesechess.ChineseChessGame;
import com.example.chinesechess.GameStatus;
import com.example.chinesechess.MoveCommand;
import com.example.chinesechess.MoveResult;
import com.example.chinesechess.PieceType;
import com.example.chinesechess.Position;
import com.example.chinesechess.Side;
import com.example.chinesechess.Violation;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ChineseChessSteps {

    private ChineseChessGame game;
    private MoveResult lastResult;

    @Before
    public void setup() {
        game = new ChineseChessGame();
        game.reset();
        lastResult = null;
    }

    @Given("^the board is empty except for a (\\w+) (\\w+) at \\((\\d+), (\\d+)\\)$")
    public void the_board_is_empty_except_for_a_piece(String color, String pieceName, int row, int col) {
        game.reset();
        game.placePiece(parsePieceType(pieceName), parseSide(color), new Position(row, col));
    }

    @Given("the board has:")
    public void the_board_has(DataTable table) {
        game.reset();
        for (Map<String, String> row : table.asMaps()) {
            String pieceDescriptor = row.get("Piece");
            String positionText = row.get("Position");
            ParsedPiece parsed = parsePieceDescriptor(pieceDescriptor);
            Position position = parsePosition(positionText);
            game.placePiece(parsed.type, parsed.side, position);
        }
    }

    @When("^(\\w+) moves the (\\w+) from \\((\\d+), (\\d+)\\) to \\((\\d+), (\\d+)\\)$")
    public void side_moves_piece(String color, String pieceName, int fromRow, int fromCol, int toRow, int toCol) {
        Side side = parseSide(color);
        Position from = new Position(fromRow, fromCol);
        Position to = new Position(toRow, toCol);
        lastResult = game.applyMove(new MoveCommand(from, to, side));
    }

    @Then("the move is legal")
    public void the_move_is_legal() {
        assertNotNull(lastResult, "尚未執行任何移動");
        assertTrue(lastResult.isLegal(), () -> formatViolations());
    }

    @Then("the move is illegal")
    public void the_move_is_illegal() {
        assertNotNull(lastResult, "尚未執行任何移動");
        assertFalse(lastResult.isLegal(), "預期移動非法");
    }

    @Then("{word} wins immediately")
    public void side_wins_immediately(String color) {
        assertNotNull(lastResult, "尚未執行任何移動");
        assertTrue(lastResult.isLegal(), formatViolations());
        Side side = parseSide(color);
        GameStatus expected = side == Side.RED ? GameStatus.RED_WIN : GameStatus.BLACK_WIN;
        assertEquals(expected, lastResult.getGameStatus(), "應判定立即獲勝");
    }

    @Then("the game is not over just from that capture")
    public void the_game_is_not_over_just_from_that_capture() {
        assertNotNull(lastResult, "尚未執行任何移動");
        assertTrue(lastResult.isLegal(), formatViolations());
        assertEquals(GameStatus.IN_PROGRESS, lastResult.getGameStatus(), "遊戲應持續進行");
    }

    private Side parseSide(String text) {
        String normalized = text.trim().toLowerCase(Locale.ROOT);
        if (normalized.startsWith("red")) {
            return Side.RED;
        }
        if (normalized.startsWith("black")) {
            return Side.BLACK;
        }
        throw new IllegalArgumentException("未知陣營: " + text);
    }

    private PieceType parsePieceType(String pieceName) {
        switch (pieceName.toLowerCase(Locale.ROOT)) {
            case "general":
                return PieceType.GENERAL;
            case "guard":
                return PieceType.GUARD;
            case "rook":
                return PieceType.ROOK;
            case "horse":
                return PieceType.HORSE;
            case "cannon":
                return PieceType.CANNON;
            case "elephant":
                return PieceType.ELEPHANT;
            case "soldier":
            case "pawn":
                return PieceType.SOLDIER;
            default:
                throw new IllegalArgumentException("未知棋子類型: " + pieceName);
        }
    }

    private ParsedPiece parsePieceDescriptor(String descriptor) {
        String[] parts = descriptor.trim().split(" +");
        if (parts.length < 2) {
            throw new IllegalArgumentException("棋子描述格式錯誤: " + descriptor);
        }
        Side side = parseSide(parts[0]);
        PieceType type = parsePieceType(parts[1]);
        return new ParsedPiece(side, type);
    }

    private Position parsePosition(String positionText) {
        String trimmed = positionText.replace("(", "").replace(")", "").trim();
        String[] parts = trimmed.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("座標格式錯誤: " + positionText);
        }
        int row = Integer.parseInt(parts[0].trim());
        int col = Integer.parseInt(parts[1].trim());
        return new Position(row, col);
    }

    private String formatViolations() {
        if (lastResult == null || lastResult.getViolations().isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder("違規原因: ");
        List<Violation> violations = lastResult.getViolations();
        for (int i = 0; i < violations.size(); i++) {
            builder.append(violations.get(i).getMessage());
            if (i < violations.size() - 1) {
                builder.append("; ");
            }
        }
        return builder.toString();
    }

    private static class ParsedPiece {
        final Side side;
        final PieceType type;

        ParsedPiece(Side side, PieceType type) {
            this.side = side;
            this.type = type;
        }
    }
}
