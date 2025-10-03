package com.example.order.promotion;

import com.example.order.OrderItem;
import com.example.order.Product;

public class ElevenElevenQuantityDiscountRule implements PromotionRule {

    private static final String APPAREL_CATEGORY = "apparel";
    private static final int ELIGIBLE_BLOCK_SIZE = 10;
    private static final double DISCOUNT_RATE = 0.20;
    private static final int ORDER = 200;

    @Override
    public void apply(PromotionContext context) {
        for (OrderItem item : context.getOriginalItems()) {
            if (!isEligible(item)) {
                continue;
            }

            int eligibleBlocks = item.getQuantity() / ELIGIBLE_BLOCK_SIZE;
            if (eligibleBlocks <= 0) {
                continue;
            }

            double unitPrice = item.getProduct().getUnitPrice();
            double blockValue = unitPrice * ELIGIBLE_BLOCK_SIZE;
            double discount = eligibleBlocks * blockValue * DISCOUNT_RATE;
            context.addDiscount(discount);
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    private boolean isEligible(OrderItem item) {
        if (item == null || item.getQuantity() < ELIGIBLE_BLOCK_SIZE) {
            return false;
        }
        Product product = item.getProduct();
        if (product == null || product.getCategory() == null) {
            return false;
        }
        return APPAREL_CATEGORY.equalsIgnoreCase(product.getCategory());
    }
}