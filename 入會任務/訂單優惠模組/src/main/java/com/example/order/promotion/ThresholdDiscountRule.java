package com.example.order.promotion;

import com.example.order.OrderItem;
import com.example.order.Product;

public class ThresholdDiscountRule implements PromotionRule {

    private static final int DISCOUNT_BLOCK_SIZE = 10;
    private static final int ORDER = 300;

    private final double threshold;
    private final double discountAmount;

    public ThresholdDiscountRule(double threshold, double discountAmount) {
        this.threshold = threshold;
        this.discountAmount = discountAmount;
    }

    @Override
    public void apply(PromotionContext context) {
        double subtotal = context.getCurrentSubtotal();
        if (subtotal > threshold && shouldApplyThreshold(context)) {
            context.addDiscount(discountAmount);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    private boolean shouldApplyThreshold(PromotionContext context) {
        for (OrderItem item : context.getOriginalItems()) {
            if (item == null) {
                continue;
            }
            Product product = item.getProduct();
            if (product == null) {
                continue;
            }
            String category = product.getCategory();
            if (category == null || !"apparel".equalsIgnoreCase(category)) {
                return true;
            }
            if (product.getUnitPrice() * DISCOUNT_BLOCK_SIZE > threshold) {
                return true;
            }
        }
        return false;
    }
}