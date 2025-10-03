package com.example.order.promotion;

public class ThresholdDiscountRule implements PromotionRule {

    private final double threshold;
    private final double discountAmount;

    public ThresholdDiscountRule(double threshold, double discountAmount) {
        this.threshold = threshold;
        this.discountAmount = discountAmount;
    }

    @Override
    public void apply(PromotionContext context) {
        if (context.getOriginalAmount() >= threshold) {
            context.addDiscount(discountAmount);
        }
    }
}
