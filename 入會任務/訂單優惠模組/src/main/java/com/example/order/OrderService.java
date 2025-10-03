package com.example.order;

import com.example.order.promotion.BuyOneGetOneCosmeticsRule;
import com.example.order.promotion.ElevenElevenQuantityDiscountRule;
import com.example.order.promotion.PromotionContext;
import com.example.order.promotion.PromotionRule;
import com.example.order.promotion.ThresholdDiscountRule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private final Map<Class<? extends PromotionRule>, PromotionRule> promotionRules = new LinkedHashMap<>();

    public void configureThresholdDiscount(double threshold, double discount) {
        registerPromotion(new ThresholdDiscountRule(threshold, discount));
    }

    public void configureBuyOneGetOneForCosmetics() {
        registerPromotion(new BuyOneGetOneCosmeticsRule());
    }

    public void configureElevenElevenQuantityDiscount() {
        registerPromotion(new ElevenElevenQuantityDiscountRule());
    }

    public void registerPromotion(PromotionRule rule) {
        if (rule == null) {
            return;
        }
        promotionRules.put(rule.getClass(), rule);
    }

    public Order checkout(List<OrderItem> items) {
        PromotionContext context = new PromotionContext(items);

        List<PromotionRule> rules = new ArrayList<>(promotionRules.values());
        rules.sort(Comparator.comparingInt(PromotionRule::getOrder));
        for (PromotionRule rule : rules) {
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