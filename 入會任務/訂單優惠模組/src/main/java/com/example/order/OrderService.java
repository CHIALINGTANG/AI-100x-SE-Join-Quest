package com.example.order;

import com.example.order.promotion.BuyOneGetOneCosmeticsRule;
import com.example.order.promotion.PromotionContext;
import com.example.order.promotion.PromotionRule;
import com.example.order.promotion.ThresholdDiscountRule;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final List<PromotionRule> promotionRules = new ArrayList<>();

    public void configureThresholdDiscount(double threshold, double discount) {
        registerPromotion(new ThresholdDiscountRule(threshold, discount));
    }

    public void configureBuyOneGetOneForCosmetics() {
        registerPromotion(new BuyOneGetOneCosmeticsRule());
    }

    public void registerPromotion(PromotionRule rule) {
        if (rule == null) {
            return;
        }
        promotionRules.add(rule);
    }

    public Order checkout(List<OrderItem> items) {
        PromotionContext context = new PromotionContext(items);
        for (PromotionRule rule : promotionRules) {
            rule.apply(context);
        }

        Order order = new Order();
        order.setItems(context.snapshotFinalItems());
        order.setOriginalAmount(context.getOriginalAmount());
        order.setDiscount(context.getDiscount());
        double total = Math.max(0, context.getOriginalAmount() - context.getDiscount());
        order.setTotalAmount(total);
        return order;
    }
}
