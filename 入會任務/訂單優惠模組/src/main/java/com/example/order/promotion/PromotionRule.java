package com.example.order.promotion;

public interface PromotionRule {

    void apply(PromotionContext context);

    default int getOrder() {
        return 0;
    }
}