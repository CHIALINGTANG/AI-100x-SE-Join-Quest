package com.example.order.promotion;

import com.example.order.OrderItem;
import com.example.order.Product;

public class BuyOneGetOneCosmeticsRule implements PromotionRule {

    @Override
    public void apply(PromotionContext context) {
        for (OrderItem item : context.getFinalItems()) {
            if (isEligible(item)) {
                item.setQuantity(item.getQuantity() + 1);
            }
        }
    }

    private boolean isEligible(OrderItem item) {
        if (item == null || item.getQuantity() <= 0) {
            return false;
        }
        Product product = item.getProduct();
        if (product == null || product.getCategory() == null) {
            return false;
        }
        return "cosmetics".equalsIgnoreCase(product.getCategory());
    }
}
