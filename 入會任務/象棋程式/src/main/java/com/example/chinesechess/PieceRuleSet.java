package com.example.chinesechess;

import com.example.chinesechess.rules.MovementRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PieceRuleSet {
    private final List<MovementRule> moveRules = new ArrayList<>();
    private final List<MovementRule> postMoveRules = new ArrayList<>();

    public PieceRuleSet addRule(MovementRule rule) {
        moveRules.add(rule);
        return this;
    }

    public PieceRuleSet addPostRule(MovementRule rule) {
        postMoveRules.add(rule);
        return this;
    }

    public List<MovementRule> getMoveRules() {
        return Collections.unmodifiableList(moveRules);
    }

    public List<MovementRule> getPostMoveRules() {
        return Collections.unmodifiableList(postMoveRules);
    }
}
