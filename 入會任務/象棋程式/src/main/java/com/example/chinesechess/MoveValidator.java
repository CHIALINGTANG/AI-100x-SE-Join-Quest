package com.example.chinesechess;

import com.example.chinesechess.rules.CannonScreenRule;
import com.example.chinesechess.rules.ElephantMidpointRule;
import com.example.chinesechess.rules.ElephantMoveRule;
import com.example.chinesechess.rules.ElephantRiverRule;
import com.example.chinesechess.rules.GeneralFaceOffRule;
import com.example.chinesechess.rules.GeneralOneStepRule;
import com.example.chinesechess.rules.GuardDiagonalRule;
import com.example.chinesechess.rules.HorseLegBlockedRule;
import com.example.chinesechess.rules.HorseMovementRule;
import com.example.chinesechess.rules.MovementRule;
import com.example.chinesechess.rules.PalaceBoundaryRule;
import com.example.chinesechess.rules.PathClearRule;
import com.example.chinesechess.rules.SoldierMovementRule;
import com.example.chinesechess.rules.StraightLineRule;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MoveValidator {
    private final Map<PieceType, PieceRuleSet> registry = new EnumMap<>(PieceType.class);
    private final MovementRule generalFaceOffRule = new GeneralFaceOffRule();

    public MoveValidator() {
        registerDefaults();
    }

    private void registerDefaults() {
        PieceRuleSet generalRules = new PieceRuleSet()
                .addRule(new GeneralOneStepRule())
                .addRule(new PalaceBoundaryRule())
                .addPostRule(generalFaceOffRule);

        PieceRuleSet guardRules = new PieceRuleSet()
                .addRule(new GuardDiagonalRule())
                .addRule(new PalaceBoundaryRule())
                .addPostRule(generalFaceOffRule);

        PieceRuleSet rookRules = new PieceRuleSet()
                .addRule(new StraightLineRule())
                .addRule(new PathClearRule())
                .addPostRule(generalFaceOffRule);

        PieceRuleSet horseRules = new PieceRuleSet()
                .addRule(new HorseMovementRule())
                .addRule(new HorseLegBlockedRule())
                .addPostRule(generalFaceOffRule);

        PieceRuleSet cannonRules = new PieceRuleSet()
                .addRule(new StraightLineRule())
                .addRule(new CannonScreenRule())
                .addPostRule(generalFaceOffRule);

        PieceRuleSet elephantRules = new PieceRuleSet()
                .addRule(new ElephantMoveRule())
                .addRule(new ElephantMidpointRule())
                .addRule(new ElephantRiverRule())
                .addPostRule(generalFaceOffRule);

        PieceRuleSet soldierRules = new PieceRuleSet()
                .addRule(new SoldierMovementRule())
                .addPostRule(generalFaceOffRule);

        registry.put(PieceType.GENERAL, generalRules);
        registry.put(PieceType.GUARD, guardRules);
        registry.put(PieceType.ROOK, rookRules);
        registry.put(PieceType.HORSE, horseRules);
        registry.put(PieceType.CANNON, cannonRules);
        registry.put(PieceType.ELEPHANT, elephantRules);
        registry.put(PieceType.SOLDIER, soldierRules);
    }

    public List<Violation> validate(MoveContext context) {
        List<Violation> violations = new ArrayList<>();
        PieceRuleSet ruleSet = registry.get(context.getPiece().getType());
        if (ruleSet == null) {
            violations.add(new Violation("缺少棋子規則"));
            return violations;
        }
        for (MovementRule rule : ruleSet.getMoveRules()) {
            Optional<Violation> violation = rule.validate(context);
            violation.ifPresent(violations::add);
        }
        if (!violations.isEmpty()) {
            return violations;
        }
        for (MovementRule rule : ruleSet.getPostMoveRules()) {
            Optional<Violation> violation = rule.validate(context);
            violation.ifPresent(violations::add);
        }
        return violations;
    }
}
